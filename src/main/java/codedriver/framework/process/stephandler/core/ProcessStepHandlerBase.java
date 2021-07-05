/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.stephandler.core;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.asynchronization.threadpool.TransactionSynchronizationPool;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.dao.mapper.RoleMapper;
import codedriver.framework.dao.mapper.TeamMapper;
import codedriver.framework.dao.mapper.UserMapper;
import codedriver.framework.dto.UserVo;
import codedriver.framework.form.dao.mapper.FormMapper;
import codedriver.framework.form.dto.FormVersionVo;
import codedriver.framework.notify.dao.mapper.NotifyMapper;
import codedriver.framework.notify.dto.NotifyPolicyVo;
import codedriver.framework.process.constvalue.*;
import codedriver.framework.process.dao.mapper.*;
import codedriver.framework.process.dao.mapper.score.ProcessTaskScoreMapper;
import codedriver.framework.process.dto.*;
import codedriver.framework.process.dto.score.ProcessScoreTemplateVo;
import codedriver.framework.process.dto.score.ProcessTaskScoreVo;
import codedriver.framework.process.dto.score.ScoreTemplateDimensionVo;
import codedriver.framework.process.exception.core.ProcessTaskException;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.process.ProcessStepHandlerNotFoundException;
import codedriver.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import codedriver.framework.process.exception.processtask.ProcessTaskStepUnActivedException;
import codedriver.framework.process.exception.processtask.ProcessTaskStepUserIsExistsException;
import codedriver.framework.process.exception.processtaskserialnumberpolicy.ProcessTaskSerialNumberPolicyHandlerNotFoundException;
import codedriver.framework.process.exception.processtaskserialnumberpolicy.ProcessTaskSerialNumberPolicyNotFoundException;
import codedriver.framework.process.notify.constvalue.TaskNotifyTriggerType;
import codedriver.framework.process.notify.constvalue.TaskStepNotifyTriggerType;
import codedriver.framework.process.operationauth.core.ProcessAuthManager;
import codedriver.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;
import codedriver.framework.process.processtaskserialnumberpolicy.core.ProcessTaskSerialNumberPolicyHandlerFactory;
import codedriver.framework.process.workerpolicy.core.IWorkerPolicyHandler;
import codedriver.framework.process.workerpolicy.core.WorkerPolicyHandlerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.*;

public abstract class ProcessStepHandlerBase implements IProcessStepHandler {
    static Logger logger = LoggerFactory.getLogger(ProcessStepHandlerBase.class);
    protected static ProcessTaskMapper processTaskMapper;
    protected static UserMapper userMapper;
    protected static TeamMapper teamMapper;
    protected static RoleMapper roleMapper;
    protected static ProcessTaskScoreMapper processTaskScoreMapper;
    protected static FormMapper formMapper;
    protected static ProcessMapper processMapper;
    protected static ChannelMapper channelMapper;
    protected static NotifyMapper notifyMapper;
    protected static ProcessTaskSerialNumberMapper processTaskSerialNumberMapper;
    protected static SelectContentByHashMapper selectContentByHashMapper;
    protected static IProcessStepHandlerUtil IProcessStepHandlerUtil;

    @Resource
    public void setProcessMapper(ProcessMapper _processMapper) {
        processMapper = _processMapper;
    }

    @Resource
    public void setProcessTaskMapper(ProcessTaskMapper _processTaskMapper) {
        processTaskMapper = _processTaskMapper;
    }

    @Resource
    public void setFormMapper(FormMapper _formMapper) {
        formMapper = _formMapper;
    }

    @Resource
    public void setUserMapper(UserMapper _userMapper) {
        userMapper = _userMapper;
    }

    @Resource
    public void setChannelMapper(ChannelMapper _channelMapper) {
        channelMapper = _channelMapper;
    }

    @Resource
    public void setNotifyMapper(NotifyMapper _notifyMapper) {
        notifyMapper = _notifyMapper;
    }

    @Resource
    public void setTeamMapper(TeamMapper _teamMapper) {
        teamMapper = _teamMapper;
    }

    @Resource
    public void setRoleMapper(RoleMapper _roleMapper) {
        roleMapper = _roleMapper;
    }

    @Resource
    public void setSelectContentByHashMapper(SelectContentByHashMapper _selectContentByHashMapper) {
        selectContentByHashMapper = _selectContentByHashMapper;
    }

    @Resource
    public void setProcessTaskScoreMapper(ProcessTaskScoreMapper _processTaskScoreMapper) {
        processTaskScoreMapper = _processTaskScoreMapper;
    }

    @Resource
    public void setProcessTaskSerialNumberMapper(ProcessTaskSerialNumberMapper _processTaskSerialNumberMapper) {
        processTaskSerialNumberMapper = _processTaskSerialNumberMapper;
    }

    @Resource
    public void setProcessStepHandlerUtilService(IProcessStepHandlerUtil _I_processStepHandlerUtil) {
        IProcessStepHandlerUtil = _I_processStepHandlerUtil;
    }

    private int updateProcessTaskStatus(Long processTaskId) {
        List<ProcessTaskStepVo> processTaskStepList =
                processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(processTaskId);

        int runningCount = 0, succeedCount = 0, failedCount = 0, abortedCount = 0, draftCount = 0, hangCount = 0;
        for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
            if (ProcessTaskStatus.DRAFT.getValue().equals(processTaskStepVo.getStatus())
                    && processTaskStepVo.getIsActive().equals(1)) {
                draftCount += 1;
            } else if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.HANG.getValue())) {
                hangCount += 1;
            } else if (processTaskStepVo.getIsActive().equals(1)) {
                runningCount += 1;
            } else if (processTaskStepVo.getIsActive().equals(-1)) {
                abortedCount += 1;
            } else if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.SUCCEED.getValue())
                    && ProcessStepHandlerType.END.getHandler().equals(processTaskStepVo.getHandler())) {
                succeedCount += 1;
            } else if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.FAILED.getValue())) {
                failedCount += 1;
            }
        }

        ProcessTaskVo processTaskVo = new ProcessTaskVo();
        processTaskVo.setId(processTaskId);
        if (draftCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.DRAFT.getValue());
        } else if (runningCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
        } else if (abortedCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.ABORTED.getValue());
        } else if (failedCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.FAILED.getValue());
        } else if (succeedCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
        } else if (hangCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.HANG.getValue());
        } else {
            return 1;
        }
        processTaskMapper.updateProcessTaskStatus(processTaskVo);
        return 1;
    }

    @Override
    public final int active(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            /* 锁定当前流程 **/
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
            boolean canFire = false;
            /* 获取当前步骤的所有前置步骤 **/
            /* 获取当前步骤的所有前置连线 **/
            List<ProcessTaskStepRelVo> fromProcessTaskStepRelList =
                    processTaskMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());
            /* 场景一：当前步骤只有1个前置关系，并且该前置关系状态为已流转，则当前步骤允许激活 **/
            if (fromProcessTaskStepRelList.size() == 1) {
                ProcessTaskStepRelVo fromProcessTaskStepRelVo = fromProcessTaskStepRelList.get(0);
                if (fromProcessTaskStepRelVo.getIsHit().equals(1)) {
                    canFire = true;
                }
            }
            /* 场景二：当前步骤有大于1个前置关系，则需要使用多种规则来判断当前步骤是否具备激活条件。 **/
            else if (fromProcessTaskStepRelList.size() > 1) {
                /* 获取汇聚节点是当前节点的所有前置节点 **/
                List<ProcessTaskStepVo> convergeStepList =
                        processTaskMapper.getProcessTaskStepByConvergeId(currentProcessTaskStepVo.getId());
                boolean hasDoingStep = false;
                for (ProcessTaskStepVo convergeStepVo : convergeStepList) {
                    /* 任意前置节点处于处理中状态 **/
                    if (convergeStepVo.getIsActive().equals(1)) {
                        hasDoingStep = true;
                        break;
                    }
                }
                if (!hasDoingStep) {
                    canFire = true;
                }
                /* 场景三：没有前置节点，证明是开始节点 **/
            } else {
                canFire = true;
            }

            if (canFire) {
                /* 设置当前步骤状态为未开始 **/
                currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
                currentProcessTaskStepVo.setUpdateActiveTime(1);
                /* 遍历后续节点所有步骤，写入汇聚步骤数据 **/
                resetConvergeInfo(currentProcessTaskStepVo);

                /* 如果当前步骤是二次进入(后续路径已经走过)，则需要对所有后续流转过的步骤都进行挂起操作 **/
                /* 获取当前步骤状态 **/
                List<ProcessTaskStepRelVo> nextTaskStepRelList =
                        processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
                for (ProcessTaskStepRelVo nextTaskStepRelVo : nextTaskStepRelList) {
                    if (Objects.equals(nextTaskStepRelVo.getIsHit(), 1)) {
                        ProcessTaskStepVo nextProcessTaskStepVo = processTaskMapper
                                .getProcessTaskStepBaseInfoById(nextTaskStepRelVo.getToProcessTaskStepId());
                        if (nextProcessTaskStepVo != null && !Objects.equals(nextProcessTaskStepVo.getIsActive(), 0)) {
                            IProcessStepHandler handler =
                                    ProcessStepHandlerFactory.getHandler(nextProcessTaskStepVo.getHandler());
                            // 标记挂起操作来源步骤
                            nextProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                            // 标记挂起操作的发起步骤，避免出现死循环
                            nextProcessTaskStepVo.setStartProcessTaskStepId(currentProcessTaskStepVo.getId());
                            if (handler != null) {
                                doNext(ProcessTaskOperationType.STEP_HANG, new ProcessStepThread(nextProcessTaskStepVo) {
                                    @Override
                                    public void myExecute() {
                                        handler.hang(nextProcessTaskStepVo);
                                    }
                                });
                            }
                        }
                    }
                    // 恢复路径命中状态为0，代表路径未通过
                    processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(),
                            nextTaskStepRelVo.getToProcessTaskStepId(), 0);
                }

                if (this.getMode().equals(ProcessStepMode.MT)) {
                    /* 分配处理人 **/
                    assign(currentProcessTaskStepVo);

                    myActive(currentProcessTaskStepVo);
                    currentProcessTaskStepVo.setIsActive(1);
                    updateProcessTaskStepStatus(currentProcessTaskStepVo);

                    /* 写入时间审计 **/
                    IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_ACTIVE);
                    if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
                        IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_START);
                        IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.START);
                        IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.START);
                    }

                    /* 计算SLA并触发超时警告 **/
                    IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);

                    /* 触发通知 **/
                    IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ACTIVE);

                    /* 执行动作 **/
                    IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ACTIVE);
                } else if (this.getMode().equals(ProcessStepMode.AT)) {
                    myActive(currentProcessTaskStepVo);
                    currentProcessTaskStepVo.setIsActive(1);
                    updateProcessTaskStepStatus(currentProcessTaskStepVo);
                    /* 自动处理 **/
                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
                    doNext(ProcessTaskOperationType.STEP_HANDLE, new ProcessStepThread(currentProcessTaskStepVo) {
                        @Override
                        public void myExecute() {
                            handler.handle(currentProcessTaskStepVo);
                        }
                    });
                }
            }
        } catch (ProcessTaskException e) {
            logger.error(e.getMessage(), e);
            currentProcessTaskStepVo.setIsActive(0);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            currentProcessTaskStepVo.setError(e.getMessage());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            /* 异常提醒 **/
            IProcessStepHandlerUtil.saveStepRemind(currentProcessTaskStepVo,
                    currentProcessTaskStepVo.getStartProcessTaskStepId(), e.getMessage(), ProcessTaskStepRemindType.ERROR);
        } finally {
            if (ProcessTaskStatus.FAILED.getValue().equals(currentProcessTaskStepVo.getStatus())) {
                /*
                 * 发生异常不能激活当前步骤，执行当前步骤的回退操作
                 */
                IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
                doNext(ProcessTaskOperationType.STEP_BACK, new ProcessStepThread(currentProcessTaskStepVo) {
                    @Override
                    public void myExecute() {
                        handler.back(currentProcessTaskStepVo);
                    }
                });
            }
            // deleteProcessTaskStepInOperationByProcessTaskStepId(currentProcessTaskStepVo.getId(),
            // ProcessTaskOperationType.ACTIVE);
        }
        return 1;
    }

    protected abstract int myActive(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int assign(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException {
        /* 清空处理人 **/
        Set<ProcessTaskStepWorkerVo> workerSet = new HashSet<>();
        /* 如果已经存在过处理人，则继续使用旧处理人，否则启用分派 **/
        List<ProcessTaskStepUserVo> oldUserList = processTaskMapper
                .getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
        if (oldUserList.size() > 0) {
            ProcessTaskStepUserVo oldUserVo = oldUserList.get(0);
            workerSet.add(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(),
                    currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), oldUserVo.getUserVo().getUuid(),
                    ProcessUserType.MAJOR.getValue()));
            currentProcessTaskStepVo.setUpdateActiveTime(0);
        }
        currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
        int autoStart = myAssign(currentProcessTaskStepVo, workerSet);
        boolean isAssignException = false;
        if (CollectionUtils.isEmpty(workerSet)) {
            /* 获取步骤配置信息 **/
            ProcessTaskStepVo processTaskStepVo =
                    processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
            String stepConfig =
                    selectContentByHashMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
            String defaultWorker = (String) JSONPath.read(stepConfig, "workerPolicyConfig.defaultWorker");
            String[] split = defaultWorker.split("#");
            workerSet.add(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(),
                    currentProcessTaskStepVo.getId(), split[0], split[1], ProcessUserType.MAJOR.getValue()));
            isAssignException = true;
        }

        processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
        if (CollectionUtils.isNotEmpty(workerSet)) {
            for (ProcessTaskStepWorkerVo workerVo : workerSet) {
                processTaskMapper.insertProcessTaskStepWorker(workerVo);
            }
        } else {
            throw new ProcessTaskException("没有匹配到处理人");
        }

        ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
        processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
        processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());// 只删除主处理人人
        processTaskMapper.deleteProcessTaskStepUser(processTaskStepUserVo);

        /* 当只分配到一个用户时，自动设置为处理人，不需要抢单 **/
        if (workerSet.size() == 1) {
            for (ProcessTaskStepWorkerVo workerVo : workerSet) {
                if (StringUtils.isNotBlank(workerVo.getUuid())
                        && GroupSearch.USER.getValue().equals(workerVo.getType())) {
                    processTaskMapper.insertProcessTaskStepUser(new ProcessTaskStepUserVo(
                            currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(),
                            workerVo.getUuid(), ProcessUserType.MAJOR.getValue()));
                    /* 当步骤设置了自动开始时，设置当前步骤状态为处理中 **/
                    if (autoStart == 1) {
                        currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
                        currentProcessTaskStepVo.setUpdateStartTime(1);
                    }
                }
            }
        }
        /* 触发通知 **/
