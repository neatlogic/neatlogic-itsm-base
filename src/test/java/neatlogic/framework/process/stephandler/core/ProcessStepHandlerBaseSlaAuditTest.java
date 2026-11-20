package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.asynchronization.threadlocal.*;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerFactory;
import neatlogic.framework.process.constvalue.*;
import neatlogic.framework.process.crossover.*;
import neatlogic.framework.process.dto.*;
import neatlogic.framework.process.fulltextindex.ProcessFullTextIndexType;
import neatlogic.framework.process.exception.processtask.ProcessTaskException;
import neatlogic.framework.process.operationauth.core.*;
import neatlogic.framework.util.SpringContextUtil;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.fulltextindex.core.IFullTextIndexHandler;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.slf4j.MDC;
import java.lang.reflect.*;
import java.util.function.Function;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

/** 执行真实生命周期方法，隔离权限、工厂和异步调度；不依赖 ITSM 实现模块。 */
public class ProcessStepHandlerBaseSlaAuditTest {
    private final List<AutoCloseable> resources = new ArrayList<>();
    private final List<String> events = new ArrayList<>();
    private final List<ProcessTaskStepVo> auditSnapshots = new ArrayList<>();
    private final List<ProcessTaskStepVo> auditArguments = new ArrayList<>();
    private final Stub mapper = new Stub();
    private final Stub util = new Stub();
    private final Stub configs = new Stub();
    private TestHandler handler;
    private ProcessTaskStepVo current;
    private ProcessTaskStepVo stored;
    private List<ProcessTaskStepRelVo> incoming = Collections.emptyList();

