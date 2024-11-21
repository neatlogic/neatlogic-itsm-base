package neatlogic.framework.process.operationauth.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.process.constvalue.ProcessTaskOperationType;
import neatlogic.framework.process.crossover.*;
import neatlogic.framework.process.dto.*;
import neatlogic.framework.process.dto.agent.ProcessTaskAgentTargetVo;
import neatlogic.framework.process.dto.agent.ProcessTaskAgentVo;
import neatlogic.framework.process.exception.operationauth.ProcessTaskPermissionDeniedException;
import neatlogic.framework.process.exception.processtask.ProcessTaskNoPermissionException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @Time:2020年12月15日
 * @ClassName: ProcessOperateManager
 * @Description: 权限判断管理类，给步骤操作页面返回操作按钮列表，校验操作是否有权限
 */
public class ProcessAuthManager {
    private final static Logger logger = LoggerFactory.getLogger(ProcessAuthManager.class);

    /** 需要校验的工单id列表 **/
    private Set<Long> processTaskIdSet;
    /** 需要校验的步骤id列表 **/
    private Set<Long> processTaskStepIdSet;
    /** 工单id与步骤idList的键值对 **/
    private Map<Long, Set<Long>> processTaskStepIdSetMap;
    /** 需要校验的权限列表 **/
    private Set<IOperationType> operationTypeSet;
    /** 需要校验的某个工单或步骤的某个权限 **/
    private Map<Long, IOperationType> checkOperationTypeMap;
    /** 缓存作用，保存授权给当前用户处理服务工单的用户列表 **/
    private Map<String, List<String>> channelUuidFromUserUuidListMap = new HashMap<>();
    /** 缓存作用，保存当前用户授权列表 **/
    private Map<String, List<ProcessTaskAgentVo>> processTaskAgentListMap = new HashMap<>();
    /** 保存某个工单或步骤的某个权限检验时，导致失败的原因 **/
    private Map<Long, Map<IOperationType, ProcessTaskPermissionDeniedException>> operationTypePermissionDeniedExceptionMap = new HashMap<>();
    /** 保存额外参数 **/
    private Map<Long, JSONObject> extraParamMap = new HashMap<>();
    /** 需要校验的用户，如果不传，默认为当前用户 **/
    private String userUuid;
    public static class Builder {
        private Set<Long> processTaskIdSet = new HashSet<>();
        private Set<Long> processTaskStepIdSet = new HashSet<>();
        private Set<IOperationType> operationTypeSet = new HashSet<>();
        private String userUuid;
        public Builder() {}

        public Builder addProcessTaskId(Long... processTaskIds) {
            for (Long processTaskId : processTaskIds) {
                if (processTaskId != null) {
                    processTaskIdSet.add(processTaskId);
                }
            }
            return this;
        }

        public Builder addProcessTaskStepId(Long... processTaskStepIds) {
            for (Long processTaskStepId : processTaskStepIds) {
                if (processTaskStepId != null) {
                    processTaskStepIdSet.add(processTaskStepId);
                }
            }
            return this;
        }
        public Builder addProcessTaskStepId(List<Long> processTaskStepIds) {
            for (Long processTaskStepId : processTaskStepIds) {
                if (processTaskStepId != null) {
                    processTaskStepIdSet.add(processTaskStepId);
                }
            }
            return this;
        }

        public Builder addOperationType(IOperationType operationType) {
            operationTypeSet.add(operationType);
            return this;
        }

        public Builder withUserUuid(String _userUuid) {
            userUuid = _userUuid;
            return this;
        }

        public ProcessAuthManager build() {
            return new ProcessAuthManager(this);
        }
    }

    /**
     *
     * @Time:2020年12月15日
     * @ClassName: TaskOperationChecker
     * @Description: 校验工单级权限
     */
    public static class TaskOperationChecker {
        private Long processTaskId;
        private IOperationType operationType;
        private JSONObject extraParam;
        private String userUuid;

        public TaskOperationChecker(Long processTaskId, IOperationType operationType) {
            this.processTaskId = processTaskId;
            this.operationType = operationType;
        }

        public TaskOperationChecker addExtraParam(String key, Object data) {
            if (extraParam == null) {
                extraParam = new JSONObject();
            }
            extraParam.put(key, data);
            return this;
        }

        public TaskOperationChecker withUserUuid(String _userUuid) {
            userUuid = _userUuid;
            return this;
        }
        public ProcessAuthManager build() {
            return new ProcessAuthManager(this);
        }
    }