//        processStepHandlerUtilService.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ASSIGN);
        if (isAssignException) {
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ASSIGNEXCEPTION);
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ASSIGNEXCEPTION);
        }
        /* 执行动作 **/
//        processStepHandlerUtilService.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ASSIGN);
        return 1;
    }


    /**
     * 子类分配处理人
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @param workerSet                处理人列表
     * @return int 返回值为1时代表配置了自动开始处理，0时代表配置了不自动开始处理
     * @throws ProcessTaskException 异常
     */
    protected abstract int myAssign(ProcessTaskStepVo currentProcessTaskStepVo,
                                    Set<ProcessTaskStepWorkerVo> workerSet) throws ProcessTaskException;

    protected int defaultAssign(ProcessTaskStepVo currentProcessTaskStepVo, Set<ProcessTaskStepWorkerVo> workerSet)
            throws ProcessTaskException {
        /* 获取步骤配置信息 **/
        ProcessTaskStepVo processTaskStepVo =
                processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());

        String executeMode = (String) JSONPath.read(stepConfig, "workerPolicyConfig.executeMode");
        Integer autoStart = (Integer) JSONPath.read(stepConfig, "workerPolicyConfig.autoStart");

        /* 如果workerList.size()>0，说明已经存在过处理人，则继续使用旧处理人，否则启用分派 **/
        if (CollectionUtils.isEmpty(workerSet)) {
            /* 分配处理人 **/
            ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo = new ProcessTaskStepWorkerPolicyVo();
            processTaskStepWorkerPolicyVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            List<ProcessTaskStepWorkerPolicyVo> workerPolicyList =
                    processTaskMapper.getProcessTaskStepWorkerPolicy(processTaskStepWorkerPolicyVo);
            if (CollectionUtils.isNotEmpty(workerPolicyList)) {
                for (ProcessTaskStepWorkerPolicyVo workerPolicyVo : workerPolicyList) {
                    IWorkerPolicyHandler workerPolicyHandler =
                            WorkerPolicyHandlerFactory.getHandler(workerPolicyVo.getPolicy());
                    if (workerPolicyHandler != null) {
                        List<ProcessTaskStepWorkerVo> tmpWorkerList =
                                workerPolicyHandler.execute(workerPolicyVo, currentProcessTaskStepVo);
                        if (CollectionUtils.isNotEmpty(tmpWorkerList)) {
                            /* 删除不存在的用户、组、角色 **/
                            Iterator<ProcessTaskStepWorkerVo> iterator = tmpWorkerList.iterator();
                            while (iterator.hasNext()) {
                                ProcessTaskStepWorkerVo workerVo = iterator.next();
                                if (workerVo.getType().equals(GroupSearch.USER.getValue())) {
                                    UserVo userVo = userMapper.getUserBaseInfoByUuid(workerVo.getUuid());
                                    if (userVo == null || userVo.getIsActive() == 0) {
                                        iterator.remove();
                                    }
                                } else if (workerVo.getType().equals(GroupSearch.TEAM.getValue())) {
                                    if (teamMapper.checkTeamIsExists(workerVo.getUuid()) == 0) {
                                        iterator.remove();
                                    }
                                } else if (workerVo.getType().equals(GroupSearch.ROLE.getValue())) {
                                    if (roleMapper.checkRoleIsExists(workerVo.getUuid()) == 0) {
                                        iterator.remove();
                                    }
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(tmpWorkerList)) {
                            /* 顺序分配处理人 **/
                            if ("sort".equals(executeMode)) {
                                // 找到处理人，则退出
                                workerSet.addAll(tmpWorkerList);
                                break;
                            } else if ("batch".equals(executeMode)) {
                                workerSet.addAll(tmpWorkerList);
                            }
                        }
                    }
                }
            }
        }
        return autoStart != null ? autoStart : 0;
    }

    /**
     * hang操作原则上不允许出现任何异常，所有异常都必须解决以便流程可以顺利挂起，否则流程可能会卡死在某个节点不能前进或后退
     */
    @Override
    public final int hang(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

            myHang(currentProcessTaskStepVo);
            // 删除当前节点的汇聚记录
            processTaskMapper.deleteProcessTaskConvergeByStepId(currentProcessTaskStepVo.getId());

            // 获取流转过的路径
            List<ProcessTaskStepRelVo> toProcessTaskStepRelList =
                    processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());

            for (ProcessTaskStepRelVo processTaskStepRelVo : toProcessTaskStepRelList) {
                // 沿着流转过的路径向后找激活过的节点并挂起
                if (Objects.equals(processTaskStepRelVo.getIsHit(), 1)) {
                    ProcessTaskStepVo toProcessTaskStepVo =
                            processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepRelVo.getToProcessTaskStepId());
                    if (toProcessTaskStepVo != null && !Objects.equals(toProcessTaskStepVo.getIsActive(), 0)) {
                        // 如果下一个步骤不等于发起步骤，则继续挂起
                        if (!toProcessTaskStepVo.getId().equals(currentProcessTaskStepVo.getStartProcessTaskStepId())) {
                            IProcessStepHandler handler =
                                    ProcessStepHandlerFactory.getHandler(toProcessTaskStepVo.getHandler());
                            toProcessTaskStepVo
                                    .setStartProcessTaskStepId(currentProcessTaskStepVo.getStartProcessTaskStepId());
                            toProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                            if (handler != null) {
                                doNext(ProcessTaskOperationType.STEP_HANG, new ProcessStepThread(toProcessTaskStepVo) {
                                    @Override
                                    public void myExecute() {
                                        handler.hang(toProcessTaskStepVo);
                                    }
                                });
                            }
                        }
                    }
                }
                /* 重置路径状态 **/
                processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(),
                        processTaskStepRelVo.getToProcessTaskStepId(), 0);
            }

            currentProcessTaskStepVo.setIsActive(0);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.HANG.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            /* 触发通知 **/
//            processStepHandlerUtilService.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.HANG);

            /* 执行动作 **/