    /** 内存接口替身仅允许显式配置的方法，新增外部调用会直接失败，防止遗漏被默认成功掩盖。 */
    private static class Stub {
        private final Map<String, Function<Object[], Object>> answers = new HashMap<>();
        private final List<String> calls = new ArrayList<>();
        void answer(String name, Function<Object[], Object> answer) { answers.put(name, answer); }
        void value(String name, Object value) { answer(name, args -> value); }
        void emptyLists(String... names) { for (String name : names) value(name, Collections.emptyList()); }
        void writes(String... names) { for (String name : names) value(name, 1); }
        <T> T proxy(Class<T> type) {
            return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (proxy, method, args) -> {
                if (method.getDeclaringClass() == Object.class) {
                    if (method.getName().equals("toString")) return "TestStub<" + type.getSimpleName() + ">";
                    if (method.getName().equals("hashCode")) return System.identityHashCode(proxy);
                    return proxy == args[0];
                }
                Function<Object[], Object> answer = answers.get(method.getName());
                if (answer == null) throw new AssertionError("未配置的接口调用: " + method);
                calls.add(method.getName());
                Object result = answer.apply(args);
                return method.getReturnType() == void.class ? null : result;
            }));
        }
    }

    /** 保存并清空工厂注册表；恢复时保留原 Map 实例，兼容 final 静态字段。 */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map registry(Class<?> type, String name) throws Exception {
        Field field = type.getDeclaredField(name); field.setAccessible(true);
        Map map = (Map) field.get(null); Map original = new HashMap(map);
        resources.add(() -> { map.clear(); map.putAll(original); });
        map.clear(); return map;
    }

    /** 仅反射注入已有静态字段，并记录恢复操作，不修改生产访问级别。 */
    private void replaceField(Class<?> type, String name, Object value) throws Exception {
        Field field = type.getDeclaredField(name); field.setAccessible(true);
        Object original = field.get(null);
        resources.add(() -> field.set(null, original)); field.set(null, value);
    }

    /** 保存 ThreadLocal 原始对象，不通过 init(copy) 丢失原上下文身份。 */
    @SuppressWarnings("unchecked")
    private void preserveContext(Class<?> type) throws Exception {
        Field field = type.getDeclaredField("instance"); field.setAccessible(true);
        ThreadLocal<Object> local = (ThreadLocal<Object>) field.get(null);
        Object original = local.get();
        resources.add(() -> { if (original == null) local.remove(); else local.set(original); });
        local.remove();
    }

    /** 真实生命周期、分派和权限管理器配合内存数据；事务只登记回调，不提交、不启动线程。 */
    @Before @SuppressWarnings({"rawtypes", "unchecked"})
    public void setup() throws Exception {
        Map<String, String> originalMdc = MDC.getCopyOfContextMap();
        resources.add(() -> { if (originalMdc == null) MDC.clear(); else MDC.setContextMap(originalMdc); });
        preserveContext(UserContext.class); preserveContext(TenantContext.class);
        UserContext.init((UserContext) null).setUserUuid("test-user");
        TenantContext.init("test");
        StaticApplicationContext context = new StaticApplicationContext();
        resources.add(context::close);
        context.getBeanFactory().registerSingleton("messageSourceAccessor", new MessageSourceAccessor(new StaticMessageSource()));
        replaceField(SpringContextUtil.class, "ctx", context);
        // 不接管调用方事务：本测试必须独立运行，以免清除其他测试注册的回调。
        assertFalse(TransactionSynchronizationManager.isSynchronizationActive());
        TransactionSynchronizationManager.initSynchronization();
        resources.add(TransactionSynchronizationManager::clearSynchronization);
        Field postField = ProcessTaskOperatePostProcessorFactory.class.getDeclaredField("postProcessorList");
        postField.setAccessible(true);
        List post = (List) postField.get(null); List originalPost = new ArrayList(post);
        resources.add(() -> { post.clear(); post.addAll(originalPost); }); post.clear();
        registry(ProcessStepInternalHandlerFactory.class, "componentMap");
        handler = new TestHandler();
        registry(ProcessStepHandlerFactory.class, "componentMap").put("test-handler", handler);
        Stub index = new Stub(); index.value("createIndex", null);
        registry(FullTextIndexHandlerFactory.class, "componentMap")
                .put(ProcessFullTextIndexType.PROCESSTASK.getType(), index.proxy(IFullTextIndexHandler.class));
        Stub auth = new Stub(); auth.value("getOperateMap", PredicateResult.ACCEPT);
        auth.value("getAllOperationTypeList", Arrays.asList(ProcessTaskOperationType.values()));
        Stub stepAuth = new Stub(); stepAuth.value("getOperateMap", PredicateResult.ACCEPT);
        stepAuth.value("getAllOperationTypeList", Arrays.asList(ProcessTaskStepOperationType.values()));
        Map authRegistry = registry(OperationAuthHandlerFactory.class, "componentMap");
        authRegistry.put(OperationAuthHandlerType.TASK.getValue(), auth.proxy(IOperationAuthHandler.class));
        authRegistry.put(OperationAuthHandlerType.STEP.getValue(), stepAuth.proxy(IOperationAuthHandler.class));
        current = step(101L, "pending", 1); stored = step(101L, "pending", 1);
        mapper.answer("getProcessTaskStepBaseInfoById", args -> {
            if (Long.valueOf(101).equals(args[0])) return stored;
            assertEquals("仅重新审批存在第二个目标步骤", Long.valueOf(202), args[0]);
            return step(202L, "hang", 0);
        });
        mapper.answer("getProcessTaskStepRelByToId", args -> incoming);
        mapper.value("getProcessTaskStepByProcessTaskIdAndType", Collections.singletonList(step(999L, "succeed", 2)));
        mapper.value("checkIsProcessTaskStepUser", 1); mapper.value("checkIsWorker", 1);
        mapper.value("getProcessTaskLockById", 10L);
        mapper.answer("getProcessTaskStepListByIdList", args -> ((List<?>) args[0]).contains(101L)
                ? Collections.singletonList(stored) : Collections.emptyList());
        mapper.value("getProcessTaskStepListByProcessTaskIdList", Collections.singletonList(stored));
        mapper.value("getProcessTaskListByIdList", Collections.singletonList(new ProcessTaskVo(10L)));
        mapper.emptyLists("getProcessTaskConvergeListByProcessTaskId", "getProcessTaskStepWorkerListByProcessTaskIdList", "getProcessTaskStepUserListByProcessTaskIdList",
                "getProcessTaskStepAgentListByProcessTaskIdList", "getProcessTaskStepRelListByProcessTaskIdList",
                "getProcessTaskStepBaseInfoByProcessTaskId", "getProcessTaskStepRelByProcessTaskId",
                "getProcessTaskStepRelByFromId", "getProcessTaskStepUserByStepId", "getProcessTaskStepReapprovalRestoreBackupListByBackupStepId",
                "getProcessTaskStepWorkerByProcessTaskIdAndProcessTaskStepId", "getToProcessTaskStepIdListByFromIdAndType");
        mapper.writes("deleteProcessTaskConvergeByStepId", "deleteProcessTaskStepWorker", "deleteProcessTaskStepUser",
                "insertProcessTaskStepUser", "insertIgnoreProcessTaskStepWorker", "updateProcessTaskStepUserStatus",
                "deleteProcessTaskStepRemind", "updateProcessTaskStepRelIsHit", "updateProcessTaskStatus",
                "deleteProcessTaskStepReapprovalRestoreBackupByBackupStepId");
        mapper.value("getProcessTaskStepAgentByProcessTaskStepId", null);
        mapper.value("getInvokeByProcessTaskId", null); mapper.value("getProcessTaskTransferReportByToProcessTaskId", null);
        mapper.answer("updateProcessTaskStepStatus", args -> {
            events.add("state:" + ((ProcessTaskStepVo) args[0]).getStatus()); return 1;
        });
        util.answer("timeAudit", args -> {
            ProcessTaskStepVo value = (ProcessTaskStepVo) args[0];
            events.add("audit:" + ((IOperationType) args[1]).getValue());
            ProcessTaskStepVo snapshot = step(value.getId(), value.getStatus(), value.getIsActive());
            snapshot.setProcessTaskId(value.getProcessTaskId());
            auditArguments.add(value); auditSnapshots.add(snapshot);
            return null;
        });
        util.answer("calculateSla", args -> { events.add("calculate"); return null; });
        for (String name : Arrays.asList("notify", "action", "audit", "saveContentAndFile",
                "checkContentIsRequired", "saveTagList", "saveFocusUserList")) {
            util.value(name, null);
        }
        util.value("assignWorkerValid", true); util.value("baseInfoValidFromDb", true);
        util.value("saveForm", false); util.value("saveStepRemind", 1);
        configs.value("getProcessTaskStepConfigByHash", "{}");
        configs.emptyLists("getProcessTaskStepConfigListByHashList", "getProcessTaskConfigListByHashList");
        Map apis = registry(CrossoverServiceFactory.class, "apiMap");
        apis.put(IProcessTaskCrossoverMapper.class, mapper.proxy(IProcessTaskCrossoverMapper.class));
        apis.put(IProcessStepHandlerCrossoverUtil.class, util.proxy(IProcessStepHandlerCrossoverUtil.class));
        apis.put(ISelectContentByHashCrossoverMapper.class, configs.proxy(ISelectContentByHashCrossoverMapper.class));
        Stub score = new Stub(); score.value("deleteProcessTaskAutoScoreByProcessTaskId", 1);
        apis.put(IProcessTaskScoreCrossoverMapper.class, score.proxy(IProcessTaskScoreCrossoverMapper.class));
        Stub sla = new Stub(); sla.value("deleteProcessTaskStepSlaDelayByTargetProcessTaskStepId", 1);
        apis.put(IProcessTaskSlaCrossoverMapper.class, sla.proxy(IProcessTaskSlaCrossoverMapper.class));
        Stub audit = new Stub(); audit.value("getProcessTaskStepCostCountByProcessTaskStepIdAndStartOperateList", 0);
        apis.put(IProcessTaskStepTimeAuditCrossoverMapper.class, audit.proxy(IProcessTaskStepTimeAuditCrossoverMapper.class));
        Stub service = new Stub(); service.emptyLists("getProcessTaskFormAttributeDataListByProcessTaskId");
        // 后续步骤只登记异步任务，不能在当前事务里实际激活。
        service.answer("saveProcessTaskStepInOperation", args -> {
            ProcessTaskStepInOperationVo operation = (ProcessTaskStepInOperationVo) args[0];
            assertEquals(Long.valueOf(10), operation.getProcessTaskId());
            assertEquals(Long.valueOf(202), operation.getProcessTaskStepId());
            events.add("schedule"); return null;
        });
        apis.put(IProcessTaskCrossoverService.class, service.proxy(IProcessTaskCrossoverService.class));
        Stub users = new Stub();
        users.answer("getUserBaseInfoByUuidWithoutCache", args -> {
            UserVo user = new UserVo(); user.setUuid((String) args[0]); user.setUserName("测试处理人"); return user;
        });
        replaceField(ProcessStepHandlerBase.class, "userMapper", users.proxy(UserMapper.class));
    }

    /** 即使断言失败，也逆序恢复工厂、上下文和共享字段。 */
    @After public void cleanup() throws Exception {
        Exception failure = null;
        for (int i = resources.size() - 1; i >= 0; i--) {
            try { resources.get(i).close(); } catch (Exception e) { if (failure == null) failure = e; }
        }
        if (failure != null) throw failure;
    }

    /** 使用真实 DTO，审计调用时复制状态，避免后续修改掩盖错误入参。 */
    private ProcessTaskStepVo step(Long id, String status, Integer active) {
        ProcessTaskStepVo value = new ProcessTaskStepVo();
        value.setId(id); value.setProcessTaskId(10L);
        value.setStatus(status); value.setIsActive(active); value.setHandler("test-handler");
        return value;
    }

    /** 由真实 assign 根据业务钩子提供的自动开始标记决定最终状态。 */
    private void assignedStatus(String status) {
        handler.autoStart = "running".equals(status) ? 1 : 0;
    }

    /** 断言状态持久化、审计、重算的次序，以及唯一审计的类型与状态。 */
    private void assertAudit(IOperationType op, String status) {
        assertEquals(events.toString(), 1, auditSnapshots.size());
        assertEquals(status, auditSnapshots.get(0).getStatus());
        assertEquals(Long.valueOf(101), auditSnapshots.get(0).getId());
        assertEquals(Long.valueOf(10), auditSnapshots.get(0).getProcessTaskId());
        assertEquals("每次生命周期操作只安排一次重算", 1, Collections.frequency(events, "calculate"));
        int state = events.indexOf("state:" + status);
        int audit = events.indexOf("audit:" + op.getValue());
        int calculate = events.indexOf("calculate");
        assertTrue(events.toString(), state >= 0 && state < audit && audit < calculate);
        assertFalse(events.contains("audit:" + ProcessTaskStepOperationType.STEP_START.getValue()));
    }

    /** 自动开始的激活只发送一次审计，由审计服务一次写入两个时间。 */
    @Test public void activeRunningDoesNotDuplicateStart() throws Exception {
        assignedStatus("running");
        handler.active(current);
        assertAudit(ProcessTaskStepOperationType.STEP_ACTIVE, "running");
    }

    /** 待处理的激活也必须启动计时。 */
    @Test public void activePendingStartsAudit() throws Exception {
        assignedStatus("pending");
        handler.active(current);
        assertAudit(ProcessTaskStepOperationType.STEP_ACTIVE, "pending");
    }

    /** 上游撤回造成自动挂起，先保存HANG/0，再写结束审计并重算。 */
    @Test public void automaticHangClosesAuditAfterStateUpdate() {
        handler.hang(current, ProcessTaskStepOperationType.STEP_RETREAT);
        assertAudit(ProcessTaskStepOperationType.STEP_HANG, "hang");
        assertEquals(Integer.valueOf(0), auditSnapshots.get(0).getIsActive());
    }

    /** 已挂起的步骤应保持幂等，不重复写暂停记录。 */
    @Test public void alreadyHungDoesNotDuplicateAudit() {
        stored.setStatus("hang"); stored.setIsActive(0);
        handler.hang(current, ProcessTaskStepOperationType.STEP_RETREAT);
        assertTrue(events.isEmpty());
        assertTrue(util.calls.isEmpty());
    }

    /** 撤回后待处理必须发送STEP_RETREAT，不能遗漏等待时间。 */
    @Test public void retreatPendingStartsNewRound() throws Exception {
        assignedStatus("pending"); handler.retreat(current);
        assertAudit(ProcessTaskStepOperationType.STEP_RETREAT, "pending");
    }

    /** 撤回自动开始时，仍只发送一个包含最终状态的审计。 */
    @Test public void retreatRunningDoesNotDuplicateStart() throws Exception {
        assignedStatus("running"); handler.retreat(current);
        assertAudit(ProcessTaskStepOperationType.STEP_RETREAT, "running");
    }

    /** 重做后待处理必须发送STEP_REDO。 */
    @Test public void redoPendingStartsNewRound() throws Exception {
        assignedStatus("pending"); handler.redo(current);
        assertAudit(ProcessTaskStepOperationType.STEP_REDO, "pending");
    }

    /** 重做自动开始时不能额外创建第二条开始审计。 */
    @Test public void redoRunningDoesNotDuplicateStart() throws Exception {
        assignedStatus("running"); handler.redo(current);
        assertAudit(ProcessTaskStepOperationType.STEP_REDO, "running");
    }

    /** 配置回退返回点，使重新审批真实进入完成分支。 */
    private void enableReapproval() {
        ProcessTaskStepRelVo rel = new ProcessTaskStepRelVo();
        rel.setFromProcessTaskStepId(202L); rel.setToProcessTaskStepId(101L);
        rel.setType(ProcessFlowDirection.BACKWARD.getValue()); rel.setIsHit(1);
        incoming = Collections.singletonList(rel);
    }

    /** 成功重新审批后写完成终点，并在流转前安排重算。 */
    @Test public void reapprovalSuccessClosesAudit() {
        enableReapproval(); handler.reapproval(current);
        assertAudit(ProcessTaskStepOperationType.STEP_COMPLETE, "succeed");
        assertEquals(Integer.valueOf(2), auditSnapshots.get(0).getIsActive());
        assertTrue(events.toString(), events.indexOf("schedule") > events.indexOf("calculate"));
        assertEquals("只登记一个提交后回调，测试不提交事务", 1,
                TransactionSynchronizationManager.getSynchronizations().size());
    }

    /** 完成状态写入前业务失败，不允许finally伪造完成审计。 */
    @Test public void reapprovalFailureDoesNotWriteCompletion() throws Exception {
        enableReapproval();
        handler.failReapproval = true;
        handler.reapproval(current);
        assertEquals("failed", current.getStatus());
        assertTrue(auditSnapshots.isEmpty());
        assertFalse(util.calls.contains("timeAudit"));
        assertFalse(events.contains("state:succeed"));
        assertFalse(events.contains("schedule"));
        assertTrue(TransactionSynchronizationManager.getSynchronizations().isEmpty());
    }

    /** 转交对象与入参特意分离，捕获错误使用旧状态对象的回归。 */
    private void transfer(String before, String after) {
        current.setStatus(before); stored.setStatus(before); stored.setConfigHash("config");
        configs.value("getProcessTaskStepConfigByHash",
                "running".equals(after) ? "{\"autoStart\":1}" : "{\"autoStart\":0}");
        ProcessTaskStepWorkerVo worker = new ProcessTaskStepWorkerVo();
        worker.setType("user"); worker.setUuid("next-user");
        handler.transfer(current, Collections.singletonList(worker));
        assertAudit(ProcessTaskStepOperationType.STEP_TRANSFER, after);
        assertSame(stored, auditArguments.get(0));
        assertEquals("原始入参不能被测试桩同步修改", before, current.getStatus());
    }

    /** 转交自动开始时，审计必须收到running状态。 */
    @Test public void transferPendingToRunningUsesUpdatedObject() { transfer("pending", "running"); }

    /** 转交重新待处理时，审计必须收到pending状态。 */
    @Test public void transferRunningToPendingUsesUpdatedObject() { transfer("running", "pending"); }

    /** 上报起始节点只发送一次完成审计，由服务生成零时长闭合段。 */
    @Test public void startProcessWritesOnlyCompleteAudit() {
        handler.startProcess(current);
        assertAudit(ProcessTaskStepOperationType.STEP_COMPLETE, "succeed");
        assertFalse(events.contains("audit:" + ProcessTaskStepOperationType.STEP_ACTIVE.getValue()));
    }

    /** 测试节点仅提供处理人和业务扩展点，复用生产生命周期、分派与流转逻辑。 */
    private static class TestHandler extends ProcessStepHandlerBase {
        private int autoStart;
        private boolean failReapproval;
        @Override public String getHandler() { return "test-handler"; }
        @Override public ProcessStepMode getMode() { return ProcessStepMode.MT; }
        @Override public String getType() { return ProcessStepType.PROCESS.getValue(); }
        @Override public String getName() { return "测试节点"; }
        @Override public int getSort() { return 0; }
        @Override public boolean isAsync() { return false; }
        @Override public Boolean isAllowStart() { return true; }
        @Override public JSONObject getChartConfig() { return new JSONObject(); }
        /** 配置一个明确用户，避免进入异常分派；状态由基类 assign 设置。 */
        @Override protected int myAssign(ProcessTaskStepVo step, Set<ProcessTaskStepWorkerVo> workers) {
            workers.add(new ProcessTaskStepWorkerVo(step.getProcessTaskId(), step.getId(), "user", "test-user", "major"));
            return autoStart;
        }
        /** 故障发生在完成落库之前，用于保证异常路径不生成虚假完成审计。 */
        @Override protected int myReapproval(ProcessTaskStepVo step) throws ProcessTaskException {
            if (failReapproval) throw new ProcessTaskException("test failure");
            return 1;
        }
        /** 起始节点无后继路径；重新审批仍通过真实 doNext 注册异步回调。 */
        @Override protected Set<Long> myGetNext(ProcessTaskStepVo step, List<Long> ids, Long id) { return Collections.emptySet(); }
        /** 转交业务钩子不接入模块实现，状态更新仍由基类完成。 */
        @Override protected int myTransfer(ProcessTaskStepVo step, List<ProcessTaskStepWorkerVo> workers) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myActive(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myHang(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myHandle(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myStart(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myComplete(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myCompleteAudit(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myReapprovalAudit(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myRetreat(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myAbort(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myRecover(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myPause(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myBack(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int mySaveDraft(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myStartProcess(ProcessTaskStepVo step) { return 1; }
        /** 本场景不增加节点业务副作用，保留基类控制流程。 */
        @Override protected int myRedo(ProcessTaskStepVo step) { return 1; }
    }
}