    /**
     *
     * @Time:2020年12月15日
     * @ClassName: StepOperationChecker
     * @Description: 校验步骤级权限
     */
    public static class StepOperationChecker {
        private Long processTaskStepId;
        private IOperationType operationType;
        private JSONObject extraParam;
        private String userUuid;

        public StepOperationChecker(Long processTaskStepId, IOperationType operationType) {
            this.processTaskStepId = processTaskStepId;
            this.operationType = operationType;
        }

        public StepOperationChecker addExtraParam(String key, Object data) {
            if (extraParam == null) {
                extraParam = new JSONObject();
            }
            extraParam.put(key, data);
            return this;
        }

        public StepOperationChecker withUserUuid(String _userUuid) {
            userUuid = _userUuid;
            return this;
        }
        public ProcessAuthManager build() {
            return new ProcessAuthManager(this);
        }
    }

    private ProcessAuthManager(Builder builder) {
        this.processTaskIdSet = builder.processTaskIdSet;
        this.processTaskStepIdSet = builder.processTaskStepIdSet;
        this.operationTypeSet = builder.operationTypeSet;
        this.userUuid = builder.userUuid;
    }

    private ProcessAuthManager(TaskOperationChecker checker) {
        this.processTaskIdSet = new HashSet<>();
        processTaskIdSet.add(checker.processTaskId);
        this.operationTypeSet = new HashSet<>();
        operationTypeSet.add(checker.operationType);
        this.checkOperationTypeMap = new HashMap<>();
        checkOperationTypeMap.put(checker.processTaskId, checker.operationType);
        extraParamMap.put(checker.processTaskId, checker.extraParam);
        this.userUuid = checker.userUuid;
    }