//            processStepHandlerUtilService.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.HANG);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            currentProcessTaskStepVo.setIsActive(2);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            currentProcessTaskStepVo.setError(ExceptionUtils.getStackTrace(e));
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        }
        // finally {
        // deleteProcessTaskStepInOperationByProcessTaskStepId(currentProcessTaskStepVo.getId(),
        // ProcessTaskOperationType.HANG);
        // }
        return 1;
    }

    /**
     * 由于hang属于自动操作，不抛出任何受检异常，所有异常都需要catch并记录日志方便修正缺陷
     **/
    protected abstract int myHang(ProcessTaskStepVo currentProcessTaskStepVo);

    @Override
    public final int handle(ProcessTaskStepVo currentProcessTaskStepVo) {
        // 锁定当前流程
        processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        // 修改当前步骤状态为运行中
        currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
        currentProcessTaskStepVo.setUpdateStartTime(1);
        currentProcessTaskStepVo.setUpdateEndTime(-1);
        updateProcessTaskStepStatus(currentProcessTaskStepVo);
        if (!this.isAsync()) {// 同步模式
            try {
                myHandle(currentProcessTaskStepVo);
                /* 如果步骤被标记为全部完成，则触发完成 **/
                if (currentProcessTaskStepVo.getIsAllDone()) {
                    /* 记录时间审计 **/
                    IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_COMPLETE);
                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
                    doNext(ProcessTaskOperationType.STEP_COMPLETE, new ProcessStepThread(currentProcessTaskStepVo) {
                        @Override
                        public void myExecute() {
                            handler.complete(currentProcessTaskStepVo);
                        }
                    });
                }
            } catch (ProcessTaskException ex) {
                logger.error(ex.getMessage(), ex);
                currentProcessTaskStepVo.setError(ex.getMessage());
                currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
                updateProcessTaskStepStatus(currentProcessTaskStepVo);
            } finally {/* 告诉超时探测器步骤已经完成 **/
                // timeoutDetector.setIsDone(true);
                // timeoutDetector.interrupt();

            }
        } else {// 异步模式
            String handlerType = this.getHandler();
            ProcessStepThread thread = new ProcessStepThread(currentProcessTaskStepVo) {
                @Override
                public void myExecute() {
                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(handlerType);
                    try {
                        // 这里不会有事务控制
                        myHandle(currentProcessTaskStepVo);
                        if (currentProcessTaskStepVo.getIsAllDone()) {
                            /* 记录时间审计 **/
                            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_COMPLETE);

                            doNext(ProcessTaskOperationType.STEP_COMPLETE, new ProcessStepThread(currentProcessTaskStepVo) {
                                @Override
                                public void myExecute() {
                                    handler.complete(currentProcessTaskStepVo);
                                }
                            });
                        }
                    } catch (ProcessTaskException ex) {
                        logger.error(ex.getMessage(), ex);
                        currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
                        currentProcessTaskStepVo.setError(ex.getMessage());
                        updateProcessTaskStepStatus(currentProcessTaskStepVo);
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
                        currentProcessTaskStepVo.setError(ExceptionUtils.getStackTrace(ex));
                        updateProcessTaskStepStatus(currentProcessTaskStepVo);
                    } finally {
                        /* 告诉超时探测器步骤已经完成 **/
                        // if (this.getTimeoutDetector() != null) {
                        // this.getTimeoutDetector().setIsDone(true);
                        // this.getTimeoutDetector().interrupt();
                        // }
                        // 是否有步骤结果
                        // if (flowJobStepVo.getHasResult()) {
                        // flowJobMapper.insertFlowJobStepResult(flowJobStepVo.getId(),
                        // flowJobStepVo.getResultPath());
                        // }
                    }
                }
            };
            /* 设置超时探测器到步骤处理线程 **/
            // thread.setTimeoutDetector(timeoutDetector);
            doNext(thread);
        }
        // finally {
        // deleteProcessTaskStepInOperationByProcessTaskStepId(currentProcessTaskStepVo.getId(),
        // ProcessTaskOperationType.HANDLE);
        // }
        return 1;
    }

    protected abstract int myHandle(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int start(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

            IProcessStepInternalHandler processStepUtilHandler =
                    ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            /* 检查处理人是否合法 **/
            new ProcessAuthManager.StepOperationChecker(currentProcessTaskStepVo.getId(),
                    ProcessTaskOperationType.STEP_START).build().checkAndNoPermissionThrowException();

            stepMajorUserRegulate(currentProcessTaskStepVo);
            ProcessTaskStepVo processTaskStepVo =
                    processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());

            /* 检查步骤是否“已激活” **/
            if (!processTaskStepVo.getIsActive().equals(1)) {
                throw new ProcessTaskStepUnActivedException();
            }
            /* 判断工单步骤状态是否 “未开始” **/
            if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
                throw new ProcessTaskRuntimeException("当前步骤已开始");
            }

            myStart(currentProcessTaskStepVo);

            /* 更新工单步骤状态为 “进行中” **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
            currentProcessTaskStepVo.setUpdateStartTime(1);
            currentProcessTaskStepVo.setUpdateEndTime(-1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(),
                    currentProcessTaskStepVo.getId());

            /* 写入时间审计 **/
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_START);

            /* 计算SLA **/
            IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.START);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.START);

            processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(),
                    ProcessTaskStepRemindType.TRANSFER.getValue()));
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            IProcessStepHandlerUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.START);
        }
        return 0;
    }

    private int updateProcessTaskStepStatus(ProcessTaskStepVo currentProcessTaskStepVo) {
        if (currentProcessTaskStepVo.getActiveTime() != null) {
            currentProcessTaskStepVo.setUpdateActiveTime(0);
        }
        if (currentProcessTaskStepVo.getStartTime() != null) {
            currentProcessTaskStepVo.setUpdateStartTime(0);
        }
        processTaskMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);
        updateProcessTaskStatus(currentProcessTaskStepVo.getProcessTaskId());
        return 1;
    }

    protected abstract int myStart(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int complete(ProcessTaskStepVo currentProcessTaskStepVo) {
        /* 锁定当前流程 **/
        processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        TaskStepNotifyTriggerType notifyTriggerType = TaskStepNotifyTriggerType.SUCCEED;
        ProcessTaskOperationType operationType = ProcessTaskOperationType.STEP_COMPLETE;
        boolean canComplete = false;
        JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
        String action = paramObj.getString("action");
        if (this.getMode().equals(ProcessStepMode.MT)) {
            if (ProcessTaskOperationType.STEP_BACK.getValue().equals(action)) {
                operationType = ProcessTaskOperationType.STEP_BACK;
                notifyTriggerType = TaskStepNotifyTriggerType.BACK;
            }
            canComplete = new ProcessAuthManager.StepOperationChecker(currentProcessTaskStepVo.getId(), operationType).build().checkAndNoPermissionThrowException();
            stepMajorUserRegulate(currentProcessTaskStepVo);
        } else if (this.getMode().equals(ProcessStepMode.AT)) {
            canComplete = true;
        }

        if (canComplete) {
            try {
                if (this.getHandler().equals(ProcessStepHandlerType.END.getHandler())) {
                    myComplete(currentProcessTaskStepVo);
                    /* 更新步骤状态 **/
                    currentProcessTaskStepVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
                    currentProcessTaskStepVo.setIsActive(2);
                    currentProcessTaskStepVo.setUpdateEndTime(1);
                    currentProcessTaskStepVo.setError(null);
                    updateProcessTaskStepStatus(currentProcessTaskStepVo);
                    IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskNotifyTriggerType.COMPLETEPROCESSTASK);
                    IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskNotifyTriggerType.COMPLETEPROCESSTASK);
                    myCompleteAudit(currentProcessTaskStepVo);
                    return 1;
                }

                if (this.getMode().equals(ProcessStepMode.MT)) {
                    if (operationType == ProcessTaskOperationType.STEP_COMPLETE) {
                        String priorityUuid = paramObj.getString("priorityUuid");
                        if (StringUtils.isNotBlank(priorityUuid)) {
                            processTaskMapper.updateProcessTaskPriorityUuidById(currentProcessTaskStepVo.getProcessTaskId(), priorityUuid);
                        }
                        IProcessStepHandlerUtil.assignWorkerValid(currentProcessTaskStepVo);
                        /* 保存表单属性值 **/
                        IProcessStepHandlerUtil.saveForm(currentProcessTaskStepVo);
                    }

                    /* 更新处理人状态 **/
                    ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid());// 兼容automatic作业无用户
                    processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
                    processTaskMajorUser.setUserType(ProcessUserType.MAJOR.getValue());
                    processTaskMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
                    /* 清空worker表 **/
                    processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
                }
                /* 保存描述内容 **/
                IProcessStepHandlerUtil.saveContentAndFile(currentProcessTaskStepVo, operationType);
                myComplete(currentProcessTaskStepVo);

                /* 更新步骤状态 **/
                currentProcessTaskStepVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
                currentProcessTaskStepVo.setIsActive(2);
                currentProcessTaskStepVo.setUpdateEndTime(1);
                currentProcessTaskStepVo.setError(null);
                updateProcessTaskStepStatus(currentProcessTaskStepVo);

                /* 流转到下一步 **/
                Set<ProcessTaskStepVo> nextStepList = getNext(currentProcessTaskStepVo);
                if (CollectionUtils.isEmpty(nextStepList)) {
                    throw new ProcessTaskException("找不到可流转路径");
                }
                Long startProcessTaskStepId = currentProcessTaskStepVo.getStartProcessTaskStepId();
                if (startProcessTaskStepId == null) {
                    startProcessTaskStepId = currentProcessTaskStepVo.getId();
                }
                for (ProcessTaskStepVo nextStep : nextStepList) {
                    IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
                    nextStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                    nextStep.setStartProcessTaskStepId(startProcessTaskStepId);

                    if (nextStepHandler != null) {
                        doNext(ProcessTaskOperationType.STEP_ACTIVE, new ProcessStepThread(nextStep) {
                            @Override
                            public void myExecute() {
                                nextStepHandler.active(nextStep);
                            }
                        });
                    }
                }

                /* 触发通知 **/
                IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, notifyTriggerType);
                /* 执行动作 **/
                IProcessStepHandlerUtil.action(currentProcessTaskStepVo, notifyTriggerType);

                /* 回退提醒 **/
                processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId()));
                if (this.getMode().equals(ProcessStepMode.MT)) {
                    if (ProcessTaskOperationType.STEP_BACK.getValue().equals(action)) {
                        processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId()));
                        IProcessStepHandlerUtil.saveStepRemind(currentProcessTaskStepVo, paramObj.getLong("nextStepId"), paramObj.getString("content"), ProcessTaskStepRemindType.BACK);
                    }
                }
            } catch (ProcessTaskException ex) {
                logger.error(ex.getMessage(), ex);
                currentProcessTaskStepVo.setError(ex.getMessage());
                currentProcessTaskStepVo.setIsActive(0);
                currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
                updateProcessTaskStepStatus(currentProcessTaskStepVo);
                /* 异常提醒 **/
                IProcessStepHandlerUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getStartProcessTaskStepId(), ex.getMessage(), ProcessTaskStepRemindType.ERROR);
                /* 触发通知 **/
                IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.FAILED);
                /* 执行动作 **/
                IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.FAILED);
            } finally {
                if (ProcessTaskStatus.FAILED.getValue().equals(currentProcessTaskStepVo.getStatus())) {
                    /* 发生异常不能完成当前步骤，执行当前步骤的回退操作 **/
                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
                    doNext(ProcessTaskOperationType.STEP_BACK, new ProcessStepThread(currentProcessTaskStepVo) {
                        @Override
                        public void myExecute() {
                            handler.back(currentProcessTaskStepVo);
                        }
                    });
                }

                myCompleteAudit(currentProcessTaskStepVo);
                if (this.getMode().equals(ProcessStepMode.MT)) {
                    /* 写入时间审计 **/
                    IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, operationType);
                    /* 计算SLA **/
                    IProcessStepHandlerUtil.calculateSla(new ProcessTaskVo(currentProcessTaskStepVo.getProcessTaskId()));
                }
            }
        }

        return 1;
    }

    protected abstract int myComplete(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    protected abstract int myCompleteAudit(ProcessTaskStepVo currentProcessTaskStepVo);

    @Override
    public final int retreat(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
            IProcessStepInternalHandler processStepUtilHandler =
                    ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }

            new ProcessAuthManager.StepOperationChecker(currentProcessTaskStepVo.getId(),
                    ProcessTaskOperationType.STEP_RETREAT).build().checkAndNoPermissionThrowException();

            stepMajorUserRegulate(currentProcessTaskStepVo);
            /* 设置当前步骤状态为未开始 **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
            /* 保存撤回原因 **/
            IProcessStepHandlerUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_RETREAT);
            myRetreat(currentProcessTaskStepVo);

            /* 遍历后续节点所有步骤，写入汇聚步骤数据 **/
            resetConvergeInfo(currentProcessTaskStepVo);

            /* 如果当前步骤是二次进入(后续路径已经走过)，则需要对所有后续流转过的步骤都进行挂起操作 **/
            /* 获取当前步骤状态 **/
            List<ProcessTaskStepRelVo> nextTaskStepRelList =
                    processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
            for (ProcessTaskStepRelVo nextTaskStepRelVo : nextTaskStepRelList) {
                if (nextTaskStepRelVo != null && nextTaskStepRelVo.getIsHit().equals(1)) {
                    ProcessTaskStepVo nextProcessTaskStepVo =
                            processTaskMapper.getProcessTaskStepBaseInfoById(nextTaskStepRelVo.getToProcessTaskStepId());
                    if (nextProcessTaskStepVo != null) {
                        IProcessStepHandler handler =
                                ProcessStepHandlerFactory.getHandler(nextProcessTaskStepVo.getHandler());
                        // 标记挂起操作来源步骤
                        nextProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                        // 标记挂起操作的发起步骤，避免出现死循环
                        nextProcessTaskStepVo.setStartProcessTaskStepId(currentProcessTaskStepVo.getId());
                        if (handler != null) {
                            doNext(ProcessTaskOperationType.STEP_HANG, new ProcessStepThread(nextProcessTaskStepVo) {
                                @Override
                                public void myExecute() {
                                    handler.hang(nextProcessTaskStepVo);
                                }
                            });
                        }
                    }
                }
                // 恢复路径命中状态为0，代表路径未通过
                processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(),
                        nextTaskStepRelVo.getToProcessTaskStepId(), 0);
            }

            /* 分配处理人 **/
            assign(currentProcessTaskStepVo);
            currentProcessTaskStepVo.setIsActive(1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);

            /* 写入时间审计 **/
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_RETREAT);
            if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
                IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_START);
            }

            /* 计算SLA并触发超时警告 **/
            IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.RETREAT);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.RETREAT);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            IProcessStepHandlerUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.RETREAT);
        }
        return 1;
    }

    protected abstract int myRetreat(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int abortProcessTask(ProcessTaskVo currentProcessTaskVo) {
        // 锁定当前流程
        processTaskMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
        /* 校验权限 **/
        new ProcessAuthManager.TaskOperationChecker(currentProcessTaskVo.getId(),
                ProcessTaskOperationType.TASK_ABORT).build().checkAndNoPermissionThrowException();

        List<ProcessTaskStepVo> processTaskStepList =
                processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(currentProcessTaskVo.getId());
        for (ProcessTaskStepVo stepVo : processTaskStepList) {
            /* 找到所有已激活步骤，执行终止操作 **/
            if (stepVo.getIsActive().equals(1)) {
                IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(stepVo.getHandler());
                if (handler != null) {
                    handler.abort(stepVo);
                } else {
                    throw new ProcessStepHandlerNotFoundException(stepVo.getHandler());
                }
            }
        }

        /* 更新流程作业状态 **/
        updateProcessTaskStatus(currentProcessTaskVo.getId());

        /* 处理历史记录 **/
        ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
        processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
        IProcessStepHandlerUtil.audit(processTaskStepVo, ProcessTaskAuditType.ABORTPROCESSTASK);
        /* 触发通知 **/
        IProcessStepHandlerUtil.notify(processTaskStepVo, TaskNotifyTriggerType.ABORTPROCESSTASK);
        /* 执行动作 **/
        IProcessStepHandlerUtil.action(processTaskStepVo, TaskNotifyTriggerType.ABORTPROCESSTASK);
        return 1;
    }

    @Override
    public int abort(ProcessTaskStepVo currentProcessTaskStepVo) {
        /* 组件完成动作 **/
        myAbort(currentProcessTaskStepVo);

        /* 清空worker表，确保没人可以处理当前步骤 **/
        processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));

        /* 修改步骤状态 **/
        currentProcessTaskStepVo.setIsActive(-1);
        processTaskMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);

        /* 写入时间审计 **/
        IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.TASK_ABORT);

        /* 计算SLA **/
        IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);

        /* 触发通知 **/