    private ProcessAuthManager(StepOperationChecker checker) {
        this.processTaskStepIdSet = new HashSet<>();
        processTaskStepIdSet.add(checker.processTaskStepId);
        this.operationTypeSet = new HashSet<>();
        operationTypeSet.add(checker.operationType);
        this.checkOperationTypeMap = new HashMap<>();
        checkOperationTypeMap.put(checker.processTaskStepId, checker.operationType);
        extraParamMap.put(checker.processTaskStepId, checker.extraParam);
        this.userUuid = checker.userUuid;
    }
    /**
     *
     * @Time:2020年12月21日
     * @Description: 返回多个工单及其步骤权限列表，返回值map中的key可能是工单id或步骤id，value就是其拥有的权限列表
     * @return Map<Long,Set<IOperationType>>
     */
    public Map<Long, Set<IOperationType>> getOperateMap() {
//        long startTime = System.currentTimeMillis();
        Map<Long, Set<IOperationType>> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(processTaskIdSet) && CollectionUtils.isEmpty(processTaskStepIdSet)) {
            return resultMap;
        }
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        if (processTaskStepIdSetMap == null) {
            processTaskStepIdSetMap = new HashMap<>();
        }
        if (CollectionUtils.isNotEmpty(processTaskStepIdSet)) {
            if (processTaskIdSet == null) {
                processTaskIdSet = new HashSet<>();
            }
            List<ProcessTaskStepVo> processTaskStepList =
                    processTaskCrossoverMapper.getProcessTaskStepListByIdList(new ArrayList<>(processTaskStepIdSet));
            for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
                processTaskIdSet.add(processTaskStepVo.getProcessTaskId());
                processTaskStepIdSetMap.computeIfAbsent(processTaskStepVo.getProcessTaskId(), k -> new HashSet<>())
                        .add(processTaskStepVo.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(processTaskIdSet)) {
            List<Long> processTaskIdList = new ArrayList<>(processTaskIdSet);
            List<ProcessTaskStepWorkerVo> processTaskStepWorkerList =
                    processTaskCrossoverMapper.getProcessTaskStepWorkerListByProcessTaskIdList(processTaskIdList);
            Map<Long, List<ProcessTaskStepWorkerVo>> processTaskStepWorkerListMap = new HashMap<>();
            for (ProcessTaskStepWorkerVo processTaskStepWorkerVo : processTaskStepWorkerList) {
                processTaskStepWorkerListMap
                        .computeIfAbsent(processTaskStepWorkerVo.getProcessTaskStepId(), k -> new ArrayList<>())
                        .add(processTaskStepWorkerVo);
            }
            List<ProcessTaskStepUserVo> processTaskStepUserList =
                    processTaskCrossoverMapper.getProcessTaskStepUserListByProcessTaskIdList(processTaskIdList);
            Map<Long, List<ProcessTaskStepUserVo>> processTaskStepUserListMap = new HashMap<>();
            for (ProcessTaskStepUserVo processTaskStepUserVo : processTaskStepUserList) {
                processTaskStepUserListMap
                        .computeIfAbsent(processTaskStepUserVo.getProcessTaskStepId(), k -> new ArrayList<>())
                        .add(processTaskStepUserVo);
            }
            List<ProcessTaskStepAgentVo> processTaskStepAgentList = processTaskCrossoverMapper.getProcessTaskStepAgentListByProcessTaskIdList(processTaskIdList);
            Map<Long, ProcessTaskStepAgentVo> processTaskStepAgentMap = processTaskStepAgentList.stream().collect(Collectors.toMap(e -> e.getProcessTaskStepId(), e -> e));
            List<ProcessTaskStepVo> processTaskStepList =
                    processTaskCrossoverMapper.getProcessTaskStepListByProcessTaskIdList(processTaskIdList);
            Map<Long, List<ProcessTaskStepVo>> processTaskStepListMap = new HashMap<>();
            for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
                processTaskStepVo.setWorkerList(
                        processTaskStepWorkerListMap.computeIfAbsent(processTaskStepVo.getId(), k -> new ArrayList<>()));
                processTaskStepVo.setUserList(
                        processTaskStepUserListMap.computeIfAbsent(processTaskStepVo.getId(), k -> new ArrayList<>()));
                ProcessTaskStepAgentVo processTaskStepAgentVo = processTaskStepAgentMap.get(processTaskStepVo.getId());
                if (processTaskStepAgentVo != null) {
                    processTaskStepVo.setProcessTaskStepAgentVo(processTaskStepAgentVo);
                }
                processTaskStepListMap.computeIfAbsent(processTaskStepVo.getProcessTaskId(), k -> new ArrayList<>())
                        .add(processTaskStepVo);
            }
            List<ProcessTaskStepRelVo> processTaskStepRelList =
                    processTaskCrossoverMapper.getProcessTaskStepRelListByProcessTaskIdList(processTaskIdList);
            Map<Long, List<ProcessTaskStepRelVo>> processTaskStepRelListMap = new HashMap<>();
            for (ProcessTaskStepRelVo processTaskStepRelVo : processTaskStepRelList) {
                processTaskStepRelListMap
                        .computeIfAbsent(processTaskStepRelVo.getProcessTaskId(), k -> new ArrayList<>())
                        .add(processTaskStepRelVo);
            }

            List<ProcessTaskVo> processTaskList = processTaskCrossoverMapper.getProcessTaskListByIdList(processTaskIdList);
            Set<String> hashSet = processTaskList.stream().map(ProcessTaskVo::getConfigHash).collect(Collectors.toSet());
//            long startTime3 = System.currentTimeMillis();
            ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
            List<ProcessTaskConfigVo> processTaskConfigList = selectContentByHashCrossoverMapper.getProcessTaskConfigListByHashList(new ArrayList<>(hashSet));
//            logger.error("D:" + (System.currentTimeMillis() - startTime3));
            Map<String, String> processTaskConfigMap = processTaskConfigList.stream().collect(Collectors.toMap(e->e.getHash(), e -> e.getConfig()));
//            logger.error("A:" + (System.currentTimeMillis() - startTime));
            if (StringUtils.isBlank(userUuid)) {
                userUuid = UserContext.get().getUserUuid(true);
            }
            for (ProcessTaskVo processTaskVo : processTaskList) {
                processTaskVo.setConfig(processTaskConfigMap.get(processTaskVo.getConfigHash()));
//                startTime = System.currentTimeMillis();
                processTaskVo.setStepList(processTaskStepListMap.computeIfAbsent(processTaskVo.getId(), k -> new ArrayList<>()));
                processTaskVo.setStepRelList(processTaskStepRelListMap.computeIfAbsent(processTaskVo.getId(), k -> new ArrayList<>()));
                resultMap.putAll(getOperateMap(processTaskVo, userUuid));
//                logger.error("B(" + processTaskVo.getId() + "):" + (System.currentTimeMillis() - startTime));
            }
        }
        return resultMap;
    }
    /**
     *
     * @Time:2020年12月21日
     * @Description: 返回一个工单及其步骤权限列表，返回值map中的key可能是工单id或步骤id，value就是其拥有的权限列表
     * @return Map<Long,Set<IOperationType>>
     */
    private Map<Long, Set<IOperationType>> getOperateMap(ProcessTaskVo processTaskVo, String userUuid) {
        Set<IOperationType> taskOperationTypeSet = new HashSet<>();
        Set<IOperationType> stepOperationTypeSet = new HashSet<>();
        List<IOperationType> taskOperationTypeList = OperationAuthHandlerType.TASK.getOperationTypeList();
        List<IOperationType> stepOperationTypeList = OperationAuthHandlerType.STEP.getOperationTypeList();
        if (CollectionUtils.isEmpty(operationTypeSet)) {
            taskOperationTypeSet.addAll(taskOperationTypeList);
            stepOperationTypeSet.addAll(stepOperationTypeList);
        } else {
            for (IOperationType operationType : operationTypeSet) {
                if (taskOperationTypeList.contains(operationType)) {
                    taskOperationTypeSet.add(operationType);
                } else if (stepOperationTypeList.contains(operationType)) {
                    stepOperationTypeSet.add(operationType);
                }
            }
        }
        Map<Long, Set<IOperationType>> resultMap = new HashMap<>();
//        String userUuid = UserContext.get().getUserUuid(true);
        JSONObject extraParam = extraParamMap.computeIfAbsent(processTaskVo.getId(), key -> new JSONObject());
        if (CollectionUtils.isNotEmpty(taskOperationTypeSet)) {
            IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(OperationAuthHandlerType.TASK.getValue());
            Set<IOperationType> resultSet = new HashSet<>();
            for (IOperationType operationType : taskOperationTypeSet) {
                boolean result = handler.getOperateMap(processTaskVo, userUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                if (result) {
                    resultSet.add(operationType);
                } else {
                    /** 因为上报权限不能授权，所以转报和复制上报权限不能授权 **/
                    if (ProcessTaskOperationType.PROCESSTASK_TRANSFERREPORT == operationType) {
                        continue;
                    }
                    if (ProcessTaskOperationType.PROCESSTASK_COPYPROCESSTASK == operationType) {
                        continue;
                    }
                    if (ProcessTaskOperationType.PROCESSTASK_START == operationType) {
                        continue;
                    }
                    /** 如果当前用户接受了其他用户的授权，查出其他用户拥有的权限，叠加当前用户权限里 **/
                    List<String> fromUuidList = getFromUuidListByChannelUuid(processTaskVo.getChannelUuid(), userUuid);
                    if (CollectionUtils.isNotEmpty(fromUuidList)) {
                        for (String fromUuid : fromUuidList) {
                            result = handler.getOperateMap(processTaskVo, fromUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                            if (result) {
                                resultSet.add(operationType);
                                break;
                            }
                        }
                    }
                }
            }
            resultMap.put(processTaskVo.getId(), resultSet);
        }
        if (CollectionUtils.isNotEmpty(stepOperationTypeSet)) {
            Set<Long> processTaskStepIdList = processTaskStepIdSetMap.get(processTaskVo.getId());
            if (CollectionUtils.isNotEmpty(processTaskStepIdList)) {
                IOperationAuthHandler stepHandler = OperationAuthHandlerFactory.getHandler(OperationAuthHandlerType.STEP.getValue());
                for (ProcessTaskStepVo processTaskStepVo : processTaskVo.getStepList()) {
                    if (processTaskStepIdList.contains(processTaskStepVo.getId())) {
                        extraParam = extraParamMap.computeIfAbsent(processTaskStepVo.getId(), key -> new JSONObject());
                        Set<IOperationType> resultSet = new HashSet<>();
                        for (IOperationType operationType : stepOperationTypeSet) {
                            Boolean result = null;
                            IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(processTaskStepVo.getHandler());
                            if (handler != null) {
                                result = handler.getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                            }
                            if(result == null || result) {
                                result = stepHandler.getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                                if (result == null) {
                                    result = false;
                                }
                            }
                            if (result) {
                                resultSet.add(operationType);
                            } else {
                                /** 如果当前用户接受了其他用户的授权，查出其他用户拥有的权限，叠加当前用户权限里 **/
                                List<String> fromUuidList = getFromUuidListByChannelUuid(processTaskVo.getChannelUuid(), userUuid);
                                if (CollectionUtils.isNotEmpty(fromUuidList)) {
                                    result = null;
                                    for (String fromUuid : fromUuidList) {
                                        if (handler != null) {
                                            result = handler.getOperateMap(processTaskVo, processTaskStepVo, fromUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                                        }
                                        if(result == null || result) {
                                            result = stepHandler.getOperateMap(processTaskVo, processTaskStepVo, fromUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                                            if (result == null) {
                                                result = false;
                                            }
                                        }
                                        if (result) {
                                            resultSet.add(operationType);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        resultMap.put(processTaskStepVo.getId(), resultSet);
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     *  获取授权给当前用户处理服务工单的用户列表
     * @param channelUuid
     * @return
     */
    private List<String> getFromUuidListByChannelUuid(String channelUuid, String userUuid) {
        List<String> fromUserUuidList = channelUuidFromUserUuidListMap.get(channelUuid);
        if (fromUserUuidList == null) {
            fromUserUuidList = new ArrayList<>();
            List<ProcessTaskAgentVo> processTaskAgentList = processTaskAgentListMap.get(userUuid);
            if (processTaskAgentList == null) {
                IProcessTaskAgentCrossoverMapper processTaskAgentCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskAgentCrossoverMapper.class);
                processTaskAgentList = processTaskAgentCrossoverMapper.getProcessTaskAgentDetailListByToUserUuid(userUuid);
                processTaskAgentListMap.put(userUuid, processTaskAgentList);
            }
            if (CollectionUtils.isNotEmpty(processTaskAgentList)) {
                for (ProcessTaskAgentVo processTaskAgentVo : processTaskAgentList) {
                    String fromUserUuid = processTaskAgentVo.getFromUserUuid();
                    if (fromUserUuidList.contains(fromUserUuid)) {
                        continue;
                    }
                    boolean flag = false;
                    List<String> catalogUuidList = new ArrayList<>();
                    List<ProcessTaskAgentTargetVo> processTaskAgentTargetList = processTaskAgentVo.getProcessTaskAgentTargetVos();
                    for (ProcessTaskAgentTargetVo processTaskAgentTargetVo : processTaskAgentTargetList) {
                        String type = processTaskAgentTargetVo.getType();
                        if ("channel".equals(type)) {
                            if (channelUuid.equals(processTaskAgentTargetVo.getTarget())) {
                                flag = true;
                                break;
                            }
                        } else if ("catalog".equals(type)) {
                            catalogUuidList.add(processTaskAgentTargetVo.getTarget());
                        }
                    }
                    if (!flag && CollectionUtils.isNotEmpty(catalogUuidList)) {
                        IChannelCrossoverMapper channelCrossoverMapper = CrossoverServiceFactory.getApi(IChannelCrossoverMapper.class);
                        ChannelVo channelVo = channelCrossoverMapper.getChannelByUuid(channelUuid);
                        ICatalogCrossoverMapper catalogCrossoverMapper = CrossoverServiceFactory.getApi(ICatalogCrossoverMapper.class);
                        CatalogVo catalogVo = catalogCrossoverMapper.getCatalogByUuid(channelVo.getParentUuid());
                        List<String> upwardUuidList = catalogCrossoverMapper.getUpwardUuidListByLftRht(catalogVo.getLft(), catalogVo.getRht());
                        flag = catalogUuidList.removeAll(upwardUuidList);
                    }
                    if (flag) {
                        fromUserUuidList.add(fromUserUuid);
                    }
                }
            }
            channelUuidFromUserUuidListMap.put(channelUuid, fromUserUuidList);
        }
        return fromUserUuidList;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 检查是否拥有某个权限 
    * @return boolean
     */
    public boolean check() {
        if (MapUtils.isNotEmpty(checkOperationTypeMap)) {
            Map<Long, Set<IOperationType>> resultMap = getOperateMap();
            for (Map.Entry<Long, IOperationType> entry : checkOperationTypeMap.entrySet()) {
                return resultMap.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).contains(entry.getValue());
            }
        }
        return false;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 检查是否拥有某个权限，如果有，则返回true，没有则抛异常  
    * @return boolean
     */
    public boolean checkAndNoPermissionThrowException() {
        if (!check()) {
            for (Map.Entry<Long, IOperationType> entry : checkOperationTypeMap.entrySet()) {
                Map<IOperationType, ProcessTaskPermissionDeniedException> map = operationTypePermissionDeniedExceptionMap.get(entry.getKey());
                if (MapUtils.isNotEmpty(map)) {
                    ProcessTaskPermissionDeniedException exception = map.get(entry.getValue());
                    if (exception != null) {
                        throw exception;
                    }
                }
                throw new ProcessTaskNoPermissionException(entry.getValue().getText());
            }
        }
        return true;
    }

    /**
     * 根据工单id或步骤id和操作权限类型获取没有权限的原因
     * @param id 工单id或步骤id
     * @param operationType 操作权限类型
     * @return
     */
    public ProcessTaskPermissionDeniedException getProcessTaskPermissionDeniedException(Long id, IOperationType operationType) {
        Map<IOperationType, ProcessTaskPermissionDeniedException> map = operationTypePermissionDeniedExceptionMap.get(id);
        if (MapUtils.isNotEmpty(map)) {
            return map.get(operationType);
        }
        return null;
    }
}