//        processStepHandlerUtilService.notify(currentProcessTaskStepVo, TaskNotifyTriggerType.ABORTPROCESSTASK);

        /* 执行动作 **/
//        processStepHandlerUtilService.action(currentProcessTaskStepVo, TaskNotifyTriggerType.ABORTPROCESSTASK);
        return 1;
    }

    /**
     * 由于abort属于批量自动操作，不抛出任何受检异常
     **/
    protected abstract int myAbort(ProcessTaskStepVo currentProcessTaskStepVo);

    @Override
    public final int recoverProcessTask(ProcessTaskVo currentProcessTaskVo) {
        // 锁定当前流程
        processTaskMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
        /* 校验权限 **/
        new ProcessAuthManager.TaskOperationChecker(currentProcessTaskVo.getId(),
                ProcessTaskOperationType.TASK_RECOVER).build().checkAndNoPermissionThrowException();

        List<ProcessTaskStepVo> processTaskStepList =
                processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(currentProcessTaskVo.getId());
        for (ProcessTaskStepVo stepVo : processTaskStepList) {
            /* 找到所有已终止步骤，执行终止操作 **/
            if (stepVo.getIsActive().equals(-1)) {
                IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(stepVo.getHandler());
                if (handler != null) {
                    handler.recover(stepVo);
                }
            }
        }

        /* 更新流程作业状态 **/
        updateProcessTaskStatus(currentProcessTaskVo.getId());

        /* 处理历史记录 **/
        ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
        processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
        IProcessStepHandlerUtil.audit(processTaskStepVo, ProcessTaskAuditType.RECOVERPROCESSTASK);
        /* 触发通知 **/
        IProcessStepHandlerUtil.notify(processTaskStepVo, TaskNotifyTriggerType.RECOVERPROCESSTASK);
        /* 执行动作 **/
        IProcessStepHandlerUtil.action(processTaskStepVo, TaskNotifyTriggerType.RECOVERPROCESSTASK);
        return 1;
    }

    @Override
    public int recover(ProcessTaskStepVo currentProcessTaskStepVo) {
        // 锁定当前流程
        processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        // 获取步骤基本信息
        currentProcessTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
        if (currentProcessTaskStepVo.getIsActive().equals(-1)) {
            /* 组件完成动作 **/
            myRecover(currentProcessTaskStepVo);
            currentProcessTaskStepVo.setIsActive(1);
            /* 如果已经存在过处理人，则继续使用旧处理人，否则重新分派 **/
            Set<ProcessTaskStepWorkerVo> workerSet = new HashSet<>();
            List<ProcessTaskStepUserVo> oldUserList = processTaskMapper
                    .getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
            if (oldUserList.size() > 0) {
                ProcessTaskStepUserVo oldUserVo = oldUserList.get(0);
                workerSet.add(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(),
                        currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), oldUserVo.getUserVo().getUuid(),
                        ProcessUserType.MAJOR.getValue()));
            } else {
                try {
                    myAssign(currentProcessTaskStepVo, workerSet);
                } catch (ProcessTaskException e) {
                    logger.error(e.getMessage(), e);
                    currentProcessTaskStepVo.setIsActive(-1);
                    currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
                    currentProcessTaskStepVo.setError(e.getMessage());
                }
            }
            for (ProcessTaskStepWorkerVo worker : workerSet) {
                processTaskMapper.insertProcessTaskStepWorker(worker);
            }
            IProcessStepInternalHandler processStepUtilHandler =
                    ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler != null) {
                processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(),
                        currentProcessTaskStepVo.getId());
            }

            /* 修改步骤状态 **/
            processTaskMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } else if (currentProcessTaskStepVo.getIsActive().equals(1)
                && ProcessTaskStatus.HANG.getValue().equals(currentProcessTaskStepVo.getStatus())) {
            IProcessStepInternalHandler processStepUtilHandler =
                    ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            /* 检查处理人是否合法 **/
            new ProcessAuthManager.StepOperationChecker(currentProcessTaskStepVo.getId(),
                    ProcessTaskOperationType.STEP_RECOVER).build().checkAndNoPermissionThrowException();

            stepMajorUserRegulate(currentProcessTaskStepVo);
            myRecover(currentProcessTaskStepVo);

            /* 更新工单步骤状态为 “进行中” **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);

            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskNotifyTriggerType.RECOVERPROCESSTASK);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskNotifyTriggerType.RECOVERPROCESSTASK);
        }

        /* 写入时间审计 **/
        IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.TASK_RECOVER);

        /* 计算SLA **/
        IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);
        return 1;
    }

    /**
     * 由于recover属于批量自动操作，不抛出任何受检异常
     **/
    protected abstract int myRecover(ProcessTaskStepVo currentProcessTaskStepVo);

    @Override
    public int pause(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

            IProcessStepInternalHandler processStepUtilHandler =
                    ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            /* 检查处理人是否合法 **/
            new ProcessAuthManager.StepOperationChecker(currentProcessTaskStepVo.getId(),
                    ProcessTaskOperationType.STEP_PAUSE).build().checkAndNoPermissionThrowException();

            stepMajorUserRegulate(currentProcessTaskStepVo);
            myPause(currentProcessTaskStepVo);

            /* 更新工单步骤状态为 “进行中” **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.HANG.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            // processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(),
            // currentProcessTaskStepVo.getId());

            /* 写入时间审计 **/
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_PAUSE);

            /* 计算SLA **/
            IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.PAUSE);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.PAUSE);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            IProcessStepHandlerUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.PAUSE);
        }
        return 1;
    }

    protected abstract int myPause(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int accept(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
            /* 校验权限 **/
            IProcessStepInternalHandler processStepUtilHandler =
                    ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }

            new ProcessAuthManager.StepOperationChecker(currentProcessTaskStepVo.getId(),
                    ProcessTaskOperationType.STEP_ACCEPT).build().checkAndNoPermissionThrowException();

            stepMajorUserRegulate(currentProcessTaskStepVo);
            /* 清空worker表，只留下当前处理人 **/
            processTaskMapper
                    .deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
            processTaskMapper.insertProcessTaskStepWorker(new ProcessTaskStepWorkerVo(
                    currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(),
                    GroupSearch.USER.getValue(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue()));

            /* 删除user表主处理人，更换为当前处理人 **/
            ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
            processTaskStepUserVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskStepUserVo.setUserVo(new UserVo(UserContext.get().getUserUuid(true)));
            processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
            processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());
            processTaskMapper.insertProcessTaskStepUser(processTaskStepUserVo);

            /* 处理历史记录 **/
            // processStepHandlerUtilService.audit(currentProcessTaskStepVo, ProcessTaskStepAction.ACCEPT);

            /* 触发通知 **/
            // processStepHandlerUtilService.notify(currentProcessTaskStepVo, NotifyTriggerType.ACCEPT);
            processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(),
                    ProcessTaskStepRemindType.TRANSFER.getValue()));
        } catch (ProcessTaskRuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        }
        return 0;
    }

    @Override
    public final int transfer(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList) {

        if (CollectionUtils.isEmpty(workerList)) {
            throw new ProcessTaskRuntimeException("请选择需要转交的处理人、处理组或角色");
        }
        // 锁定当前流程
        processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

        /* 校验权限 **/
        IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
        if (processStepUtilHandler == null) {
            throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
        }

        new ProcessAuthManager.StepOperationChecker(currentProcessTaskStepVo.getId(),
                ProcessTaskOperationType.STEP_TRANSFER).build().checkAndNoPermissionThrowException();

        ProcessTaskStepVo processTaskStepVo =
                processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
        processTaskStepVo.setParamObj(currentProcessTaskStepVo.getParamObj());
        /* 检查步骤是否 “已激活” **/
        if (!processTaskStepVo.getIsActive().equals(1)) {
            throw new ProcessTaskStepUnActivedException();
        }

        try {
            List<ProcessTaskStepUserVo> oldUserList = processTaskMapper
                    .getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
            if (CollectionUtils.isNotEmpty(oldUserList)) {
                List<String> workerUserUuidList = new ArrayList<>();
                for (ProcessTaskStepWorkerVo workerVo : workerList) {
                    if (GroupSearch.USER.getValue().equals(workerVo.getType())
                            && StringUtils.isNotBlank(workerVo.getUuid())) {
                        workerUserUuidList.add(workerVo.getUuid());
                    }
                }
                for (ProcessTaskStepUserVo oldUser : oldUserList) {
                    if (workerUserUuidList.contains(oldUser.getUserVo().getUuid())) {
                        throw new ProcessTaskStepUserIsExistsException(oldUser.getUserVo().getUserName());
                    }
                }
                /* 清空user表 **/
                ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
                processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
                processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
                processTaskMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
            }

            /* 保存描述内容 **/
            IProcessStepHandlerUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.TASK_TRANSFER);

            /* 根据子类需要把最终处理人放进来，引擎将自动写入数据库，也可能为空，例如一些特殊的流程节点 **/
            processTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
            myTransfer(processTaskStepVo, workerList);

            ProcessTaskStepWorkerVo processTaskStepWorkerVo =
                    new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId());
            /* 当只分配到一个用户时，自动设置为处理人，不需要抢单 **/
            if (workerList.size() == 1) {
                if (StringUtils.isNotBlank(workerList.get(0).getUuid())
                        && GroupSearch.USER.getValue().equals(workerList.get(0).getType())) {
                    processTaskMapper.insertProcessTaskStepUser(new ProcessTaskStepUserVo(
                            currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(),
                            workerList.get(0).getUuid(), ProcessUserType.MAJOR.getValue()));
                    /* 当步骤设置了自动开始时，设置当前步骤状态为处理中 **/
                    int autoStart = myAssign(processTaskStepVo, new HashSet<>(workerList));
                    if (autoStart == 1) {
                        processTaskStepWorkerVo.setUserType(ProcessUserType.MAJOR.getValue());
                        processTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
                    }
                }
            }
            /* 清空work表，重新写入新数据 **/
            processTaskMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
            for (ProcessTaskStepWorkerVo workerVo : workerList) {
                processTaskMapper.insertProcessTaskStepWorker(workerVo);
            }

            updateProcessTaskStepStatus(processTaskStepVo);

            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.TRANSFER);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.TRANSFER);

            /* 处理时间审计 **/
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.TASK_TRANSFER);
            /* 转交提醒 **/

            processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(),
                    ProcessTaskStepRemindType.TRANSFER.getValue()));
            IProcessStepHandlerUtil.saveStepRemind(currentProcessTaskStepVo,
                    currentProcessTaskStepVo.getId(), currentProcessTaskStepVo.getParamObj().getString("content"),
                    ProcessTaskStepRemindType.TRANSFER);
        } catch (ProcessTaskException e) {
            logger.error(e.getMessage(), e);
            processTaskStepVo.setError(e.getMessage());
            processTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(processTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            IProcessStepHandlerUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.TRANSFER);
        }

        return 0;
    }

    protected abstract int myTransfer(ProcessTaskStepVo currentProcessTaskStepVo,
                                      List<ProcessTaskStepWorkerVo> workerList) throws ProcessTaskException;

    /**
     * back操作不允许出现任何异常，所有异常都必须解决以便流程可以顺利回退，否则流程可能会卡死在某个节点不能前进或后退
     */
    @Override
    public final int back(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

            myBack(currentProcessTaskStepVo);

            // 获取来源路径
            List<ProcessTaskStepRelVo> fromProcessTaskStepRelList =
                    processTaskMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());

            for (ProcessTaskStepRelVo processTaskStepRelVo : fromProcessTaskStepRelList) {
                // 沿着流转过的路径向前找节点并重新激活
                if (!processTaskStepRelVo.getIsHit().equals(0)) {
                    ProcessTaskStepVo fromProcessTaskStepVo = processTaskMapper
                            .getProcessTaskStepBaseInfoById(processTaskStepRelVo.getFromProcessTaskStepId());
                    if (fromProcessTaskStepVo != null) {
                        // 如果是分流节点或条件节点，则再次调用back查找上一个处理节点
                        if (fromProcessTaskStepVo.getHandler().equals(ProcessStepHandlerType.DISTRIBUTARY.getHandler())
                                || fromProcessTaskStepVo.getHandler()
                                .equals(ProcessStepHandlerType.CONDITION.getHandler())) {
                            IProcessStepHandler handler =
                                    ProcessStepHandlerFactory.getHandler(fromProcessTaskStepVo.getHandler());
                            if (handler != null) {
                                fromProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                                doNext(ProcessTaskOperationType.STEP_BACK, new ProcessStepThread(fromProcessTaskStepVo) {
                                    @Override
                                    public void myExecute() {
                                        handler.back(fromProcessTaskStepVo);
                                    }
                                });
                            }
                        } else {
                            // 如果是处理节点，则重新激活
                            IProcessStepHandler handler =
                                    ProcessStepHandlerFactory.getHandler(fromProcessTaskStepVo.getHandler());
                            if (handler != null) {
                                fromProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                                doNext(ProcessTaskOperationType.STEP_ACTIVE, new ProcessStepThread(fromProcessTaskStepVo) {
                                    @Override
                                    public void myExecute() {
                                        handler.active(fromProcessTaskStepVo);
                                    }
                                });
                            }
                        }
                    }
                }
            }
            /* 处理时间审计 **/
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_BACK);

            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.BACK);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.BACK);
        } catch (ProcessTaskException e) {
            logger.error(e.getMessage(), e);
            currentProcessTaskStepVo.setError(e.getMessage());
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        }
        /* 处理历史记录 **/
        // processStepHandlerUtilService.audit(currentProcessTaskStepVo, ProcessTaskStepAction.BACK);

        /* 计算SLA **/
        IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);
        return 1;
    }

    protected abstract int myBack(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int saveDraft(ProcessTaskStepVo currentProcessTaskStepVo) {
        JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
        Long processTaskId = currentProcessTaskStepVo.getProcessTaskId();
        ProcessTaskVo processTaskVo;
        if (processTaskId == null) {// 首次保存
            processTaskVo = new ProcessTaskVo();
            processTaskVo.setTitle(paramObj.getString("title"));
            processTaskVo.setOwner(paramObj.getString("owner"));
            processTaskVo.setChannelUuid(paramObj.getString("channelUuid"));
            processTaskVo.setPriorityUuid(paramObj.getString("priorityUuid"));
            processTaskVo.setProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            processTaskVo.setReporter(UserContext.get().getUserUuid(true));
            processTaskVo.setStatus(ProcessTaskStatus.DRAFT.getValue());
            String source = paramObj.getString("source");
            if (StringUtils.isNotBlank(source)) {
                processTaskVo.setSource(source);
            }

            ProcessVo processVo = processMapper.getProcessByUuid(currentProcessTaskStepVo.getProcessUuid());
            /* 对流程配置进行散列处理 **/
            String configStr = processVo.getConfigStr();
            if (StringUtils.isNotBlank(configStr)) {
                String hash = DigestUtils.md5DigestAsHex(configStr.getBytes());
                processTaskVo.setConfigHash(hash);
                processTaskMapper.insertIgnoreProcessTaskConfig(new ProcessTaskConfigVo(hash, configStr));
            }
            ChannelVo channelVo = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
            String worktimeUuid = channelMapper.getWorktimeUuidByChannelUuid(processTaskVo.getChannelUuid());
            processTaskVo.setWorktimeUuid(worktimeUuid);
            /* 生成工单号 **/
            ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo = processTaskSerialNumberMapper.getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(channelVo.getChannelTypeUuid());
            if (processTaskSerialNumberPolicyVo == null) {
                throw new ProcessTaskSerialNumberPolicyNotFoundException(channelVo.getChannelTypeUuid());
            }
            IProcessTaskSerialNumberPolicyHandler policyHandler = ProcessTaskSerialNumberPolicyHandlerFactory.getHandler(processTaskSerialNumberPolicyVo.getHandler());
            if (policyHandler == null) {
                throw new ProcessTaskSerialNumberPolicyHandlerNotFoundException(processTaskSerialNumberPolicyVo.getHandler());
            }
            String serialNumber = policyHandler.genarate(processTaskSerialNumberPolicyVo);
            processTaskVo.setSerialNumber(serialNumber);
            processTaskSerialNumberMapper.insertProcessTaskSerialNumber(processTaskVo.getId(), serialNumber);
            /* 创建工单 **/
            processTaskMapper.insertProcessTask(processTaskVo);
            currentProcessTaskStepVo.setProcessTaskId(processTaskVo.getId());

            /* 写入表单信息 **/
            if (StringUtils.isNotBlank(processVo.getFormUuid())) {
                FormVersionVo formVersionVo = formMapper.getActionFormVersionByFormUuid(processVo.getFormUuid());
                if (formVersionVo != null && StringUtils.isNotBlank(formVersionVo.getFormConfig())) {
                    ProcessTaskFormVo processTaskFormVo = new ProcessTaskFormVo();
                    processTaskFormVo.setFormContent(formVersionVo.getFormConfig());
                    processTaskFormVo.setProcessTaskId(processTaskVo.getId());
                    processTaskFormVo.setFormUuid(formVersionVo.getFormUuid());
                    processTaskFormVo.setFormName(formVersionVo.getFormName());
                    processTaskMapper.insertProcessTaskForm(processTaskFormVo);
                    processTaskMapper.insertIgnoreProcessTaskFormContent(processTaskFormVo);
                }
            }

            ProcessScoreTemplateVo processScoreTemplateVo = processMapper.getProcessScoreTemplateByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            if (processScoreTemplateVo != null) {
                ProcessTaskScoreTemplateVo processTaskScoreTemplateVo =
                        new ProcessTaskScoreTemplateVo(processScoreTemplateVo);
                if (processTaskScoreTemplateVo.getConfig() != null) {
                    ProcessTaskScoreTemplateConfigVo processTaskScoreTemplateConfigVo =
                            new ProcessTaskScoreTemplateConfigVo(processTaskScoreTemplateVo.getConfigStr());
                    if (StringUtils.isNotBlank(processTaskScoreTemplateConfigVo.getHash()) && selectContentByHashMapper
                            .checkProcessTaskScoreTempleteConfigIsExists(processTaskScoreTemplateConfigVo.getHash()) == 0) {
                        processTaskMapper.insertProcessTaskScoreTempleteConfig(processTaskScoreTemplateConfigVo);
                    }
                    processTaskScoreTemplateVo.setConfigHash(processTaskScoreTemplateConfigVo.getHash());
                }
                processTaskScoreTemplateVo.setProcessTaskId(processTaskVo.getId());
                processTaskMapper.insertProcessTaskScoreTemplate(processTaskScoreTemplateVo);
            }
            Map<String, Long> stepIdMap = new HashMap<>();
            /* 写入所有步骤信息 **/
            List<ProcessStepVo> processStepList = processMapper.getProcessStepDetailByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            for (ProcessStepVo stepVo : processStepList) {
                ProcessTaskStepVo ptStepVo = new ProcessTaskStepVo(stepVo);
                ptStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
                ptStepVo.setProcessTaskId(processTaskVo.getId());
                if (StringUtils.isNotBlank(ptStepVo.getConfig())) {
                    /* 对步骤配置进行散列处理 **/
                    String hash = DigestUtils.md5DigestAsHex(ptStepVo.getConfig().getBytes());
                    ptStepVo.setConfigHash(hash);
                    processTaskMapper.replaceProcessTaskStepConfig(new ProcessTaskStepConfigVo(hash, ptStepVo.getConfig()));
                }

                processTaskMapper.insertProcessTaskStep(ptStepVo);
                stepIdMap.put(ptStepVo.getProcessStepUuid(), ptStepVo.getId());

                /* 写入步骤表单属性 **/
                if (CollectionUtils.isNotEmpty(ptStepVo.getFormAttributeList())) {
                    for (ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : ptStepVo.getFormAttributeList()) {
                        processTaskStepFormAttributeVo.setProcessTaskId(processTaskVo.getId());
                        processTaskStepFormAttributeVo.setProcessTaskStepId(ptStepVo.getId());
                        processTaskMapper.insertProcessTaskStepFormAttribute(processTaskStepFormAttributeVo);
                    }
                }

                /* 写入用户分配策略信息 **/
                if (CollectionUtils.isNotEmpty(ptStepVo.getWorkerPolicyList())) {
                    for (ProcessTaskStepWorkerPolicyVo policyVo : ptStepVo.getWorkerPolicyList()) {
                        policyVo.setProcessTaskId(processTaskVo.getId());
                        policyVo.setProcessTaskStepId(ptStepVo.getId());
                        processTaskMapper.insertProcessTaskStepWorkerPolicy(policyVo);
                    }
                }

                /* 找到开始节点 **/
                if (stepVo.getType().equals(ProcessStepType.START.getValue())) {
                    currentProcessTaskStepVo.setId(ptStepVo.getId());
                }

                Long notifyPolicyId = processMapper.getNotifyPolicyIdByProcessStepUuid(ptStepVo.getProcessStepUuid());
                if (notifyPolicyId != null) {
                    NotifyPolicyVo notifyPolicyVo = notifyMapper.getNotifyPolicyById(notifyPolicyId);
                    if (notifyPolicyVo != null) {
                        ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo = new ProcessTaskStepNotifyPolicyVo();
                        processTaskStepNotifyPolicyVo.setProcessTaskStepId(ptStepVo.getId());
                        processTaskStepNotifyPolicyVo.setPolicyId(notifyPolicyVo.getId());
                        processTaskStepNotifyPolicyVo.setPolicyName(notifyPolicyVo.getName());
                        processTaskStepNotifyPolicyVo.setPolicyHandler(notifyPolicyVo.getHandler());
                        processTaskStepNotifyPolicyVo.setPolicyConfig(notifyPolicyVo.getConfigStr());
                        processTaskMapper.insertIgnoreProcessTaskStepNotifyPolicyConfig(processTaskStepNotifyPolicyVo);
                        processTaskMapper.insertProcessTaskStepNotifyPolicy(processTaskStepNotifyPolicyVo);
                    }
                }
            }

            /* 写入关系信息 **/
            List<ProcessStepRelVo> processStepRelList = processMapper.getProcessStepRelByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            for (ProcessStepRelVo relVo : processStepRelList) {
                ProcessTaskStepRelVo processTaskStepRelVo = new ProcessTaskStepRelVo(relVo);
                processTaskStepRelVo.setProcessTaskId(processTaskVo.getId());
                processTaskStepRelVo.setFromProcessTaskStepId(stepIdMap.get(processTaskStepRelVo.getFromProcessStepUuid()));
                processTaskStepRelVo.setToProcessTaskStepId(stepIdMap.get(processTaskStepRelVo.getToProcessStepUuid()));
                /* 同时找到from step id 和to step id 时才写入，其他数据舍弃 **/
                if (processTaskStepRelVo.getFromProcessTaskStepId() != null && processTaskStepRelVo.getToProcessTaskStepId() != null) {
                    processTaskMapper.insertProcessTaskStepRel(processTaskStepRelVo);
                }
            }

            /* 写入sla信息 **/
            List<ProcessSlaVo> processSlaList = processMapper.getProcessSlaByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            for (ProcessSlaVo slaVo : processSlaList) {
                List<String> slaStepUuidList = processMapper.getProcessStepUuidBySlaUuid(slaVo.getUuid());
                if (CollectionUtils.isNotEmpty(slaStepUuidList)) {
                    ProcessTaskSlaVo processTaskSlaVo = new ProcessTaskSlaVo(slaVo);
                    processTaskSlaVo.setProcessTaskId(processTaskVo.getId());
                    processTaskMapper.insertProcessTaskSla(processTaskSlaVo);
                    for (String suuid : slaStepUuidList) {
                        Long stepId = stepIdMap.get(suuid);
                        if (stepId != null) {
                            processTaskMapper.insertProcessTaskStepSla(stepId, processTaskSlaVo.getId());
                        }
                    }
                }
            }

            /* 加入上报人为处理人 **/
            ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue());
            processTaskMapper.insertProcessTaskStepUser(processTaskStepUserVo);
            processTaskMapper.insertProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue()));

            /* 保存转报数据 **/
            Long fromProcessTaskId = paramObj.getLong("fromProcessTaskId");
            Long channelTypeRelationId = paramObj.getLong("channelTypeRelationId");
            if (fromProcessTaskId != null && channelTypeRelationId != null) {
                processTaskMapper.insertProcessTaskTranferReport(new ProcessTaskTranferReportVo(channelTypeRelationId, fromProcessTaskId, processTaskVo.getId()));
            }
        } else {
            /* 锁定当前流程 **/
            processTaskMapper.getProcessTaskLockById(processTaskId);
            // 第二次保存时的操作
            processTaskVo = processTaskMapper.getProcessTaskById(processTaskId);
            new ProcessAuthManager.TaskOperationChecker(processTaskVo.getId(), ProcessTaskOperationType.TASK_START).build().checkAndNoPermissionThrowException();
            /* 更新工单信息 **/
            processTaskVo.setTitle(paramObj.getString("title"));
            processTaskVo.setOwner(paramObj.getString("owner"));
            processTaskVo.setPriorityUuid(paramObj.getString("priorityUuid"));
            processTaskMapper.updateProcessTaskTitleOwnerPriorityUuid(processTaskVo);
            processTaskMapper.deleteProcessTaskStepContentByProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskMapper.deleteProcessTaskStepFileByProcessTaskStepId(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
        }
        try {
            mySaveDraft(currentProcessTaskStepVo);
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.DRAFT.getValue());
            currentProcessTaskStepVo.setUpdateActiveTime(1);
            currentProcessTaskStepVo.setUpdateStartTime(1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            currentProcessTaskStepVo.setError(ex.getMessage());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        }

        return 1;
    }

    protected abstract int mySaveDraft(ProcessTaskStepVo processTaskStepVo) throws ProcessTaskException;

    @Override
    public final int startProcess(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
            new ProcessAuthManager.TaskOperationChecker(currentProcessTaskStepVo.getProcessTaskId(), ProcessTaskOperationType.TASK_START).build().checkAndNoPermissionThrowException();

            IProcessStepHandlerUtil.assignWorkerValid(currentProcessTaskStepVo);
            IProcessStepHandlerUtil.baseInfoValidFromDb(currentProcessTaskStepVo);

            /* 保存表单属性值 **/
            IProcessStepHandlerUtil.saveForm(currentProcessTaskStepVo);

            /* 保存描述内容和附件 **/
            IProcessStepHandlerUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.TASK_START);

            /* 写入“标签”信息 **/
            IProcessStepHandlerUtil.saveTagList(currentProcessTaskStepVo);
            /* 保存工单关注人 **/
            IProcessStepHandlerUtil.saveFocusUserList(currentProcessTaskStepVo);
            myStartProcess(currentProcessTaskStepVo);
            // 获取表单数据
            List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
                processTaskFormAttributeDataList.sort(ProcessTaskFormAttributeDataVo::compareTo);
                JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
                paramObj.put(ProcessTaskAuditDetailType.FORM.getParamName(), JSON.toJSONString(processTaskFormAttributeDataList));
            }
            /* 更新处理人状态 **/
            ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid(true));
            processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
            processTaskMajorUser.setUserType(ProcessUserType.MAJOR.getValue());
            processTaskMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
            /* 清空worker表 **/
            processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
            currentProcessTaskStepVo.setIsActive(2);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
            currentProcessTaskStepVo.setUpdateEndTime(1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);

            /* 流转到下一步 **/
            Set<ProcessTaskStepVo> nextStepList = getNext(currentProcessTaskStepVo);
            for (ProcessTaskStepVo nextStep : nextStepList) {
                IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
                nextStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                nextStep.setStartProcessTaskStepId(currentProcessTaskStepVo.getId());
                if (nextStepHandler != null) {
                    doNext(ProcessTaskOperationType.STEP_ACTIVE, new ProcessStepThread(nextStep) {
                        @Override
                        public void myExecute() {
                            nextStepHandler.active(nextStep);
                        }

                    });
                }
            }
            /* 写入时间审计 **/
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_ACTIVE);
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_START);
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_COMPLETE);

            /* 计算SLA并触发超时警告 **/
            IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskNotifyTriggerType.STARTPROCESS);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskNotifyTriggerType.STARTPROCESS);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            currentProcessTaskStepVo.setError(ex.getMessage());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.FAILED);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.FAILED);
        } finally {
            /* 处理历史记录 **/
            ProcessTaskTranferReportVo processTaskTranferReportVo = processTaskMapper.getProcessTaskTranferReportByToProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            if (processTaskTranferReportVo != null) {
                currentProcessTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.CHANNELTYPERELATION.getParamName(), processTaskTranferReportVo.getChannelTypeRelationId());
                currentProcessTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.PROCESSTASK.getParamName(), processTaskTranferReportVo.getFromProcessTaskId());
                IProcessStepHandlerUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.REPORTRELATION);

                ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
                processTaskStepVo.setProcessTaskId(processTaskTranferReportVo.getFromProcessTaskId());
                processTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.CHANNELTYPERELATION.getParamName(), processTaskTranferReportVo.getChannelTypeRelationId());
                processTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.PROCESSTASKLIST.getParamName(), JSON.toJSONString(Arrays.asList(currentProcessTaskStepVo.getProcessTaskId())));
                IProcessStepHandlerUtil.audit(processTaskStepVo, ProcessTaskAuditType.TRANFERREPORT);
            } else {
                IProcessStepHandlerUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.STARTPROCESS);
            }

        }
        return 0;
    }

    protected abstract int myStartProcess(ProcessTaskStepVo processTaskStepVo) throws ProcessTaskException;

    @Override
    public final Set<ProcessTaskStepVo> getNext(ProcessTaskStepVo currentProcessTaskStepVo) {
        List<ProcessTaskStepRelVo> relList =
                processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
        // 重置所有关系状态为-1
        for (ProcessTaskStepRelVo rel : relList) {
            processTaskMapper.updateProcessTaskStepRelIsHit(rel.getFromProcessTaskStepId(),
                    rel.getToProcessTaskStepId(), -1);
        }
        currentProcessTaskStepVo.setRelList(relList);
        Long nextStepId = currentProcessTaskStepVo.getParamObj().getLong("nextStepId");
        List<ProcessTaskStepVo> nextStepList =
                processTaskMapper.getToProcessTaskStepByFromIdAndType(currentProcessTaskStepVo.getId(), null);
        Set<ProcessTaskStepVo> nextStepSet = null;
        try {
            nextStepSet = myGetNext(currentProcessTaskStepVo, nextStepList, nextStepId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            if (ex.getMessage() != null && !ex.getMessage().equals("")) {
                currentProcessTaskStepVo.appendError(ex.getMessage());
            } else {
                currentProcessTaskStepVo.appendError(ExceptionUtils.getStackTrace(ex));
            }
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            /* 异常提醒 **/
            IProcessStepHandlerUtil.saveStepRemind(currentProcessTaskStepVo,
                    currentProcessTaskStepVo.getId(), ex.getMessage(), ProcessTaskStepRemindType.ERROR);
        }

        /* 更新路径isHit=1，在active方法里需要根据isHit状态判断路径是否经通过 **/
        if (nextStepSet == null) {
            nextStepSet = new HashSet<>();
        }
        for (ProcessTaskStepVo stepVo : nextStepSet) {
            processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(), stepVo.getId(), 1);
        }
        return nextStepSet;
    }

    private void resetConvergeInfo(ProcessTaskStepVo nextStepVo) {

        List<ProcessTaskStepVo> stepList = processTaskMapper
                .getProcessTaskStepByProcessTaskIdAndType(nextStepVo.getProcessTaskId(), ProcessStepType.END.getValue());
        ProcessTaskStepVo endStepVo = null;
        if (stepList.size() == 1) {
            endStepVo = stepList.get(0);
        }
        // 重新插入汇聚数据
        List<List<Long>> routeList = new ArrayList<>();
        List<Long> routeStepList = new ArrayList<>();
        routeList.add(routeStepList);

        getAllRouteList(nextStepVo.getId(), routeList, routeStepList, endStepVo);
        // 如果最后一个步骤不是结束节点的路由全部删掉，因为这是回环路由
        Iterator<List<Long>> routeStepIt = routeList.iterator();
        List<Long> convergeIdList = new ArrayList<>();
        while (routeStepIt.hasNext()) {
            List<Long> rsList = routeStepIt.next();
            if (!rsList.get(rsList.size() - 1).equals(endStepVo.getId())) {
                routeStepIt.remove();
            } else {
                for (Long cid : rsList) {
                    if (!convergeIdList.contains(cid) && !cid.equals(nextStepVo.getId())) {
                        convergeIdList.add(cid);
                    }
                }
            }
        }
        if (convergeIdList.size() > 0) {
            for (Long convergeId : convergeIdList) {
                ProcessTaskConvergeVo processTaskStepConvergeVo =
                        new ProcessTaskConvergeVo(nextStepVo.getProcessTaskId(), nextStepVo.getId(), convergeId);
                if (processTaskMapper.checkProcessTaskConvergeIsExists(processTaskStepConvergeVo) == 0) {
                    processTaskMapper.insertProcessTaskConverge(processTaskStepConvergeVo);
                }
            }
        }
    }

    private void getAllRouteList(Long processTaskStepId, List<List<Long>> routeList, List<Long> routeStepList,
                                 ProcessTaskStepVo endStepVo) {
        if (!routeStepList.contains(processTaskStepId)) {
            routeStepList.add(processTaskStepId);
            List<Long> tmpRouteStepList = new ArrayList<>(routeStepList);
            if (!processTaskStepId.equals(endStepVo.getId())) {
                List<ProcessTaskStepVo> convergeStepList =
                        processTaskMapper.getProcessTaskStepByConvergeId(processTaskStepId);
                List<ProcessTaskStepVo> toProcessTaskStepList =
                        processTaskMapper.getToProcessTaskStepByFromIdAndType(processTaskStepId, null);
                for (int i = 0; i < toProcessTaskStepList.size(); i++) {
                    ProcessTaskStepVo toProcessTaskStepVo = toProcessTaskStepList.get(i);
                    /* 当前节点不是别人的汇聚节点时，才记录进路由，这是为了避免因为出现打回路径而产生错误的汇聚数据 **/
                    if (!convergeStepList.contains(toProcessTaskStepVo)) {
                        if (i > 0) {
                            List<Long> newRouteStepList = new ArrayList<>(tmpRouteStepList);
                            routeList.add(newRouteStepList);
                            getAllRouteList(toProcessTaskStepVo.getId(), routeList, newRouteStepList, endStepVo);
                        } else {
                            getAllRouteList(toProcessTaskStepVo.getId(), routeList, routeStepList, endStepVo);
                        }
                    }
                }
            }
        }
    }

    protected abstract Set<ProcessTaskStepVo> myGetNext(ProcessTaskStepVo currentProcessTaskStepVo,
                                                        List<ProcessTaskStepVo> nextStepList, Long nextStepId) throws ProcessTaskException;

    protected synchronized static void doNext(ProcessTaskOperationType operationType, ProcessStepThread thread) {
        if (operationType != null) {
            ProcessTaskStepInOperationVo processTaskStepInOperationVo =
                    new ProcessTaskStepInOperationVo(thread.getProcessTaskStepVo().getProcessTaskId(),
                            thread.getProcessTaskStepVo().getId(), operationType.getValue());
            processTaskMapper.insertProcessTaskStepInOperation(processTaskStepInOperationVo);
            thread.setSupplier(() -> processTaskMapper
                    .deleteProcessTaskStepInOperationByProcessTaskStepIdAndOperationType(processTaskStepInOperationVo));
        }
        TransactionSynchronizationPool.execute(thread);
    }

    /**
     * handle方法异步模式会调用这个方法
     **/
    protected synchronized static void doNext(ProcessStepThread thread) {
        doNext(null, thread);
    }

    public int redo(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
            IProcessStepInternalHandler processStepUtilHandler =
                    ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }

            new ProcessAuthManager.TaskOperationChecker(currentProcessTaskStepVo.getProcessTaskId(),
                    ProcessTaskOperationType.TASK_REDO).build().checkAndNoPermissionThrowException();

            stepMajorUserRegulate(currentProcessTaskStepVo);
            /* 设置当前步骤状态为未开始 **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
            /* 保存打回原因 **/
            IProcessStepHandlerUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.TASK_REDO);
            myRedo(currentProcessTaskStepVo);

            /* 遍历后续节点所有步骤，写入汇聚步骤数据 **/
            resetConvergeInfo(currentProcessTaskStepVo);

            /* 如果当前步骤是二次进入(后续路径已经走过)，则需要对所有后续流转过的步骤都进行挂起操作 **/
            /* 获取当前步骤状态 **/
            List<ProcessTaskStepRelVo> nextTaskStepRelList =
                    processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
            for (ProcessTaskStepRelVo nextTaskStepRelVo : nextTaskStepRelList) {
                if (nextTaskStepRelVo != null && nextTaskStepRelVo.getIsHit().equals(1)) {
                    ProcessTaskStepVo nextProcessTaskStepVo =
                            processTaskMapper.getProcessTaskStepBaseInfoById(nextTaskStepRelVo.getToProcessTaskStepId());
                    if (nextProcessTaskStepVo != null) {
                        IProcessStepHandler handler =
                                ProcessStepHandlerFactory.getHandler(nextProcessTaskStepVo.getHandler());
                        // 标记挂起操作来源步骤
                        nextProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                        // 标记挂起操作的发起步骤，避免出现死循环
                        nextProcessTaskStepVo.setStartProcessTaskStepId(currentProcessTaskStepVo.getId());
                        if (handler != null) {
                            doNext(ProcessTaskOperationType.STEP_HANG, new ProcessStepThread(nextProcessTaskStepVo) {
                                @Override
                                public void myExecute() {
                                    handler.hang(nextProcessTaskStepVo);
                                }
                            });
                        }
                    }
                }
                // 恢复路径命中状态为0，代表路径未通过
                processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(),
                        nextTaskStepRelVo.getToProcessTaskStepId(), 0);
            }

            /* 分配处理人 **/
            assign(currentProcessTaskStepVo);
            currentProcessTaskStepVo.setIsActive(1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            processTaskScoreMapper
                    .deleteProcessTaskAutoScoreByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            /* 写入时间审计 **/
            IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.TASK_REDO);
            if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
                IProcessStepHandlerUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.STEP_START);
            }

            /* 计算SLA并触发超时警告 **/
            IProcessStepHandlerUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            IProcessStepHandlerUtil.notify(currentProcessTaskStepVo, TaskNotifyTriggerType.REOPENPROCESSTASK);

            /* 执行动作 **/
            IProcessStepHandlerUtil.action(currentProcessTaskStepVo, TaskNotifyTriggerType.REOPENPROCESSTASK);

            /* 回退提醒 **/
            IProcessStepHandlerUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getId(), currentProcessTaskStepVo.getParamObj().getString("content"), ProcessTaskStepRemindType.REDO);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            IProcessStepHandlerUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.REDO);
        }
        return 1;
    }

    protected abstract int myRedo(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    public int scoreProcessTask(ProcessTaskVo currentProcessTaskVo) {
        // 锁定当前流程
        processTaskMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
        // 只有上报人才可评分
        new ProcessAuthManager.TaskOperationChecker(currentProcessTaskVo.getId(), ProcessTaskOperationType.TASK_SCORE)
                .build().checkAndNoPermissionThrowException();

        JSONObject paramObj = currentProcessTaskVo.getParamObj();
        Long scoreTemplateId = paramObj.getLong("scoreTemplateId");
        String content = paramObj.getString("content");
        List<ScoreTemplateDimensionVo> scoreDimensionList =
                JSON.parseArray(paramObj.getJSONArray("scoreDimensionList").toJSONString(), ScoreTemplateDimensionVo.class);

        ProcessTaskScoreVo processtaskScoreVo = new ProcessTaskScoreVo();
        processtaskScoreVo.setProcessTaskId(currentProcessTaskVo.getId());
        processtaskScoreVo.setScoreTemplateId(scoreTemplateId);
        processtaskScoreVo.setFcu(UserContext.get().getUserUuid());
        processtaskScoreVo.setIsAuto(0);
        for (ScoreTemplateDimensionVo scoreTemplateDimensionVo : scoreDimensionList) {
            processtaskScoreVo.setScoreDimensionId(scoreTemplateDimensionVo.getId());
            processtaskScoreVo.setScore(scoreTemplateDimensionVo.getScore());
            processTaskScoreMapper.insertProcessTaskScore(processtaskScoreVo);
        }

        JSONObject contentObj = new JSONObject();
        contentObj.put("scoreTemplateId", scoreTemplateId);
        contentObj.put("content", content);
        contentObj.put("dimensionList", paramObj.getJSONArray("scoreDimensionList"));
        JSONObject scoreObj = new JSONObject();
        scoreObj.put(ProcessTaskAuditDetailType.SCORE.getParamName(), contentObj);
        /*
         * processtask_content表存储了两份数据： 1、评价内容content本身 2、由评分模版ID、评价内容、评分维度与分数组装而成的JSON
         */
        if (StringUtils.isNotBlank(content)) {
            ProcessTaskContentVo contentVo = new ProcessTaskContentVo(content);
            processTaskMapper.insertIgnoreProcessTaskContent(contentVo);
            processtaskScoreVo.setContentHash(contentVo.getHash());
            processTaskScoreMapper.insertProcessTaskScoreContent(processtaskScoreVo);
        }
        currentProcessTaskVo.setStatus(ProcessTaskStatus.SCORED.getValue());
        processTaskMapper.updateProcessTaskStatus(currentProcessTaskVo);
        processTaskScoreMapper.deleteProcessTaskAutoScoreByProcessTaskId(currentProcessTaskVo.getId());
        ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
        processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
        processTaskStepVo.setParamObj(scoreObj);
        /* 生成活动 */
        IProcessStepHandlerUtil.audit(processTaskStepVo, ProcessTaskAuditType.SCORE);
        /* 触发通知 **/
        IProcessStepHandlerUtil.notify(processTaskStepVo, TaskNotifyTriggerType.SCOREPROCESSTASK);
        IProcessStepHandlerUtil.action(processTaskStepVo, TaskNotifyTriggerType.SCOREPROCESSTASK);
        return 1;
    }

    /**
     * @param currentProcessTaskStepVo
     * @return void
     * @Time:2020年9月30日
     * @Description: 步骤主处理人校正操作 判断当前用户是否是代办人，如果不是就什么都不做，如果是，进行下面3个操作 1.往processtask_step_agent表中插入一条数据，记录该步骤的原主处理人和代办人
     * 2.将processtask_step_worker表中该步骤的主处理人uuid改为代办人(当前用户)
     * 3.将processtask_step_user表中该步骤的主处理人user_uuid改为代办人(当前用户)
     */
    private void stepMajorUserRegulate(ProcessTaskStepVo currentProcessTaskStepVo) {
        /* 能进入这个方法，说明当前用户有权限处理当前步骤，可能是三类处理人：第一处理人(A)、代办人(B)、代办人的代办人(C) 。其中A授权给B，B授权给C **/
        ProcessTaskStepAgentVo processTaskStepAgentVo =
                processTaskMapper.getProcessTaskStepAgentByProcessTaskStepId(currentProcessTaskStepVo.getId());
        if (processTaskStepAgentVo == null) {
            // 代办人还没接管，当前用户可能是A和B
            List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
            if (processTaskMapper.checkIsWorker(currentProcessTaskStepVo.getProcessTaskId(),
                    currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true),
                    teamUuidList, UserContext.get().getRoleUuidList()) == 0) {
                // 当用户是B
                String userUuid =
                        userMapper.getUserUuidByAgentUuidAndFunc(UserContext.get().getUserUuid(), "processTask");
                if (StringUtils.isNotBlank(userUuid)) {
                    processTaskMapper.replaceProcesssTaskStepAgent(
                            new ProcessTaskStepAgentVo(currentProcessTaskStepVo.getProcessTaskId(),
                                    currentProcessTaskStepVo.getId(), userUuid, UserContext.get().getUserUuid(true)));
                    processTaskMapper.updateProcessTaskStepWorkerUuid(
                            new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(),
                                    currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), userUuid,
                                    ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                    processTaskMapper.updateProcessTaskStepUserUserUuid(new ProcessTaskStepUserVo(
                            currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), userUuid,
                            ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                    currentProcessTaskStepVo.setOriginalUser(userUuid);
                }
            }
        } else {
            // 代办人接管过了，当前用户可能是A、B、C
            if (UserContext.get().getUserUuid(true).equals(processTaskStepAgentVo.getUserUuid())) {
                // 当前用户是A
                processTaskMapper.deleteProcessTaskStepAgentByProcessTaskStepId(currentProcessTaskStepVo.getId());
                processTaskMapper.updateProcessTaskStepWorkerUuid(new ProcessTaskStepWorkerVo(
                        currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(),
                        GroupSearch.USER.getValue(), processTaskStepAgentVo.getAgentUuid(),
                        ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                processTaskMapper.updateProcessTaskStepUserUserUuid(
                        new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(),
                                currentProcessTaskStepVo.getId(), processTaskStepAgentVo.getAgentUuid(),
                                ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
            } else if (UserContext.get().getUserUuid(true).equals(processTaskStepAgentVo.getAgentUuid())) {
                // 当前用户是B
                currentProcessTaskStepVo.setOriginalUser(processTaskStepAgentVo.getUserUuid());
            } else {
                // 当前用户是C
                processTaskMapper.replaceProcesssTaskStepAgent(new ProcessTaskStepAgentVo(
                        currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(),
                        processTaskStepAgentVo.getAgentUuid(), UserContext.get().getUserUuid(true)));
                processTaskMapper.updateProcessTaskStepWorkerUuid(new ProcessTaskStepWorkerVo(
                        currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(),
                        GroupSearch.USER.getValue(), processTaskStepAgentVo.getAgentUuid(),
                        ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                processTaskMapper.updateProcessTaskStepUserUserUuid(
                        new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(),
                                currentProcessTaskStepVo.getId(), processTaskStepAgentVo.getAgentUuid(),
                                ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                currentProcessTaskStepVo.setOriginalUser(processTaskStepAgentVo.getAgentUuid());
            }
        }

    }
}
