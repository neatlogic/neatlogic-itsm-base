/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.stephandler.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.common.constvalue.SystemUser;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.dao.mapper.RoleMapper;
import neatlogic.framework.dao.mapper.TeamMapper;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.dto.AuthenticationInfoVo;
import neatlogic.framework.dto.RoleTeamVo;
import neatlogic.framework.dto.TeamVo;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.form.dao.mapper.FormMapper;
import neatlogic.framework.form.dto.FormVersionVo;
import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerFactory;
import neatlogic.framework.fulltextindex.core.IFullTextIndexHandler;
import neatlogic.framework.notify.dao.mapper.NotifyMapper;
import neatlogic.framework.process.constvalue.*;
import neatlogic.framework.process.crossover.*;
import neatlogic.framework.process.dto.*;
import neatlogic.framework.process.dto.score.ProcessScoreTemplateVo;
import neatlogic.framework.process.dto.score.ProcessTaskScoreVo;
import neatlogic.framework.process.dto.score.ScoreTemplateDimensionVo;
import neatlogic.framework.process.exception.channeltype.ChannelTypeNotFoundException;
import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;
import neatlogic.framework.process.exception.operationauth.ProcessTaskStepRunningException;
import neatlogic.framework.process.exception.process.ProcessStepHandlerNotFoundException;
import neatlogic.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import neatlogic.framework.process.exception.processtask.*;
import neatlogic.framework.process.exception.processtaskserialnumberpolicy.ProcessTaskSerialNumberPolicyHandlerNotFoundException;
import neatlogic.framework.process.exception.processtaskserialnumberpolicy.ProcessTaskSerialNumberPolicyNotFoundException;
import neatlogic.framework.process.fulltextindex.ProcessFullTextIndexType;
import neatlogic.framework.process.notify.constvalue.ProcessTaskNotifyTriggerType;
import neatlogic.framework.process.notify.constvalue.ProcessTaskStepNotifyTriggerType;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.operationauth.core.ProcessAuthManager;
import neatlogic.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;
import neatlogic.framework.process.processtaskserialnumberpolicy.core.ProcessTaskSerialNumberPolicyHandlerFactory;
import neatlogic.framework.process.workerpolicy.core.IWorkerPolicyHandler;
import neatlogic.framework.process.workerpolicy.core.WorkerPolicyHandlerFactory;
import neatlogic.framework.service.AuthenticationInfoService;
import neatlogic.framework.transaction.core.AfterTransactionJob;
import neatlogic.framework.worktime.dao.mapper.WorktimeMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.*;

public abstract class ProcessStepHandlerBase implements IProcessStepHandler {
    static Logger logger = LoggerFactory.getLogger(ProcessStepHandlerBase.class);
    protected static UserMapper userMapper;
    protected static TeamMapper teamMapper;
    protected static RoleMapper roleMapper;
    protected static AuthenticationInfoService authenticationInfoService;
    protected static FormMapper formMapper;
    protected static NotifyMapper notifyMapper;
    protected static WorktimeMapper worktimeMapper;

    @Resource
    public void setFormMapper(FormMapper _formMapper) {
        formMapper = _formMapper;
    }

    @Resource
    public void setUserMapper(UserMapper _userMapper) {
        userMapper = _userMapper;
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
    public void setAuthenticationInfoService(AuthenticationInfoService _authenticationInfoService) {
        authenticationInfoService = _authenticationInfoService;
    }

    @Resource
    public void setWorktimeMapper(WorktimeMapper _worktimeMapper) {
        worktimeMapper = _worktimeMapper;
    }

    private int updateProcessTaskStatus(Long processTaskId) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        List<ProcessTaskStepVo> processTaskStepList = processTaskCrossoverMapper.getProcessTaskStepBaseInfoByProcessTaskId(processTaskId);
        int runningCount = 0, succeedCount = 0, failedCount = 0, abortedCount = 0, draftCount = 0, hangCount = 0, endSucceedCount = 0;
        for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
            if (ProcessTaskStepStatus.DRAFT.getValue().equals(processTaskStepVo.getStatus())
                    && processTaskStepVo.getIsActive().equals(1)) {
                draftCount += 1;
            } else if (processTaskStepVo.getIsActive().equals(1) && processTaskStepVo.getStatus().equals(ProcessTaskStepStatus.HANG.getValue())) {
                hangCount += 1;
            } else if (processTaskStepVo.getIsActive().equals(1)) {
                if (processTaskStepVo.getStatus().equals(ProcessTaskStepStatus.PENDING.getValue())
                        || processTaskStepVo.getStatus().equals(ProcessTaskStepStatus.RUNNING.getValue())) {
                    runningCount += 1;
                } else if (processTaskStepVo.getStatus().equals(ProcessTaskStepStatus.FAILED.getValue())) {
                    failedCount += 1;
                }
            } else if (processTaskStepVo.getIsActive().equals(-1)) {
                abortedCount += 1;
            } else if (processTaskStepVo.getStatus().equals(ProcessTaskStepStatus.SUCCEED.getValue())) {
                if (ProcessStepHandlerType.END.getHandler().equals(processTaskStepVo.getHandler())) {
                    endSucceedCount += 1;
                } else {
                    succeedCount += 1;
                }
            }
        }

        boolean needCalculateTimeCost = false;
        ProcessTaskVo processTaskVo = new ProcessTaskVo();
        processTaskVo.setId(processTaskId);
        if (draftCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.DRAFT.getValue());
        } else if (endSucceedCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
            needCalculateTimeCost = true;
        } else if (abortedCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.ABORTED.getValue());
            needCalculateTimeCost = true;
        } else if (failedCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            needCalculateTimeCost = true;
        } else if (runningCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
        } else if (hangCount > 0) {
            processTaskVo.setStatus(ProcessTaskStatus.HANG.getValue());
        } else {
            return 1;
        }
        processTaskCrossoverMapper.updateProcessTaskStatus(processTaskVo);
        //如果工单状态为“已完成”、“已取消”、“异常”时，计算工单耗时
        if (needCalculateTimeCost) {
            ProcessTaskVo processTask = processTaskCrossoverMapper.getProcessTaskById(processTaskId);
            Date startTime = processTask.getStartTime();
            Date endTime = processTask.getEndTime();
            String worktimeUuid = processTask.getWorktimeUuid();
            if (startTime != null && endTime != null) {
                long startTimeLong = startTime.getTime();
                long endTimeLong = endTime.getTime();
                ProcessTaskTimeCostVo processTaskTimeCostVo = new ProcessTaskTimeCostVo();
                processTaskTimeCostVo.setProcessTaskId(processTaskId);
                long realTimeCost = endTimeLong - startTimeLong;
                processTaskTimeCostVo.setRealTimeCost(realTimeCost);
                if (worktimeUuid != null) {
                    long timeCost = worktimeMapper.calculateCostTime(worktimeUuid, startTimeLong, endTimeLong);
                    processTaskTimeCostVo.setTimeCost(timeCost);
                } else {
                    processTaskTimeCostVo.setTimeCost(realTimeCost);
                }
                processTaskCrossoverMapper.insertProcessTaskTimeCost(processTaskTimeCostVo);
            }
        } else {
            processTaskCrossoverMapper.deleteProcessTaskTimeCostByProcessTaskId(processTaskId);
        }
        return 1;
    }

    @Override
    public final int active(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        try {
            IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
            /* 锁定当前流程 **/
            processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
            boolean canFire = false;
            /* 获取当前步骤的所有前置步骤 **/
            /* 获取当前步骤的所有前置连线 **/
            List<ProcessTaskStepRelVo> fromProcessTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());
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
                List<ProcessTaskStepVo> convergeStepList = processTaskCrossoverMapper.getProcessTaskStepByConvergeId(currentProcessTaskStepVo.getId());
                boolean hasDoingStep = false;
                for (ProcessTaskStepVo convergeStepVo : convergeStepList) {
                    /* 任意前置节点处于处理中状态 **/
                    if (convergeStepVo.getIsActive().equals(1)) {
                        hasDoingStep = true;
                        break;
                    }
                }
                if (!hasDoingStep) {
                    for (ProcessTaskStepRelVo fromProcessTaskStepRelVo : fromProcessTaskStepRelList) {
                        if (Objects.equals(fromProcessTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                            if (fromProcessTaskStepRelVo.getIsHit().equals(0)) {
                                hasDoingStep = true;
                            }
                        }
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
                currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.PENDING.getValue());
                currentProcessTaskStepVo.setUpdateActiveTime(1);
                /* 遍历后续节点所有步骤，写入汇聚步骤数据 **/
                resetConvergeInfo(currentProcessTaskStepVo);
                /* 如果当前步骤是二次进入(后续路径已经走过)，则需要对所有后续流转过的步骤都进行挂起操作 **/
                hangPostStep(currentProcessTaskStepVo);
                resetPostStepRelIsHit(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
                if (this.getMode().equals(ProcessStepMode.MT)) {
                    if (!this.disableAssign()) {
                        /* 分配处理人 **/
                        assign(currentProcessTaskStepVo);
                    } else {
                        /* 清空主处理人 **/
                        ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
                        processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
                        processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());// 只删除主处理人
                        processTaskCrossoverMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
                    }

                    currentProcessTaskStepVo.setIsActive(1);
                    myActive(currentProcessTaskStepVo);
                    updateProcessTaskStepStatus(currentProcessTaskStepVo);
                    IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
                    if (processStepUtilHandler != null) {
                        processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
                    }
                    currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
                    ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_ACTIVE);
                    /* 写入时间审计 **/
                    processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_ACTIVE);
                    if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStepStatus.RUNNING.getValue())) {
                        processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_START);
                        processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.START);
                        processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.START);
                    }
                    // 删除时效延时数据
                    IProcessTaskSlaCrossoverMapper processTaskSlaCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskSlaCrossoverMapper.class);
                    processTaskSlaCrossoverMapper.deleteProcessTaskStepSlaDelayByTargetProcessTaskStepId(currentProcessTaskStepVo.getId());
                    /* 计算SLA并触发超时警告 **/
                    processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);

                    /* 触发通知 **/
                    processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.ACTIVE);



                    /* 执行动作 **/
                    processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.ACTIVE);
                } else if (this.getMode().equals(ProcessStepMode.AT)) {
                    myActive(currentProcessTaskStepVo);
                    currentProcessTaskStepVo.setIsActive(1);
                    updateProcessTaskStepStatus(currentProcessTaskStepVo);
                    /* 自动处理 **/
                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
                    doNext(ProcessTaskStepOperationType.STEP_HANDLE, new ProcessStepThread(currentProcessTaskStepVo) {
                        @Override
                        public void myExecute() {
                            handler.handle(currentProcessTaskStepVo);
                        }
                    });
                }
            }
        } catch (ProcessTaskException e) {
            logger.error(e.getMessage(), e);
//            currentProcessTaskStepVo.setIsActive(0);
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            currentProcessTaskStepVo.setError(e.getMessage());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            /* 异常提醒 **/
            processStepHandlerCrossoverUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getStartProcessTaskStepId(), e.getMessage(), ProcessTaskStepRemindType.ERROR);
        } finally {
//            if (ProcessTaskStepStatus.FAILED.getValue().equals(currentProcessTaskStepVo.getStatus())) {
//                /*
//                 * 发生异常不能激活当前步骤，执行当前步骤的回退操作
//                 */
//                IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
//                doNext(ProcessTaskOperationType.STEP_BACK, new ProcessStepThread(currentProcessTaskStepVo) {
//                    @Override
//                    public void myExecute() {
//                        handler.back(currentProcessTaskStepVo);
//                    }
//                });
//            }
        }
        return 1;
    }

    protected abstract int myActive(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int assign(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        /* 清空处理人 **/
        Set<ProcessTaskStepWorkerVo> workerSet = new HashSet<>();
        /* 如果已经存在过处理人，则继续使用旧处理人，否则启用分派 **/
        List<ProcessTaskStepUserVo> oldUserList = processTaskCrossoverMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
        if (!oldUserList.isEmpty()) {
            ProcessTaskStepUserVo oldUserVo = oldUserList.get(0);
            ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo(
                    currentProcessTaskStepVo.getProcessTaskId(),
                    currentProcessTaskStepVo.getId(),
                    GroupSearch.USER.getValue(),
                    oldUserVo.getUserUuid(),
                    ProcessUserType.MAJOR.getValue()
            );
            workerSet.add(processTaskStepWorkerVo);
            currentProcessTaskStepVo.setUpdateActiveTime(0);
            currentProcessTaskStepVo.setUpdateEndTime(-1);
        }
        currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.PENDING.getValue());
        int autoStart = myAssign(currentProcessTaskStepVo, workerSet);
        boolean isAssignException = false;
        if (CollectionUtils.isEmpty(workerSet)) {
            /* 获取步骤配置信息 **/
            ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
            ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
            String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
            String defaultWorker = (String) JSONPath.read(stepConfig, "workerPolicyConfig.defaultWorker");
            String[] split = defaultWorker.split("#");
            ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo(
                    currentProcessTaskStepVo.getProcessTaskId(),
                    currentProcessTaskStepVo.getId(),
                    split[0],
                    split[1],
                    ProcessUserType.MAJOR.getValue()
            );
            workerSet.add(processTaskStepWorkerVo);
            isAssignException = true;
        }

        processTaskCrossoverMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
        if (CollectionUtils.isNotEmpty(workerSet)) {
            for (ProcessTaskStepWorkerVo workerVo : workerSet) {
                processTaskCrossoverMapper.insertIgnoreProcessTaskStepWorker(workerVo);
            }
        } else {
            throw new ProcessTaskStepNoMatchedWorkerException();
        }

        if (!oldUserList.isEmpty()) {
            boolean changeMajorUser = true;
            ProcessTaskStepUserVo oldUserVo = oldUserList.get(0);
            for (ProcessTaskStepWorkerVo workerVo : workerSet) {
                if (Objects.equals(workerVo.getUuid(), oldUserVo.getUserUuid())) {
                    changeMajorUser = false;
                    break;
                }
            }
            if (changeMajorUser) {
                oldUserVo.setUserType(ProcessTaskStepUserType.HISTORY_MAJOR.getValue());
                oldUserVo.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
                oldUserVo.setEndTime(new Date());
                processTaskCrossoverMapper.insertIgnoreProcessTaskStepUser(oldUserVo);
            }
            ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
            processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());// 只删除主处理人
            processTaskCrossoverMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
        }
        /* 当只分配到一个用户时，自动设置为处理人，不需要抢单 **/
        if (workerSet.size() == 1 && autoStart == 1) {
            for (ProcessTaskStepWorkerVo workerVo : workerSet) {
                String userUuid = null;
                if (GroupSearch.TEAM.getValue().equals(workerVo.getType())) {
                    List<String> userUuidList = userMapper.getUserUuidListByTeamUuid(workerVo.getUuid());
                    if (userUuidList.size() == 1) {
                        userUuid = userUuidList.get(0);
                    }
                } else if (GroupSearch.ROLE.getValue().equals(workerVo.getType())) {
                    Set<String> userUuidSet = new HashSet<>();
                    List<String> userUuidList = userMapper.getUserUuidListByRoleUuid(workerVo.getUuid());
                    userUuidSet.addAll(userUuidList);
                    if (userUuidSet.size() < 2) {
                        List<RoleTeamVo> roleTeamList = roleMapper.getRoleTeamListByRoleUuid(workerVo.getUuid());
                        if (CollectionUtils.isNotEmpty(roleTeamList)) {
                            List<String> checkedChildrenteamUuidList = new ArrayList<>();
                            List<String> teamUuidList = new ArrayList<>();
                            for (RoleTeamVo roleTeamVo : roleTeamList) {
                                if (Objects.equals(roleTeamVo.getCheckedChildren(), 1)) {
                                    checkedChildrenteamUuidList.add(roleTeamVo.getTeamUuid());
                                } else {
                                    teamUuidList.add(roleTeamVo.getTeamUuid());
                                }
                            }
                            if (CollectionUtils.isNotEmpty(checkedChildrenteamUuidList)) {
                                List<TeamVo> teamList = teamMapper.getTeamByUuidList(checkedChildrenteamUuidList);
                                teamList.sort(Comparator.comparing(TeamVo::getLft));
                                for (TeamVo teamVo : teamList) {
                                    if (!teamUuidList.contains(teamVo.getUuid())) {
                                        teamUuidList.add(teamVo.getUuid());
                                        List<String> childrenUuidList = teamMapper.getChildrenUuidListByLeftRightCode(teamVo.getLft(), teamVo.getRht());
                                        teamUuidList.addAll(childrenUuidList);
                                    }
                                }
                            }
                            userUuidSet.addAll(userMapper.getUserUuidListByTeamUuidListLimitTwo(teamUuidList));
                        }
                        if (userUuidSet.size() == 1) {
                            for (String uuid : userUuidSet) {
                                userUuid = uuid;
                            }
                        }
                    }
                } else if (GroupSearch.USER.getValue().equals(workerVo.getType())) {
                    userUuid = workerVo.getUuid();
                }
                if (StringUtils.isNotBlank(userUuid)) {
                    UserVo userVo = null;
                    if (Objects.equals(SystemUser.SYSTEM.getUserUuid(), userUuid)) {
                        userVo = SystemUser.SYSTEM.getUserVo();
                    } else {
                        userVo = userMapper.getUserBaseInfoByUuidWithoutCache(userUuid);
                    }
                    if (userVo != null) {
                        ProcessTaskStepUserVo processTaskStepUser = new ProcessTaskStepUserVo(
                                currentProcessTaskStepVo.getProcessTaskId(),
                                currentProcessTaskStepVo.getId(),
                                userVo.getUuid(),
                                ProcessUserType.MAJOR.getValue()
                        );
                        processTaskCrossoverMapper.insertProcessTaskStepUser(processTaskStepUser);
                        /* 当步骤设置了自动开始时，设置当前步骤状态为处理中 **/
                        currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.RUNNING.getValue());
                        currentProcessTaskStepVo.setUpdateStartTime(1);
                    }
                }
            }
        } else {
            currentProcessTaskStepVo.setUpdateStartTime(-1);
        }
        /* 触发通知 **/
//        processStepHandlerUtilService.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ASSIGN);
        if (isAssignException) {
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.ASSIGNEXCEPTION);
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.ASSIGNEXCEPTION);
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
    protected abstract int myAssign(ProcessTaskStepVo currentProcessTaskStepVo, Set<ProcessTaskStepWorkerVo> workerSet) throws ProcessTaskException;

    protected int defaultAssign(ProcessTaskStepVo currentProcessTaskStepVo, Set<ProcessTaskStepWorkerVo> workerSet)
            throws ProcessTaskException {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        try {
            IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
            /* 获取步骤配置信息 **/
            ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
            ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
            String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());

            String executeMode = (String) JSONPath.read(stepConfig, "workerPolicyConfig.executeMode");
            Integer autoStart = (Integer) JSONPath.read(stepConfig, "autoStart");
            autoStart = autoStart != null ? autoStart : 1;
            /* 分配处理人 **/
            ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo = new ProcessTaskStepWorkerPolicyVo();
            processTaskStepWorkerPolicyVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            List<ProcessTaskStepWorkerPolicyVo> workerPolicyList = processTaskCrossoverMapper.getProcessTaskStepWorkerPolicy(processTaskStepWorkerPolicyVo);
            if (CollectionUtils.isEmpty(workerPolicyList)) {
                return autoStart;
            }
            Set<ProcessTaskStepWorkerVo> newWorkerSet = new HashSet<>();
            for (ProcessTaskStepWorkerPolicyVo workerPolicyVo : workerPolicyList) {
                IWorkerPolicyHandler workerPolicyHandler = WorkerPolicyHandlerFactory.getHandler(workerPolicyVo.getPolicy());
                if (workerPolicyHandler == null) {
                    continue;
                }
                /* 如果workerList.size()>0，说明已经存在过处理人，分配策略设置只分配一次，则继续使用旧处理人，否则启用分派 **/
                if (Objects.equals(workerPolicyHandler.isOnlyOnceExecute(), 1) && CollectionUtils.isNotEmpty(workerSet)) {
                    continue;
                }
                workerSet.clear();
                List<ProcessTaskStepWorkerVo> tmpWorkerList = workerPolicyHandler.execute(workerPolicyVo, currentProcessTaskStepVo);
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
                        newWorkerSet.addAll(tmpWorkerList);
                        break;
                    } else if ("batch".equals(executeMode)) {
                        newWorkerSet.addAll(tmpWorkerList);
                    }
                }
            }
            workerSet.addAll(newWorkerSet);
            return autoStart;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            processStepHandlerCrossoverUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getId(), e.getMessage(), ProcessTaskStepRemindType.ERROR);
        }
        return 0;
    }

    private void reapprovalRestoreBackup(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        boolean flag = false;
        Long startProcessTaskStepId = currentProcessTaskStepVo.getStartProcessTaskStepId();
        if (startProcessTaskStepId == null) {
            startProcessTaskStepId = currentProcessTaskStepVo.getId();
        }
        List<ProcessTaskStepRelVo> relList = processTaskCrossoverMapper.getProcessTaskStepRelByFromId(startProcessTaskStepId);
        for (ProcessTaskStepRelVo processTaskStepRelVo : relList) {
            if (Objects.equals(processTaskStepRelVo.getIsHit(), 1) && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.BACKWARD.getValue())) {
                ProcessTaskStepVo toProcessTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(processTaskStepRelVo.getToProcessTaskStepId());
                if (Objects.equals(toProcessTaskStepVo.getEnableReapproval(), 1)) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            JSONObject config = new JSONObject();
            ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
            config.put("isActive", processTaskStepVo.getIsActive());
            config.put("status", processTaskStepVo.getStatus());
            List<ProcessTaskConvergeVo> processTaskConvergeList = processTaskCrossoverMapper.getProcessTaskConvergeListByStepId(currentProcessTaskStepVo.getId());
            config.put("convergeList", processTaskConvergeList);
            ProcessTaskStepRelVo toStepRelVo = new ProcessTaskStepRelVo();
            toStepRelVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getFromProcessTaskStepId());
            toStepRelVo.setToProcessTaskStepId(currentProcessTaskStepVo.getId());
            toStepRelVo.setIsHit(1);
            config.put("toStepRelVo", toStepRelVo);

            ProcessTaskStepReapprovalRestoreBackupVo processTaskStepReapprovalRestoreBackupVo = new ProcessTaskStepReapprovalRestoreBackupVo();
            processTaskStepReapprovalRestoreBackupVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            processTaskStepReapprovalRestoreBackupVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskStepReapprovalRestoreBackupVo.setBackupStepId(startProcessTaskStepId);
            processTaskStepReapprovalRestoreBackupVo.setConfig(config);
            Integer maxSort = processTaskCrossoverMapper.getProcessTaskStepReapprovalRestoreBackupMaxSortByBackupStepId(startProcessTaskStepId);
            maxSort = maxSort == null ? 0 : maxSort;
            processTaskStepReapprovalRestoreBackupVo.setSort(maxSort + 1);
            processTaskCrossoverMapper.insertProcessTaskStepReapprovalRestoreBackup(processTaskStepReapprovalRestoreBackupVo);
        }
    }

    /**
     * hang操作原则上不允许出现任何异常，所有异常都必须解决以便流程可以顺利挂起，否则流程可能会卡死在某个节点不能前进或后退
     */
    @Override
    public final int hang(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
            // 锁定当前流程
            processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
            ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
            // 如果当前节点已经挂起，则不再重复操作
            if (Objects.equals(processTaskStepVo.getStatus(), ProcessTaskStepStatus.HANG.getValue()) && Objects.equals(processTaskStepVo.getIsActive(), 0)) {
                return 1;
            }
            reapprovalRestoreBackup(currentProcessTaskStepVo);

            myHang(currentProcessTaskStepVo);
            // 删除当前节点的汇聚记录
            processTaskCrossoverMapper.deleteProcessTaskConvergeByStepId(currentProcessTaskStepVo.getId());

            // 获取流转过的路径
            hangPostStep(currentProcessTaskStepVo);
            ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo();
            processTaskStepWorkerVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            processTaskStepWorkerVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskCrossoverMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
            ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
            processTaskStepUserVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
            processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
            processTaskCrossoverMapper.updateProcessTaskStepUserStatus(processTaskStepUserVo);
            processTaskStepUserVo.setUserType(ProcessUserType.MINOR.getValue());
            processTaskCrossoverMapper.updateProcessTaskStepUserStatus(processTaskStepUserVo);
            currentProcessTaskStepVo.setIsActive(0);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.HANG.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            /* 触发通知 **/
//            processStepHandlerUtilService.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.HANG);

            /* 执行动作 **/
//            processStepHandlerUtilService.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.HANG);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
//            currentProcessTaskStepVo.setIsActive(2);
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
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
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        // 修改当前步骤状态为运行中
        currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.RUNNING.getValue());
        currentProcessTaskStepVo.setUpdateStartTime(1);
        currentProcessTaskStepVo.setUpdateEndTime(-1);
        updateProcessTaskStepStatus(currentProcessTaskStepVo);
        if (!this.isAsync()) {// 同步模式
            try {
                myHandle(currentProcessTaskStepVo);
                /* 如果步骤被标记为全部完成，则触发完成 **/
                if (currentProcessTaskStepVo.getIsAllDone()) {
                    /* 记录时间审计 **/
                    processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_COMPLETE);
                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
                    doNext(ProcessTaskStepOperationType.STEP_COMPLETE, new ProcessStepThread(currentProcessTaskStepVo) {
                        @Override
                        public void myExecute() {
                            handler.complete(currentProcessTaskStepVo);
                        }
                    });
                }
            } catch (ProcessTaskException ex) {
                logger.error(ex.getMessage(), ex);
                currentProcessTaskStepVo.setError(ex.getMessage());
                currentProcessTaskStepVo.setIsActive(1);
                currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
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
                            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_COMPLETE);

                            doNext(ProcessTaskStepOperationType.STEP_COMPLETE, new ProcessStepThread(currentProcessTaskStepVo) {
                                @Override
                                public void myExecute() {
                                    handler.complete(currentProcessTaskStepVo);
                                }
                            });
                        }
                    } catch (ProcessTaskException ex) {
                        logger.error(ex.getMessage(), ex);
                        currentProcessTaskStepVo.setIsActive(1);
                        currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
                        currentProcessTaskStepVo.setError(ex.getMessage());
                        updateProcessTaskStepStatus(currentProcessTaskStepVo);
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        currentProcessTaskStepVo.setIsActive(1);
                        currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
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
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        /* 检查处理人是否合法 **/
        new ProcessAuthManager
                .StepOperationChecker(currentProcessTaskStepVo.getId(), ProcessTaskStepOperationType.STEP_START)
                .build()
                .checkAndNoPermissionThrowException();
        try {
            IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            stepMajorUserRegulate(currentProcessTaskStepVo);
            ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());

            /* 检查步骤是否“已激活” **/
            if (!processTaskStepVo.getIsActive().equals(1)) {
                throw new ProcessTaskStepUnActivedException();
            }
            /* 判断工单步骤状态是否 “未开始” **/
            if (processTaskStepVo.getStatus().equals(ProcessTaskStepStatus.RUNNING.getValue())) {
                throw new ProcessTaskStepRunningException();
            }
            currentProcessTaskStepVo.setIsActive(processTaskStepVo.getIsActive());
            currentProcessTaskStepVo.setStatus(processTaskStepVo.getStatus());
            myStart(currentProcessTaskStepVo);

            /* 更新工单步骤状态为 “进行中” **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.RUNNING.getValue());
            currentProcessTaskStepVo.setUpdateStartTime(1);
            currentProcessTaskStepVo.setUpdateEndTime(-1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());

            currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
            ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_START);
            /* 写入时间审计 **/
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_START);

            /* 计算SLA **/
            processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.START);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.START);

            processTaskCrossoverMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(), ProcessTaskStepRemindType.TRANSFER.getValue()));
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            processStepHandlerCrossoverUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.START);
        }
        return 0;
    }

    private int updateProcessTaskStepStatus(ProcessTaskStepVo currentProcessTaskStepVo) {
        // linbq 2021-10-20 步骤重新激活时，激活时间不变、开始时间、结束时间都会更新
//        if (currentProcessTaskStepVo.getActiveTime() != null) {
//            currentProcessTaskStepVo.setUpdateActiveTime(0);
//        }
//        if (currentProcessTaskStepVo.getStartTime() != null) {
//            currentProcessTaskStepVo.setUpdateStartTime(0);
//        }
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        processTaskCrossoverMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);
        updateProcessTaskStatus(currentProcessTaskStepVo.getProcessTaskId());
        return 1;
    }

    protected abstract int myStart(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int complete(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        /* 锁定当前流程 **/
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        ProcessTaskStepNotifyTriggerType notifyTriggerType = ProcessTaskStepNotifyTriggerType.SUCCEED;
        IOperationType operationType = ProcessTaskStepOperationType.STEP_COMPLETE;
        boolean canComplete = false;
        JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
        String action = paramObj.getString("action");
        if (this.getMode().equals(ProcessStepMode.MT)) {
            if (ProcessTaskStepOperationType.STEP_BACK.getValue().equals(action)) {
                operationType = ProcessTaskStepOperationType.STEP_BACK;
                notifyTriggerType = ProcessTaskStepNotifyTriggerType.BACK;
            }
            canComplete = new ProcessAuthManager
                    .StepOperationChecker(currentProcessTaskStepVo.getId(), operationType)
                    .build()
                    .checkAndNoPermissionThrowException();
            stepMajorUserRegulate(currentProcessTaskStepVo);
        } else if (this.getMode().equals(ProcessStepMode.AT)) {
            canComplete = true;
        }

        if (canComplete) {
            try {
                if (this.getHandler().equals(ProcessStepHandlerType.END.getHandler())) {
                    myComplete(currentProcessTaskStepVo);
                    /* 更新步骤状态 **/
                    currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.SUCCEED.getValue());
                    currentProcessTaskStepVo.setIsActive(2);
                    currentProcessTaskStepVo.setUpdateEndTime(1);
                    currentProcessTaskStepVo.setError(null);
                    updateProcessTaskStepStatus(currentProcessTaskStepVo);
                    processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskNotifyTriggerType.COMPLETEPROCESSTASK);
                    processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskNotifyTriggerType.COMPLETEPROCESSTASK);
                    //根据工单来源执行额外操作
                    ProcessTaskInvokeVo invokeVo = processTaskCrossoverMapper.getInvokeByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
                    if (invokeVo != null) {
                        IProcessTaskSource processTaskSource = ProcessTaskSourceFactory.getHandler(invokeVo.getSource());
                        if (processTaskSource != null) {
                            processTaskSource.complete(currentProcessTaskStepVo);
                        }
                    }
                    ProcessTaskVo processTaskVo = processTaskCrossoverMapper.getProcessTaskById(currentProcessTaskStepVo.getProcessTaskId());
                    if (Objects.equals(processTaskVo.getNeedScore(), 1)) {
                        ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
                        processTaskStepVo.setIsAutoGenerateId(false);
                        processTaskStepVo.setProcessTaskId(processTaskVo.getId());
                        processStepHandlerCrossoverUtil.notify(processTaskStepVo, ProcessTaskNotifyTriggerType.WAITINGSCOREPROCESSTASK);
                        processStepHandlerCrossoverUtil.action(processTaskStepVo, ProcessTaskNotifyTriggerType.WAITINGSCOREPROCESSTASK);
                    }
                    myCompleteAudit(currentProcessTaskStepVo);
                    return 1;
                }

                if (this.getMode().equals(ProcessStepMode.MT)) {
                    myBeforeComplete(currentProcessTaskStepVo);
                    if (operationType == ProcessTaskStepOperationType.STEP_COMPLETE) {
                        String priorityUuid = paramObj.getString("priorityUuid");
                        if (StringUtils.isNotBlank(priorityUuid)) {
                            processTaskCrossoverMapper.updateProcessTaskPriorityUuidById(currentProcessTaskStepVo.getProcessTaskId(), priorityUuid);
                        }
                        processStepHandlerCrossoverUtil.assignWorkerValid(currentProcessTaskStepVo);
                        /* 保存表单属性值 **/
                        processStepHandlerCrossoverUtil.saveForm(currentProcessTaskStepVo);
                    }
                    String majorUserUuid = null;
                    List<ProcessTaskStepUserVo> processTaskStepUserList = processTaskCrossoverMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
                    if (CollectionUtils.isNotEmpty(processTaskStepUserList)) {
                        majorUserUuid = processTaskStepUserList.get(0).getUserUuid();
                    }
                    /* 更新处理人状态 **/
                    ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(
                            currentProcessTaskStepVo.getProcessTaskId(),
                            currentProcessTaskStepVo.getId(),
                            majorUserUuid
                    );
                    // 兼容automatic作业无用户
                    processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
                    processTaskMajorUser.setUserType(ProcessUserType.MAJOR.getValue());
                    processTaskCrossoverMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
                    /* 清空worker表 **/
                    processTaskCrossoverMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
                    processTaskCrossoverMapper.deleteProcessTaskStepReapprovalRestoreBackupByBackupStepId(currentProcessTaskStepVo.getId());
                }
                /* 保存描述内容 **/
                processStepHandlerCrossoverUtil.checkContentIsRequired(currentProcessTaskStepVo);
                processStepHandlerCrossoverUtil.saveContentAndFile(currentProcessTaskStepVo, operationType);
                myComplete(currentProcessTaskStepVo);

                /* 更新步骤状态 **/
                currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.SUCCEED.getValue());
                currentProcessTaskStepVo.setIsActive(2);
                currentProcessTaskStepVo.setUpdateEndTime(1);
                currentProcessTaskStepVo.setError(null);
                updateProcessTaskStepStatus(currentProcessTaskStepVo);

                /* 流转到下一步 **/
                Set<Long> nextStepIdSet = getNext(currentProcessTaskStepVo);
                if (CollectionUtils.isEmpty(nextStepIdSet)) {
                    throw new ProcessTaskNotFoundNextStepException();
                }
                // 完成步骤时将所有后退路线重置为0
                List<ProcessTaskStepRelVo> relList = processTaskCrossoverMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());
                for (ProcessTaskStepRelVo processTaskStepRelVo : relList) {
                    if (Objects.equals(processTaskStepRelVo.getIsHit(), 1) && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.BACKWARD.getValue())) {
                        processTaskStepRelVo.setIsHit(0);
                        processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(processTaskStepRelVo);
                    }
                }
                //将不流转的步骤的正向输入连线的isHit设置为-1
                identifyPostInvalidStepRelIsHit(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), nextStepIdSet);
                List<ProcessTaskStepThread> processTaskStepThreadList = new ArrayList<>();
                List<Long> nextStepIdList = new ArrayList<>(nextStepIdSet);
                List<ProcessTaskStepVo> nextStepList = processTaskCrossoverMapper.getProcessTaskStepListByIdList(nextStepIdList);
                for (ProcessTaskStepVo nextStep : nextStepList) {
                    IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
                    if (nextStepHandler != null) {
                        ProcessTaskStepRelVo processTaskStepRelVo = new ProcessTaskStepRelVo();
                        processTaskStepRelVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                        processTaskStepRelVo.setToProcessTaskStepId(nextStep.getId());
                        processTaskStepRelVo.setIsHit(1);
                        processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(processTaskStepRelVo);
                        nextStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                        nextStep.setStartProcessTaskStepId(currentProcessTaskStepVo.getStartProcessTaskStepId());
                        nextStep.setParallelActivateStepIdList(nextStepIdList);
                        ProcessTaskStepThread thread = new ProcessTaskStepThread(ProcessTaskStepOperationType.STEP_ACTIVE, nextStep, nextStepHandler.getMode()) {
                            @Override
                            protected void myExecute(ProcessTaskStepVo processTaskStepVo) {
                                IProcessStepHandler processStepHandler = ProcessStepHandlerFactory.getHandler(processTaskStepVo.getHandler());
                                if (processStepHandler != null) {
                                    processStepHandler.active(processTaskStepVo);
                                }
                            }
                        };
                        processTaskStepThreadList.add(thread);
//                        doNext(ProcessTaskOperationType.STEP_ACTIVE, new ProcessStepThread(nextStep) {
//                            @Override
//                            public void myExecute() {
//                                nextStepHandler.active(nextStep);
//                            }
//                        });
                    }
                }
                // 默认是向前流转
                ProcessFlowDirection direction = ProcessFlowDirection.FORWARD;
                // 判断是向前流转还是向后回退，向后回退时nextStepIdSet大小一定是1，因为条件节点和分流节点都不能向后回退
                if (nextStepIdSet.size() == 1) {
                    // 找出当前步骤的所有正向的下一个步骤列表，如果nextStepIdSet不包含下一个步骤id，则是向后回退
                    boolean isForward = false;
                    List<ProcessTaskStepVo> toProcessTaskStepList = processTaskCrossoverMapper.getToProcessTaskStepByFromIdAndType(currentProcessTaskStepVo.getId(), ProcessFlowDirection.FORWARD.getValue());
                    for (ProcessTaskStepVo toProcessTaskStep : toProcessTaskStepList) {
                        if (nextStepIdSet.contains(toProcessTaskStep.getId())) {
                            isForward = true;
                            break;
                        }
                    }
                    if (!isForward) {
                        direction = ProcessFlowDirection.BACKWARD;
                    }
                }
                // 如果是向前流转时，需要找出不流转的路经，判断是否有聚合步骤在等待该条路径到达才能前进，如果有，则激活该聚合步骤
                if (direction == ProcessFlowDirection.FORWARD) {
                    List<Long> awaitAdvanceStepIdList = getAwaitAdvanceStepIdList(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
                    awaitAdvanceStepIdList.removeAll(nextStepIdList);
                    if (CollectionUtils.isNotEmpty(awaitAdvanceStepIdList)) {
                        List<ProcessTaskStepVo> awaitAdvanceStepList = processTaskCrossoverMapper.getProcessTaskStepListByIdList(awaitAdvanceStepIdList);
                        for (ProcessTaskStepVo awaitAdvanceStep : awaitAdvanceStepList) {
                            IProcessStepHandler awaitAdvanceStepHandler = ProcessStepHandlerFactory.getHandler(awaitAdvanceStep.getHandler());
                            if (awaitAdvanceStepHandler != null) {
                                awaitAdvanceStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                                awaitAdvanceStep.setStartProcessTaskStepId(currentProcessTaskStepVo.getStartProcessTaskStepId());
                                awaitAdvanceStep.setParallelActivateStepIdList(new ArrayList<>());
                                ProcessTaskStepThread thread = new ProcessTaskStepThread(ProcessTaskStepOperationType.STEP_ACTIVE, awaitAdvanceStep, awaitAdvanceStepHandler.getMode()) {
                                    @Override
                                    protected void myExecute(ProcessTaskStepVo processTaskStepVo) {
                                        IProcessStepHandler processStepHandler = ProcessStepHandlerFactory.getHandler(processTaskStepVo.getHandler());
                                        if (processStepHandler != null) {
                                            processStepHandler.active(processTaskStepVo);
                                        }
                                    }
                                };
                                processTaskStepThreadList.add(thread);
                            }
                        }
                    }
                }
                doNext(processTaskStepThreadList);

                /* 触发通知 **/
                processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, notifyTriggerType);
                /* 执行动作 **/
                processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, notifyTriggerType);

                /* 回退提醒 **/
                processTaskCrossoverMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId()));
                if (this.getMode().equals(ProcessStepMode.MT)) {
                    if (ProcessTaskStepOperationType.STEP_BACK.getValue().equals(action)) {
//                        processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId()));
                        processStepHandlerCrossoverUtil.saveStepRemind(currentProcessTaskStepVo, paramObj.getLong("nextStepId"), paramObj.getString("content"), ProcessTaskStepRemindType.BACK);
                    }
                }
            } catch (ProcessTaskException ex) {
                logger.error(ex.getMessage(), ex);
                currentProcessTaskStepVo.setError(ex.getMessage());
//                currentProcessTaskStepVo.setIsActive(0);
                currentProcessTaskStepVo.setIsActive(1);
                currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
                updateProcessTaskStepStatus(currentProcessTaskStepVo);
                /* 异常提醒 **/
                processStepHandlerCrossoverUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getStartProcessTaskStepId(), ex.getMessage(), ProcessTaskStepRemindType.ERROR);
                /* 触发通知 **/
                processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.FAILED);
                /* 执行动作 **/
                processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.FAILED);
            } finally {
//                if (Objects.equals(currentProcessTaskStepVo.getStatus(), ProcessTaskStepStatus.FAILED.getValue())) {
//                    /* 发生异常不能完成当前步骤，执行当前步骤的回退操作 **/
//                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
//                    doNext(ProcessTaskOperationType.STEP_BACK, new ProcessStepThread(currentProcessTaskStepVo) {
//                        @Override
//                        public void myExecute() {
//                            handler.back(currentProcessTaskStepVo);
//                        }
//                    });
//                }

                myCompleteAudit(currentProcessTaskStepVo);
                if (this.getMode().equals(ProcessStepMode.MT)) {
                    currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
                    ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, operationType);
                    /* 写入时间审计 **/
                    processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, operationType);
                    /* 计算SLA **/
                    processStepHandlerCrossoverUtil.calculateSla(new ProcessTaskVo(currentProcessTaskStepVo.getProcessTaskId()));
                }
            }
        }

        return 1;
    }

    /**
     * 获取等待前进的聚合步骤列表
     */
    private List<Long> getAwaitAdvanceStepIdList(Long processTaskId, Long currentProcessTaskStepId) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        List<ProcessTaskStepRelVo> allProcessTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelByProcessTaskId(processTaskId);
        return doGetAwaitAdvanceStepIdList(currentProcessTaskStepId, allProcessTaskStepRelList, new ArrayList<>());
    }

    private List<Long> doGetAwaitAdvanceStepIdList(Long currentProcessTaskStepId, List<ProcessTaskStepRelVo> allProcessTaskStepRelList, List<Long> passedStepIdList) {
        List<Long> awaitAdvanceStepIdList = new ArrayList<>();
        if (passedStepIdList.contains(currentProcessTaskStepId)) {
            return awaitAdvanceStepIdList;
        }
        passedStepIdList.add(currentProcessTaskStepId);
        // 失效步骤ID列表
        List<Long> invalidStepIdList = new ArrayList<>();
        for (ProcessTaskStepRelVo processTaskStepRelVo : allProcessTaskStepRelList) {
            if (Objects.equals(processTaskStepRelVo.getFromProcessTaskStepId(), currentProcessTaskStepId)
                    && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                if (Objects.equals(processTaskStepRelVo.getIsHit(), -1)) {
                    invalidStepIdList.add(processTaskStepRelVo.getToProcessTaskStepId());
                }
            }
        }
        // 需要进一步下探的步骤ID列表
        List<Long> needExploreStepIdList = new ArrayList<>();
        for (Long invalidStepId : invalidStepIdList) {
            int isHit0Count = 0;
            int isHit1Count = 0;
            for (ProcessTaskStepRelVo processTaskStepRelVo : allProcessTaskStepRelList) {
                if (Objects.equals(processTaskStepRelVo.getToProcessTaskStepId(), invalidStepId)
                        && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                    if (Objects.equals(processTaskStepRelVo.getIsHit(), 0)) {
                        isHit0Count++;
                        break;
                    } else if (Objects.equals(processTaskStepRelVo.getIsHit(), 1)) {
                        isHit1Count++;
                    }
                }
            }
            if (isHit0Count > 0) {
                continue;
            }
            if (isHit1Count > 0) {
                awaitAdvanceStepIdList.add(invalidStepId);
            } else {
                needExploreStepIdList.add(invalidStepId);
            }
        }
        for (Long stepId : needExploreStepIdList) {
            List<Long> list = doGetAwaitAdvanceStepIdList(stepId, allProcessTaskStepRelList, passedStepIdList);
            awaitAdvanceStepIdList.addAll(list);
        }
        return awaitAdvanceStepIdList;
    }

    protected abstract int myComplete(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    protected int myBeforeComplete(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException {
        return 0;
    }

    /*
    节点完成时写入时间线信息
     */
    protected abstract int myCompleteAudit(ProcessTaskStepVo currentProcessTaskStepVo);

    @Override
    public final int autoComplete(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
        //未激活的步骤不能自动完成
        if (!Objects.equals(processTaskStepVo.getIsActive(), 1)) {
            return 0;
        }
        //如果步骤没有开始就先自动开始
        if (ProcessTaskStepStatus.PENDING.getValue().equals(processTaskStepVo.getStatus())) {
//            this.accept(currentProcessTaskStepVo);
            this.start(currentProcessTaskStepVo);
        }
        this.complete(currentProcessTaskStepVo);
        return 1;
    }

    @Override
    public final int reapproval(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        /* 锁定当前流程 **/
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        new ProcessAuthManager
                .StepOperationChecker(currentProcessTaskStepVo.getId(), ProcessTaskStepOperationType.STEP_REAPPROVAL)
                .build()
                .checkAndNoPermissionThrowException();
        Long needActiveStepId = null;
        List<ProcessTaskStepRelVo> processTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());
        for (ProcessTaskStepRelVo processTaskStepRelVo : processTaskStepRelList) {
            if (Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.BACKWARD.getValue()) && Objects.equals(processTaskStepRelVo.getIsHit(), 1)) {
                needActiveStepId = processTaskStepRelVo.getFromProcessTaskStepId();
            }
        }
        if (needActiveStepId != null) {
            List<ProcessTaskStepReapprovalRestoreBackupVo> processTaskStepReapprovalRestoreBackupList = processTaskCrossoverMapper.getProcessTaskStepReapprovalRestoreBackupListByBackupStepId(needActiveStepId);
            for (ProcessTaskStepReapprovalRestoreBackupVo processTaskStepReapprovalRestoreBackupVo : processTaskStepReapprovalRestoreBackupList) {
                JSONObject config = processTaskStepReapprovalRestoreBackupVo.getConfig();
                Integer isActive = config.getInteger("isActive");
                String status = config.getString("status");
                ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
                processTaskStepVo.setProcessTaskId(processTaskStepReapprovalRestoreBackupVo.getProcessTaskId());
                processTaskStepVo.setId(processTaskStepReapprovalRestoreBackupVo.getProcessTaskStepId());
                processTaskStepVo.setIsActive(isActive);
                processTaskStepVo.setStatus(status);
                updateProcessTaskStepStatus(processTaskStepVo);

                JSONArray convergeArray = config.getJSONArray("convergeList");
                if (CollectionUtils.isNotEmpty(convergeArray)) {
                    List<ProcessTaskConvergeVo> processTaskConvergeList = convergeArray.toJavaList(ProcessTaskConvergeVo.class);
                    for (ProcessTaskConvergeVo processTaskConvergeVo : processTaskConvergeList) {
                        processTaskCrossoverMapper.insertIgnoreProcessTaskConverge(processTaskConvergeVo);
                    }
                }
                JSONObject toStepRelObj = config.getJSONObject("toStepRelVo");
                if (MapUtils.isNotEmpty(toStepRelObj)) {
                    ProcessTaskStepRelVo toStepRelVo = toStepRelObj.toJavaObject(ProcessTaskStepRelVo.class);
                    processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(toStepRelVo);
                }
            }

            processTaskCrossoverMapper.deleteProcessTaskStepReapprovalRestoreBackupByBackupStepId(needActiveStepId);
            try {
                JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
                if (this.getMode().equals(ProcessStepMode.MT)) {
//                    if (operationType == ProcessTaskOperationType.STEP_COMPLETE) {
                    String priorityUuid = paramObj.getString("priorityUuid");
                    if (StringUtils.isNotBlank(priorityUuid)) {
                        processTaskCrossoverMapper.updateProcessTaskPriorityUuidById(currentProcessTaskStepVo.getProcessTaskId(), priorityUuid);
                    }
                    /* 保存表单属性值 **/
                    processStepHandlerCrossoverUtil.saveForm(currentProcessTaskStepVo);
//                    }

                    String majorUserUuid = null;
                    List<ProcessTaskStepUserVo> processTaskStepUserList = processTaskCrossoverMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
                    if (CollectionUtils.isNotEmpty(processTaskStepUserList)) {
                        majorUserUuid = processTaskStepUserList.get(0).getUserUuid();
                    }
                    /* 更新处理人状态 **/
                    ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(
                            currentProcessTaskStepVo.getProcessTaskId(),
                            currentProcessTaskStepVo.getId(),
                            majorUserUuid
                    );
                    // 兼容automatic作业无用户
                    processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
                    processTaskMajorUser.setUserType(ProcessUserType.MAJOR.getValue());
                    processTaskCrossoverMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
                    /* 清空worker表 **/
                    processTaskCrossoverMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
                }
                /* 保存描述内容 **/
                processStepHandlerCrossoverUtil.checkContentIsRequired(currentProcessTaskStepVo);
                processStepHandlerCrossoverUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_REAPPROVAL);
                myReapproval(currentProcessTaskStepVo);

                /* 更新步骤状态 **/
                currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.SUCCEED.getValue());
                currentProcessTaskStepVo.setIsActive(2);
                currentProcessTaskStepVo.setUpdateEndTime(1);
                currentProcessTaskStepVo.setError(null);
                updateProcessTaskStepStatus(currentProcessTaskStepVo);

                /* 流转到下一步 **/
                ProcessTaskStepVo nextStep = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(needActiveStepId);
                IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
                if (nextStepHandler == null) {
                    throw new ProcessStepUtilHandlerNotFoundException(nextStep.getHandler());
                }
//                Long startProcessTaskStepId = currentProcessTaskStepVo.getStartProcessTaskStepId();
//                if (startProcessTaskStepId == null) {
//                    startProcessTaskStepId = currentProcessTaskStepVo.getId();
//                }
                // 完成步骤时将所有后退路线重置为0
                List<ProcessTaskStepRelVo> relList = processTaskCrossoverMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());
                for (ProcessTaskStepRelVo processTaskStepRelVo : relList) {
                    if (Objects.equals(processTaskStepRelVo.getIsHit(), 1) && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.BACKWARD.getValue())) {
                        processTaskStepRelVo.setIsHit(0);
                        processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(processTaskStepRelVo);
                    }
                }
                doNext(ProcessTaskStepOperationType.STEP_ACTIVE, new ProcessStepThread(nextStep) {
                    @Override
                    public void myExecute() {
                        nextStepHandler.active(nextStep);
                    }
                });

                /* 触发通知 **/
                processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.SUCCEED);
                /* 执行动作 **/
                processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.SUCCEED);

                /* 回退提醒 **/
                processTaskCrossoverMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId()));
//                if (this.getMode().equals(ProcessStepMode.MT)) {
//                    if (ProcessTaskOperationType.STEP_BACK.getValue().equals(action)) {
//                        processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId()));
//                        IProcessStepHandlerUtil.saveStepRemind(currentProcessTaskStepVo, paramObj.getLong("nextStepId"), paramObj.getString("content"), ProcessTaskStepRemindType.BACK);
//                    }
//                }
            } catch (ProcessTaskException ex) {
                logger.error(ex.getMessage(), ex);
                currentProcessTaskStepVo.setError(ex.getMessage());
//                currentProcessTaskStepVo.setIsActive(0);
                currentProcessTaskStepVo.setIsActive(1);
                currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
                updateProcessTaskStepStatus(currentProcessTaskStepVo);
                /* 异常提醒 **/
                processStepHandlerCrossoverUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getStartProcessTaskStepId(), ex.getMessage(), ProcessTaskStepRemindType.ERROR);
                /* 触发通知 **/
                processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.FAILED);
                /* 执行动作 **/
                processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.FAILED);
            } finally {
//                if (ProcessTaskStepStatus.FAILED.getValue().equals(currentProcessTaskStepVo.getStatus())) {
//                    /* 发生异常不能完成当前步骤，执行当前步骤的回退操作 **/
//                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
//                    doNext(ProcessTaskOperationType.STEP_BACK, new ProcessStepThread(currentProcessTaskStepVo) {
//                        @Override
//                        public void myExecute() {
//                            handler.back(currentProcessTaskStepVo);
//                        }
//                    });
//                }

                myReapprovalAudit(currentProcessTaskStepVo);
                if (this.getMode().equals(ProcessStepMode.MT)) {
                    currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
                    ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_REAPPROVAL);
                    /* 写入时间审计 **/
                    processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_REAPPROVAL);
                    /* 计算SLA **/
                    processStepHandlerCrossoverUtil.calculateSla(new ProcessTaskVo(currentProcessTaskStepVo.getProcessTaskId()));
                }
            }
        }
        return 1;
    }

    protected abstract int myReapproval(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    protected abstract int myReapprovalAudit(ProcessTaskStepVo currentProcessTaskStepVo);

    @Override
    public final int retreat(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        new ProcessAuthManager
                .StepOperationChecker(currentProcessTaskStepVo.getId(), ProcessTaskStepOperationType.STEP_RETREAT)
                .build()
                .checkAndNoPermissionThrowException();
        try {
            stepMajorUserRegulate(currentProcessTaskStepVo);
            /* 设置当前步骤状态为未开始 **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.PENDING.getValue());
            /* 保存撤回原因 **/
            processStepHandlerCrossoverUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.PROCESSTASK_RETREAT);
            myRetreat(currentProcessTaskStepVo);

            /* 遍历后续节点所有步骤，写入汇聚步骤数据 **/
            resetConvergeInfo(currentProcessTaskStepVo);

            /* 如果当前步骤是二次进入(后续路径已经走过)，则需要对所有后续流转过的步骤都进行挂起操作 **/
            hangPostStep(currentProcessTaskStepVo);
            resetPostStepRelIsHit(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
            /* 获取当前步骤状态 **/

            /* 分配处理人 **/
            assign(currentProcessTaskStepVo);
            currentProcessTaskStepVo.setIsActive(1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);

            currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
            ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_RETREAT);
            /* 写入时间审计 **/
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_RETREAT);
            if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStepStatus.RUNNING.getValue())) {
                processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_START);
            }

            /* 计算SLA并触发超时警告 **/
            processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.RETREAT);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.RETREAT);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            processStepHandlerCrossoverUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.RETREAT);
        }
        return 1;
    }

    protected abstract int myRetreat(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int abortProcessTask(ProcessTaskVo currentProcessTaskVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
        /* 校验权限 **/
        new ProcessAuthManager
                .TaskOperationChecker(currentProcessTaskVo.getId(), ProcessTaskOperationType.PROCESSTASK_ABORT)
                .build()
                .checkAndNoPermissionThrowException();

        List<ProcessTaskStepVo> processTaskStepList = processTaskCrossoverMapper.getProcessTaskStepBaseInfoByProcessTaskId(currentProcessTaskVo.getId());
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
        processStepHandlerCrossoverUtil.saveProcessTaskOperationContent(currentProcessTaskVo, ProcessTaskOperationType.PROCESSTASK_ABORT);
        /* 更新流程作业状态 **/
        updateProcessTaskStatus(currentProcessTaskVo.getId());

        /* 处理历史记录 **/
        ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
        processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
        processTaskStepVo.getParamObj().putAll(currentProcessTaskVo.getParamObj());
        processStepHandlerCrossoverUtil.audit(processTaskStepVo, ProcessTaskAuditType.ABORTPROCESSTASK);
        /* 触发通知 **/
        processStepHandlerCrossoverUtil.notify(processTaskStepVo, ProcessTaskNotifyTriggerType.ABORTPROCESSTASK);
        /* 执行动作 **/
        processStepHandlerCrossoverUtil.action(processTaskStepVo, ProcessTaskNotifyTriggerType.ABORTPROCESSTASK);
        return 1;
    }

    @Override
    public int abort(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        /* 组件完成动作 **/
        myAbort(currentProcessTaskStepVo);

        /* 清空worker表，确保没人可以处理当前步骤 **/
        processTaskCrossoverMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));

        /* 修改步骤状态 **/
        currentProcessTaskStepVo.setIsActive(-1);
        processTaskCrossoverMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);

        /* 写入时间审计 **/
        processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.PROCESSTASK_ABORT);

        /* 计算SLA **/
        processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);

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
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
        /* 校验权限 **/
        new ProcessAuthManager
                .TaskOperationChecker(currentProcessTaskVo.getId(), ProcessTaskOperationType.PROCESSTASK_RECOVER)
                .build()
                .checkAndNoPermissionThrowException();

        List<ProcessTaskStepVo> processTaskStepList = processTaskCrossoverMapper.getProcessTaskStepBaseInfoByProcessTaskId(currentProcessTaskVo.getId());
        for (ProcessTaskStepVo stepVo : processTaskStepList) {
            /* 找到所有已终止步骤，执行终止操作 **/
            if (stepVo.getIsActive().equals(-1)) {
                IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(stepVo.getHandler());
                if (handler != null) {
                    handler.recover(stepVo);
                }
            }
        }

        processStepHandlerCrossoverUtil.saveProcessTaskOperationContent(currentProcessTaskVo, ProcessTaskOperationType.PROCESSTASK_RECOVER);
        /* 更新流程作业状态 **/
        updateProcessTaskStatus(currentProcessTaskVo.getId());

        /* 处理历史记录 **/
        ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
        processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
        processTaskStepVo.getParamObj().put("source", currentProcessTaskVo.getParamObj().getString("source"));
        processTaskStepVo.getParamObj().put("content", currentProcessTaskVo.getParamObj().getString("content"));
        processStepHandlerCrossoverUtil.audit(processTaskStepVo, ProcessTaskAuditType.RECOVERPROCESSTASK);
        /* 触发通知 **/
        processStepHandlerCrossoverUtil.notify(processTaskStepVo, ProcessTaskNotifyTriggerType.RECOVERPROCESSTASK);
        /* 执行动作 **/
        processStepHandlerCrossoverUtil.action(processTaskStepVo, ProcessTaskNotifyTriggerType.RECOVERPROCESSTASK);
        return 1;
    }

    @Override
    public int recover(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        // 获取步骤基本信息
//        currentProcessTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
        if (currentProcessTaskStepVo.getIsActive().equals(-1)) {
            /* 组件完成动作 **/
            myRecover(currentProcessTaskStepVo);
            currentProcessTaskStepVo.setIsActive(1);
            /* 如果已经存在过处理人，则继续使用旧处理人，否则重新分派 **/
            Set<ProcessTaskStepWorkerVo> workerSet = new HashSet<>();
            List<ProcessTaskStepUserVo> oldUserList = processTaskCrossoverMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
            if (!oldUserList.isEmpty()) {
                ProcessTaskStepUserVo oldUserVo = oldUserList.get(0);
                ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo(
                        currentProcessTaskStepVo.getProcessTaskId(),
                        currentProcessTaskStepVo.getId(),
                        GroupSearch.USER.getValue(),
                        oldUserVo.getUserUuid(),
                        ProcessUserType.MAJOR.getValue()
                );
                workerSet.add(processTaskStepWorkerVo);
            } else {
                try {
                    myAssign(currentProcessTaskStepVo, workerSet);
                } catch (ProcessTaskException e) {
                    logger.error(e.getMessage(), e);
//                    currentProcessTaskStepVo.setIsActive(-1);
                    currentProcessTaskStepVo.setIsActive(1);
                    currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
                    currentProcessTaskStepVo.setError(e.getMessage());
                }
            }
            for (ProcessTaskStepWorkerVo worker : workerSet) {
                processTaskCrossoverMapper.insertIgnoreProcessTaskStepWorker(worker);
            }
            IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler != null) {
                processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
            }

            /* 修改步骤状态 **/
            processTaskCrossoverMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } else if (currentProcessTaskStepVo.getIsActive().equals(1) && ProcessTaskStepStatus.HANG.getValue().equals(currentProcessTaskStepVo.getStatus())) {
            IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            /* 检查处理人是否合法 **/
            new ProcessAuthManager
                    .StepOperationChecker(currentProcessTaskStepVo.getId(), ProcessTaskStepOperationType.STEP_RECOVER)
                    .build()
                    .checkAndNoPermissionThrowException();

            stepMajorUserRegulate(currentProcessTaskStepVo);
            processStepHandlerCrossoverUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_RECOVER);
            myRecover(currentProcessTaskStepVo);

            /* 更新工单步骤状态为 “进行中” **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.RUNNING.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);

            List<ProcessTaskStepUserVo> processTaskStepUserList = processTaskCrossoverMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
            if (CollectionUtils.isNotEmpty(processTaskStepUserList)) {
                String userUuid = processTaskStepUserList.get(0).getUserUuid();
                ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo();
                processTaskStepWorkerVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
                processTaskStepWorkerVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
                processTaskStepWorkerVo.setUserType(ProcessUserType.MAJOR.getValue());
                processTaskStepWorkerVo.setUuid(userUuid);
                processTaskStepWorkerVo.setType(GroupSearch.USER.getValue());
                processTaskCrossoverMapper.insertIgnoreProcessTaskStepWorker(processTaskStepWorkerVo);
            }

            processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
            currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
            ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_RECOVER);
            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.RECOVER);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.RECOVER);
            processStepHandlerCrossoverUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.RECOVER);
        }

        /* 写入时间审计 **/
        processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.PROCESSTASK_RECOVER);

        /* 计算SLA **/
        processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);
        return 1;
    }

    /**
     * 由于recover属于批量自动操作，不抛出任何受检异常
     **/
    protected abstract int myRecover(ProcessTaskStepVo currentProcessTaskStepVo);

    @Override
    public int pause(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        /* 检查处理人是否合法 **/
        new ProcessAuthManager
                .StepOperationChecker(currentProcessTaskStepVo.getId(), ProcessTaskStepOperationType.STEP_PAUSE)
                .build()
                .checkAndNoPermissionThrowException();
        try {
            IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(this.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            stepMajorUserRegulate(currentProcessTaskStepVo);
            processStepHandlerCrossoverUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_PAUSE);
            myPause(currentProcessTaskStepVo);

            /* 更新工单步骤状态为 “已挂起” **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.HANG.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            /* 判断主处理人是否有步骤暂停权限，如果没有需要将`processtask_step_worker`中主处理人数据删除 **/
            boolean majorUserHasStepPauseAuth = false;
            List<ProcessTaskStepUserVo> processTaskStepUserList = processTaskCrossoverMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
            if (CollectionUtils.isNotEmpty(processTaskStepUserList)) {
                String userUuid = processTaskStepUserList.get(0).getUserUuid();
                majorUserHasStepPauseAuth = new ProcessAuthManager
                        .StepOperationChecker(currentProcessTaskStepVo.getId(), ProcessTaskStepOperationType.STEP_RECOVER)
                        .withUserUuid(userUuid)
                        .build()
                        .check();
            }
            if (!majorUserHasStepPauseAuth) {
                ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo();
                processTaskStepWorkerVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
                processTaskStepWorkerVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
                processTaskStepWorkerVo.setUserType(ProcessUserType.MAJOR.getValue());
                processTaskCrossoverMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
            }

            processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
            currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
            ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_PAUSE);
            /* 写入时间审计 **/
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_PAUSE);

            /* 计算SLA **/
            processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.PAUSE);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.PAUSE);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            processStepHandlerCrossoverUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.PAUSE);
        }
        return 1;
    }

    protected abstract int myPause(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int accept(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

        /* 校验权限 **/
        new ProcessAuthManager
                .StepOperationChecker(currentProcessTaskStepVo.getId(), ProcessTaskStepOperationType.STEP_ACCEPT)
                .build()
                .checkAndNoPermissionThrowException();
        try {
            stepMajorUserRegulate(currentProcessTaskStepVo);
            /* 清空worker表，只留下当前处理人 **/
            processTaskCrossoverMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
            ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo(
                    currentProcessTaskStepVo.getProcessTaskId(),
                    currentProcessTaskStepVo.getId(),
                    GroupSearch.USER.getValue(),
                    UserContext.get().getUserUuid(true),
                    ProcessUserType.MAJOR.getValue()
            );
            processTaskCrossoverMapper.insertIgnoreProcessTaskStepWorker(processTaskStepWorkerVo);

            /* 删除user表主处理人，更换为当前处理人 **/
            ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
            processTaskStepUserVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
//            processTaskStepUserVo.setUserVo(new UserVo(UserContext.get().getUserUuid(true)));
            processTaskStepUserVo.setUserUuid(UserContext.get().getUserUuid(true));
            processTaskStepUserVo.setUserName(UserContext.get().getUserName());
            processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
            processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());
            processTaskCrossoverMapper.insertProcessTaskStepUser(processTaskStepUserVo);

            currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
            ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_ACCEPT);
            /* 处理历史记录 **/
            // processStepHandlerUtilService.audit(currentProcessTaskStepVo, ProcessTaskStepAction.ACCEPT);

            /* 触发通知 **/
            // processStepHandlerUtilService.notify(currentProcessTaskStepVo, NotifyTriggerType.ACCEPT);
            processTaskCrossoverMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(), ProcessTaskStepRemindType.TRANSFER.getValue()));
        } catch (ProcessTaskRuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        }
        return 0;
    }

    @Override
    public final int transfer(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList) {

        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        if (CollectionUtils.isEmpty(workerList)) {
            throw new ProcessTaskStepWorkerIsRequiredException();
        }
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

        /* 校验权限 **/
        new ProcessAuthManager
                .StepOperationChecker(currentProcessTaskStepVo.getId(), ProcessTaskStepOperationType.STEP_TRANSFER)
                .build()
                .checkAndNoPermissionThrowException();

        ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
        processTaskStepVo.getParamObj().putAll(currentProcessTaskStepVo.getParamObj());
        /* 检查步骤是否 “已激活” **/
        if (!processTaskStepVo.getIsActive().equals(1)) {
            throw new ProcessTaskStepUnActivedException();
        }

        try {
            List<ProcessTaskStepUserVo> oldUserList = processTaskCrossoverMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
            if (CollectionUtils.isNotEmpty(oldUserList)) {
                List<String> workerUserUuidList = new ArrayList<>();
                for (ProcessTaskStepWorkerVo workerVo : workerList) {
                    if (GroupSearch.USER.getValue().equals(workerVo.getType()) && StringUtils.isNotBlank(workerVo.getUuid())) {
                        workerUserUuidList.add(workerVo.getUuid());
                    }
                }
                for (ProcessTaskStepUserVo oldUser : oldUserList) {
                    /* 更新处理人状态 **/
                    oldUser.setUserType(ProcessTaskStepUserType.HISTORY_MAJOR.getValue());
                    if (Objects.equals(oldUser.getUserUuid(), UserContext.get().getUserUuid(true))) {
                        oldUser.setStatus(ProcessTaskStepUserStatus.TRANSFERRED.getValue());
                    } else {
                        oldUser.setStatus(ProcessTaskStepUserStatus.SOMEONE_TRANSFERRED.getValue());
                    }
                    oldUser.setEndTime(new Date());
                    processTaskCrossoverMapper.insertIgnoreProcessTaskStepUser(oldUser);
                    if (workerUserUuidList.contains(oldUser.getUserUuid())) {
                        String userName = oldUser.getUserName();
                        if (StringUtils.isBlank(userName)) {
                            UserVo userVo = userMapper.getUserBaseInfoByUuid(oldUser.getUserUuid());
                            if (userVo != null) {
                                userName = userVo.getUserName();
                            }
                        }
                        throw new ProcessTaskStepUserIsExistsException(userName);
                    }
                }
                /* 清空user表 **/
                ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
                processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
                processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
                processTaskCrossoverMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
            }
            /* 保存表单属性值 **/
            processStepHandlerCrossoverUtil.saveForm(currentProcessTaskStepVo);
            /* 保存描述内容 **/
            JSONArray contentTargetList = new JSONArray();
            for (ProcessTaskStepWorkerVo workerVo : workerList) {
                JSONObject contentTargetObj = new JSONObject();
                contentTargetObj.put("type", workerVo.getType());
                contentTargetObj.put("uuid", workerVo.getUuid());
                contentTargetList.add(contentTargetObj);
            }
            currentProcessTaskStepVo.getParamObj().put("contentTargetList", contentTargetList);
            processStepHandlerCrossoverUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_TRANSFER);

            /* 根据子类需要把最终处理人放进来，引擎将自动写入数据库，也可能为空，例如一些特殊的流程节点 **/
            processTaskStepVo.setStatus(ProcessTaskStepStatus.PENDING.getValue());
            myTransfer(processTaskStepVo, workerList);

            ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId());
            /* 获取步骤配置信息 **/
            ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
            String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
            Integer autoStart = (Integer) JSONPath.read(stepConfig, "autoStart");
            autoStart = autoStart != null ? autoStart : 1;
            /* 当只分配到一个用户时，自动设置为处理人，不需要抢单 **/
            if (workerList.size() == 1 && autoStart == 1) {
                if (StringUtils.isNotBlank(workerList.get(0).getUuid()) && GroupSearch.USER.getValue().equals(workerList.get(0).getType())) {
                    ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(
                            currentProcessTaskStepVo.getProcessTaskId(),
                            currentProcessTaskStepVo.getId(),
                            workerList.get(0).getUuid(),
                            ProcessUserType.MAJOR.getValue()
                    );
                    processTaskCrossoverMapper.insertProcessTaskStepUser(processTaskStepUserVo);
                    processTaskStepWorkerVo.setUserType(ProcessUserType.MAJOR.getValue());
                    processTaskStepVo.setStatus(ProcessTaskStepStatus.RUNNING.getValue());
                }
            }
            JSONArray oldWorkerList = new JSONArray();
            List<ProcessTaskStepWorkerVo> processTaskStepWorkerList = processTaskCrossoverMapper.getProcessTaskStepWorkerByProcessTaskIdAndProcessTaskStepId(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
            for (ProcessTaskStepWorkerVo workerVo : processTaskStepWorkerList) {
                if (Objects.equals(workerVo.getUserType(), ProcessUserType.MAJOR.getValue())) {
                    oldWorkerList.add(workerVo.getType() + "#" + workerVo.getUuid());
                }
            }
            currentProcessTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.WORKERLIST.getOldDataParamName(), oldWorkerList);
            /* 清空work表，重新写入新数据 **/
            processTaskCrossoverMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
            for (ProcessTaskStepWorkerVo workerVo : workerList) {
                processTaskCrossoverMapper.insertIgnoreProcessTaskStepWorker(workerVo);
            }

            updateProcessTaskStepStatus(processTaskStepVo);
            myAfterTransfer(processTaskStepVo);

            currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
            ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_TRANSFER);

            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.TRANSFER);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.TRANSFER);

            /* 处理时间审计 **/
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.PROCESSTASK_TRANSFER);
            /* 转交提醒 **/

            processTaskCrossoverMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(), ProcessTaskStepRemindType.TRANSFER.getValue()));
            processStepHandlerCrossoverUtil.saveStepRemind(
                    currentProcessTaskStepVo,
                    currentProcessTaskStepVo.getId(),
                    currentProcessTaskStepVo.getParamObj().getString("content"),
                    ProcessTaskStepRemindType.TRANSFER
            );
        } catch (ProcessTaskException e) {
            logger.error(e.getMessage(), e);
            processTaskStepVo.setError(e.getMessage());
            processTaskStepVo.setIsActive(1);
            processTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            updateProcessTaskStepStatus(processTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            processStepHandlerCrossoverUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.TRANSFER);
        }

        return 0;
    }

    protected abstract int myTransfer(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList) throws ProcessTaskException;

    protected void myAfterTransfer(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException {
    }

    /**
     * back操作不允许出现任何异常，所有异常都必须解决以便流程可以顺利回退，否则流程可能会卡死在某个节点不能前进或后退
     */
    @Override
    public final int back(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        try {
            IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
            // 锁定当前流程
            processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

            myBack(currentProcessTaskStepVo);

            // 获取来源路径
            List<ProcessTaskStepRelVo> fromProcessTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());

            for (ProcessTaskStepRelVo processTaskStepRelVo : fromProcessTaskStepRelList) {
                // 沿着流转过的路径向前找节点并重新激活
                if (!processTaskStepRelVo.getIsHit().equals(0)) {
                    ProcessTaskStepVo fromProcessTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(processTaskStepRelVo.getFromProcessTaskStepId());
                    if (fromProcessTaskStepVo != null) {
                        // 如果是分流节点或条件节点，则再次调用back查找上一个处理节点
                        if (fromProcessTaskStepVo.getHandler().equals(ProcessStepHandlerType.DISTRIBUTARY.getHandler())
                                || fromProcessTaskStepVo.getHandler().equals(ProcessStepHandlerType.CONDITION.getHandler())) {
                            IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(fromProcessTaskStepVo.getHandler());
                            if (handler != null) {
                                fromProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                                doNext(ProcessTaskStepOperationType.STEP_BACK, new ProcessStepThread(fromProcessTaskStepVo) {
                                    @Override
                                    public void myExecute() {
                                        handler.back(fromProcessTaskStepVo);
                                    }
                                });
                            }
                        } else {
                            // 如果是处理节点，则重新激活
                            IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(fromProcessTaskStepVo.getHandler());
                            if (handler != null) {
                                fromProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                                doNext(ProcessTaskStepOperationType.STEP_ACTIVE, new ProcessStepThread(fromProcessTaskStepVo) {
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
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_BACK);

            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.BACK);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.BACK);
        } catch (ProcessTaskException e) {
            logger.error(e.getMessage(), e);
            currentProcessTaskStepVo.setError(e.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        }
        /* 处理历史记录 **/
        // processStepHandlerUtilService.audit(currentProcessTaskStepVo, ProcessTaskStepAction.BACK);

        /* 计算SLA **/
        processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);
        return 1;
    }

    protected abstract int myBack(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    @Override
    public final int saveDraft(ProcessTaskStepVo currentProcessTaskStepVo, Long newProcessTaskId) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
        Long processTaskId = currentProcessTaskStepVo.getProcessTaskId();
        ProcessTaskVo processTaskVo;
        if (processTaskId == null) {// 首次保存
            processTaskVo = new ProcessTaskVo();
            if (newProcessTaskId != null) {
                processTaskVo.setId(newProcessTaskId);
            }
            processTaskVo.setTitle(paramObj.getString("title"));
            processTaskVo.setOwner(paramObj.getString("owner"));
            processTaskVo.setChannelUuid(paramObj.getString("channelUuid"));
            processTaskVo.setPriorityUuid(paramObj.getString("priorityUuid"));
            processTaskVo.setProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            processTaskVo.setReporter(paramObj.getString("reporter"));
            processTaskVo.setStatus(ProcessTaskStatus.DRAFT.getValue());
            processTaskVo.setRegionId(paramObj.getLong("regionId"));
            String source = paramObj.getString("source");
            if (StringUtils.isNotBlank(source)) {
                processTaskVo.setSource(source);
            }
            IProcessCrossoverMapper processCrossoverMapper = CrossoverServiceFactory.getApi(IProcessCrossoverMapper.class);
            ProcessVo processVo = processCrossoverMapper.getProcessByUuid(currentProcessTaskStepVo.getProcessUuid());
            /* 对流程配置进行散列处理 **/

            JSONObject config = processVo.getConfig();
            if (MapUtils.isNotEmpty(config)) {
                //如果不存在优先级List则默认不显示优先级
//                List<ChannelPriorityVo> channelPriorityList = channelMapper.getChannelPriorityListByChannelUuid(processTaskVo.getChannelUuid());
//                if (CollectionUtils.isEmpty(channelPriorityList)) {
//                    processVo.getConfig().put("isNeedPriority", 0);
//                } else {
//                    processVo.getConfig().put("isNeedPriority", 1);
//                }
                JSONObject process = config.getJSONObject("process");
                JSONObject scoreConfig = process.getJSONObject("scoreConfig");
                if (MapUtils.isNotEmpty(scoreConfig)) {
                    Integer isActive = scoreConfig.getInteger("isActive");
                    if (Objects.equals(isActive, 1)) {
                        processTaskVo.setNeedScore(1);
                    } else {
                        processTaskVo.setNeedScore(0);
                    }
                }
                String configStr = processVo.getConfigStr();
                String hash = DigestUtils.md5DigestAsHex(configStr.getBytes());
                processTaskVo.setConfigHash(hash);
                processTaskCrossoverMapper.insertIgnoreProcessTaskConfig(new ProcessTaskConfigVo(hash, configStr));
            }
            IChannelCrossoverMapper channelCrossoverMapper = CrossoverServiceFactory.getApi(IChannelCrossoverMapper.class);
            ChannelVo channelVo = channelCrossoverMapper.getChannelByUuid(processTaskVo.getChannelUuid());
            String worktimeUuid = channelCrossoverMapper.getWorktimeUuidByChannelUuid(processTaskVo.getChannelUuid());
            processTaskVo.setWorktimeUuid(worktimeUuid);
            /* 生成工单号 **/
            IProcessTaskSerialNumberCrossoverMapper processTaskSerialNumberCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskSerialNumberCrossoverMapper.class);
            ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo = processTaskSerialNumberCrossoverMapper.getProcessTaskSerialNumberPolicyByChannelTypeUuid(channelVo.getChannelTypeUuid());
            if (processTaskSerialNumberPolicyVo == null) {
                IChannelTypeCrossoverMapper channelTypeCrossoverMapper = CrossoverServiceFactory.getApi(IChannelTypeCrossoverMapper.class);
                ChannelTypeVo channelTypeVo = channelTypeCrossoverMapper.getChannelTypeByUuid(channelVo.getChannelTypeUuid());
                if (channelTypeVo == null) {
                    throw new ChannelTypeNotFoundException(channelVo.getChannelTypeUuid());
                }
                throw new ProcessTaskSerialNumberPolicyNotFoundException(channelTypeVo.getName());
            }
            IProcessTaskSerialNumberPolicyHandler policyHandler = ProcessTaskSerialNumberPolicyHandlerFactory.getHandler(processTaskSerialNumberPolicyVo.getHandler());
            if (policyHandler == null) {
                throw new ProcessTaskSerialNumberPolicyHandlerNotFoundException(processTaskSerialNumberPolicyVo.getHandler());
            }
            String serialNumber = policyHandler.genarate(channelVo.getChannelTypeUuid());
            processTaskVo.setSerialNumber(serialNumber);
            processTaskSerialNumberCrossoverMapper.insertProcessTaskSerialNumber(processTaskVo.getId(), serialNumber);
            /* 创建工单 **/
            processTaskCrossoverMapper.insertProcessTask(processTaskVo);
            currentProcessTaskStepVo.setProcessTaskId(processTaskVo.getId());

            /* 写入表单信息 **/
            if (StringUtils.isNotBlank(processVo.getFormUuid())) {
                FormVersionVo formVersionVo = formMapper.getActionFormVersionByFormUuid(processVo.getFormUuid());
                if (formVersionVo != null && MapUtils.isNotEmpty(formVersionVo.getFormConfig())) {
                    ProcessTaskFormVo processTaskFormVo = new ProcessTaskFormVo();
                    processTaskFormVo.setFormContent(formVersionVo.getFormConfig().toJSONString());
                    processTaskFormVo.setProcessTaskId(processTaskVo.getId());
                    processTaskFormVo.setFormUuid(formVersionVo.getFormUuid());
                    processTaskFormVo.setFormName(formVersionVo.getFormName());
                    processTaskCrossoverMapper.insertProcessTaskForm(processTaskFormVo);
                    processTaskCrossoverMapper.insertIgnoreProcessTaskFormContent(processTaskFormVo);
                }
            }

            ProcessScoreTemplateVo processScoreTemplateVo = processCrossoverMapper.getProcessScoreTemplateByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            if (processScoreTemplateVo != null) {
                ProcessTaskScoreTemplateVo processTaskScoreTemplateVo = new ProcessTaskScoreTemplateVo(processScoreTemplateVo);
                JSONObject processTaskScoreTemplateConfig = processTaskScoreTemplateVo.getConfig();
                if (processTaskScoreTemplateConfig != null) {
                    IScoreTemplateCrossoverMapper scoreTemplateCrossoverMapper = CrossoverServiceFactory.getApi(IScoreTemplateCrossoverMapper.class);
                    List<ScoreTemplateDimensionVo> scoreTemplateDimensionList = scoreTemplateCrossoverMapper.getScoreTemplateDimensionListByScoreTemplateId(processTaskScoreTemplateVo.getScoreTemplateId());
                    processTaskScoreTemplateConfig.put("scoreTemplateDimensionList", scoreTemplateDimensionList);
                    ProcessTaskScoreTemplateConfigVo processTaskScoreTemplateConfigVo = new ProcessTaskScoreTemplateConfigVo(processTaskScoreTemplateConfig.toJSONString());
                    ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
                    if (StringUtils.isNotBlank(processTaskScoreTemplateConfigVo.getHash())
                            && selectContentByHashCrossoverMapper.checkProcessTaskScoreTempleteConfigIsExists(processTaskScoreTemplateConfigVo.getHash()) == 0) {
                        processTaskCrossoverMapper.insertProcessTaskScoreTemplateConfig(processTaskScoreTemplateConfigVo);
                    }
                    processTaskScoreTemplateVo.setConfigHash(processTaskScoreTemplateConfigVo.getHash());
                }
                processTaskScoreTemplateVo.setProcessTaskId(processTaskVo.getId());
                processTaskCrossoverMapper.insertProcessTaskScoreTemplate(processTaskScoreTemplateVo);
            }
            Map<String, Long> stepIdMap = new HashMap<>();
            List<ProcessTaskStepVo> processTaskStepList = new ArrayList<>();
            List<ProcessTaskStepConfigVo> processTaskStepConfigList = new ArrayList<>();
            List<ProcessTaskStepWorkerPolicyVo> processTaskStepWorkerPolicyList = new ArrayList<>();
            List<ProcessTaskStepTagVo> processTaskStepTagList = new ArrayList<>();
            /* 写入所有步骤信息 **/
            List<ProcessStepTagVo> processStepTagList = processCrossoverMapper.getProcessStepTagListByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            List<ProcessStepVo> processStepList = processCrossoverMapper.getProcessStepDetailByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            for (ProcessStepVo stepVo : processStepList) {
                ProcessTaskStepVo ptStepVo = new ProcessTaskStepVo(stepVo);
                ptStepVo.setStatus(ProcessTaskStepStatus.PENDING.getValue());
                ptStepVo.setProcessTaskId(processTaskVo.getId());
                String stepConfig = stepVo.getConfig();
                if (StringUtils.isNotBlank(stepConfig)) {
                    /* 对步骤配置进行散列处理 **/
                    String hash = DigestUtils.md5DigestAsHex(stepConfig.getBytes());
                    ptStepVo.setConfigHash(hash);
//                    processTaskCrossoverMapper.insertIgnoreProcessTaskStepConfig(new ProcessTaskStepConfigVo(hash, stepConfig));
                    processTaskStepConfigList.add(new ProcessTaskStepConfigVo(hash, stepConfig));
                }

//                processTaskCrossoverMapper.insertProcessTaskStep(ptStepVo);
                processTaskStepList.add(ptStepVo);
                stepIdMap.put(ptStepVo.getProcessStepUuid(), ptStepVo.getId());

                /* 写入用户分配策略信息 **/
                if (CollectionUtils.isNotEmpty(ptStepVo.getWorkerPolicyList())) {
                    for (ProcessTaskStepWorkerPolicyVo policyVo : ptStepVo.getWorkerPolicyList()) {
                        policyVo.setProcessTaskId(processTaskVo.getId());
                        policyVo.setProcessTaskStepId(ptStepVo.getId());
//                        processTaskCrossoverMapper.insertProcessTaskStepWorkerPolicy(policyVo);
                        processTaskStepWorkerPolicyList.add(policyVo);
                    }
                }

                /* 找到开始节点 **/
                if (stepVo.getType().equals(ProcessStepType.START.getValue())) {
                    currentProcessTaskStepVo.setId(ptStepVo.getId());
                }

//                Long notifyPolicyId = processCrossoverMapper.getNotifyPolicyIdByProcessStepUuid(ptStepVo.getProcessStepUuid());
//                if (notifyPolicyId != null) {
//                    NotifyPolicyVo notifyPolicyVo = notifyMapper.getNotifyPolicyById(notifyPolicyId);
//                    if (notifyPolicyVo != null) {
//                        ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo = new ProcessTaskStepNotifyPolicyVo();
//                        processTaskStepNotifyPolicyVo.setProcessTaskStepId(ptStepVo.getId());
//                        processTaskStepNotifyPolicyVo.setPolicyId(notifyPolicyVo.getId());
//                        processTaskStepNotifyPolicyVo.setPolicyName(notifyPolicyVo.getName());
//                        processTaskStepNotifyPolicyVo.setPolicyHandler(notifyPolicyVo.getHandler());
//                        processTaskStepNotifyPolicyVo.setPolicyConfig(notifyPolicyVo.getConfigStr());
//                        processTaskCrossoverMapper.insertProcessTaskStepNotifyPolicy(processTaskStepNotifyPolicyVo);
//                        processTaskStepNotifyPolicyList.add(processTaskStepNotifyPolicyVo);
//                    }
//                }

//                List<Long> tagIdList = processCrossoverMapper.getProcessStepTagIdListByProcessStepUuid(stepVo.getUuid());
                List<Long> tagIdList = new ArrayList<>();
                for (ProcessStepTagVo processStepTagVo : processStepTagList) {
                    if (Objects.equals(processStepTagVo.getProcessStepUuid(), stepVo.getUuid())) {
                        tagIdList.add(processStepTagVo.getTagId());
                    }
                }
                if (CollectionUtils.isNotEmpty(tagIdList)) {
                    ProcessTaskStepTagVo processTaskStepTagVo = new ProcessTaskStepTagVo();
                    processTaskStepTagVo.setProcessTaskId(processTaskVo.getId());
                    processTaskStepTagVo.setProcessTaskStepId(ptStepVo.getId());
                    for (Long tagId : tagIdList) {
                        processTaskStepTagVo.setTagId(tagId);
//                        processTaskCrossoverMapper.insertProcessTaskStepTag(processTaskStepTagVo);
                        processTaskStepTagList.add(processTaskStepTagVo);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(processTaskStepList)) {
                processTaskCrossoverMapper.insertProcessTaskStepList(processTaskStepList);
            }
            if (CollectionUtils.isNotEmpty(processTaskStepConfigList)) {
                processTaskCrossoverMapper.insertIgnoreProcessTaskStepConfigList(processTaskStepConfigList);
            }
            if (CollectionUtils.isNotEmpty(processTaskStepWorkerPolicyList)) {
                processTaskCrossoverMapper.insertProcessTaskStepWorkerPolicyList(processTaskStepWorkerPolicyList);
            }
            if (CollectionUtils.isNotEmpty(processTaskStepTagList)) {
                processTaskCrossoverMapper.insertProcessTaskStepTagList(processTaskStepTagList);
            }
            /* 写入关系信息 **/
            List<ProcessTaskStepRelVo> processTaskStepRelList = new ArrayList<>();
            List<ProcessStepRelVo> processStepRelList = processCrossoverMapper.getProcessStepRelByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            for (ProcessStepRelVo relVo : processStepRelList) {
                ProcessTaskStepRelVo processTaskStepRelVo = new ProcessTaskStepRelVo(relVo);
                processTaskStepRelVo.setProcessTaskId(processTaskVo.getId());
                processTaskStepRelVo.setFromProcessTaskStepId(stepIdMap.get(processTaskStepRelVo.getFromProcessStepUuid()));
                processTaskStepRelVo.setToProcessTaskStepId(stepIdMap.get(processTaskStepRelVo.getToProcessStepUuid()));
                /* 同时找到from step id 和to step id 时才写入，其他数据舍弃 **/
                if (processTaskStepRelVo.getFromProcessTaskStepId() != null && processTaskStepRelVo.getToProcessTaskStepId() != null) {
//                    processTaskCrossoverMapper.insertProcessTaskStepRel(processTaskStepRelVo);
                    processTaskStepRelList.add(processTaskStepRelVo);
                }
            }
            if (CollectionUtils.isNotEmpty(processTaskStepRelList)) {
                processTaskCrossoverMapper.insertProcessTaskStepRelList(processTaskStepRelList);
            }
            IProcessTaskSlaCrossoverMapper processTaskSlaCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskSlaCrossoverMapper.class);
            /* 写入sla信息 **/
            List<ProcessSlaVo> processSlaList = processCrossoverMapper.getProcessSlaByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
            for (ProcessSlaVo slaVo : processSlaList) {
                List<String> slaStepUuidList = processCrossoverMapper.getProcessStepUuidBySlaUuid(slaVo.getUuid());
                if (CollectionUtils.isNotEmpty(slaStepUuidList)) {
                    ProcessTaskSlaVo processTaskSlaVo = new ProcessTaskSlaVo(slaVo);
                    processTaskSlaVo.setProcessTaskId(processTaskVo.getId());
                    processTaskSlaVo.setIsActive(1);
                    processTaskSlaCrossoverMapper.insertProcessTaskSla(processTaskSlaVo);
                    for (String suuid : slaStepUuidList) {
                        Long stepId = stepIdMap.get(suuid);
                        if (stepId != null) {
                            processTaskSlaCrossoverMapper.insertProcessTaskStepSla(stepId, processTaskSlaVo.getId());
                        }
                    }
                }
            }

            /* 加入上报人为处理人 **/
            ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue());
            processTaskCrossoverMapper.insertProcessTaskStepUser(processTaskStepUserVo);
            processTaskCrossoverMapper.insertIgnoreProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue()));

            /* 保存转报数据 **/
            Long fromProcessTaskId = paramObj.getLong("fromProcessTaskId");
            Long channelTypeRelationId = paramObj.getLong("channelTypeRelationId");
            if (fromProcessTaskId != null && channelTypeRelationId != null) {
                processTaskCrossoverMapper.insertProcessTaskTransferReport(new ProcessTaskTransferReportVo(channelTypeRelationId, fromProcessTaskId, processTaskVo.getId()));
                Long invokeId = fromProcessTaskId;
                Long fromProcessTaskStepId = paramObj.getLong("fromProcessTaskStepId");
                if (fromProcessTaskStepId != null) {
                    invokeId = fromProcessTaskStepId;
                }
                processTaskCrossoverMapper.insertProcessTaskInvoke(processTaskVo.getId(), ProcessTaskSource.PROCESSTASK_TRANSFER_REPORT.getValue(), ProcessTaskSourceType.ITSM.getValue(), invokeId);
            }

            //父子工单
            String invoke = paramObj.getString("invoke");
            if (StringUtils.isNotBlank(invoke)) {
                IProcessTaskSource processTaskSource = ProcessTaskSourceFactory.getHandler(invoke);
                if (processTaskSource == null) {
                    throw new ProcessTaskSourceNotFoundException(invoke);
                }
                processTaskSource.saveDraft(paramObj, processTaskVo);
            }
        } else {
            /* 锁定当前流程 **/
            processTaskCrossoverMapper.getProcessTaskLockById(processTaskId);
            // 第二次保存时的操作
            processTaskVo = processTaskCrossoverMapper.getProcessTaskById(processTaskId);
            new ProcessAuthManager
                    .TaskOperationChecker(processTaskVo.getId(), ProcessTaskOperationType.PROCESSTASK_START)
                    .build()
                    .checkAndNoPermissionThrowException();
            /* 更新工单信息 **/
            processTaskVo.setTitle(paramObj.getString("title"));
            processTaskVo.setOwner(paramObj.getString("owner"));
            processTaskVo.setReporter(paramObj.getString("reporter"));
            processTaskVo.setPriorityUuid(paramObj.getString("priorityUuid"));
            processTaskCrossoverMapper.updateProcessTaskTitleOwnerPriorityUuid(processTaskVo);
            processTaskCrossoverMapper.deleteProcessTaskStepContentByProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskCrossoverMapper.deleteProcessTaskStepFileByProcessTaskStepId(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
        }
        try {
            mySaveDraft(currentProcessTaskStepVo);
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.DRAFT.getValue());
            currentProcessTaskStepVo.setUpdateActiveTime(1);
            currentProcessTaskStepVo.setUpdateStartTime(1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            currentProcessTaskStepVo.setError(ex.getMessage());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        }

        return 1;
    }

    protected abstract int mySaveDraft(ProcessTaskStepVo processTaskStepVo) throws ProcessTaskException;

    @Override
    public final int startProcess(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        new ProcessAuthManager
                .TaskOperationChecker(currentProcessTaskStepVo.getProcessTaskId(), ProcessTaskOperationType.PROCESSTASK_START)
                .build()
                .checkAndNoPermissionThrowException();
        try {

            processStepHandlerCrossoverUtil.assignWorkerValid(currentProcessTaskStepVo);
            processStepHandlerCrossoverUtil.baseInfoValidFromDb(currentProcessTaskStepVo);

            /* 保存表单属性值 **/
            processStepHandlerCrossoverUtil.saveForm(currentProcessTaskStepVo);

            /* 写入“标签”信息 **/
            processStepHandlerCrossoverUtil.saveTagList(currentProcessTaskStepVo);
            /* 保存工单关注人 **/
            processStepHandlerCrossoverUtil.saveFocusUserList(currentProcessTaskStepVo);

            processStepHandlerCrossoverUtil.checkContentIsRequired(currentProcessTaskStepVo);
            myStartProcess(currentProcessTaskStepVo);

            /* 保存描述内容和附件 **/
            processStepHandlerCrossoverUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.PROCESSTASK_START);
            // 获取表单数据
            IProcessTaskCrossoverService processTaskCrossoverService = CrossoverServiceFactory.getApi(IProcessTaskCrossoverService.class);
            List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskCrossoverService.getProcessTaskFormAttributeDataListByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
                processTaskFormAttributeDataList.sort(ProcessTaskFormAttributeDataVo::compareTo);
                JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
                paramObj.put(ProcessTaskAuditDetailType.FORM.getParamName(), JSON.toJSONString(processTaskFormAttributeDataList));
            }
            /* 更新处理人状态 **/
            ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid(true));
            processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
            processTaskMajorUser.setUserType(ProcessUserType.MAJOR.getValue());
            processTaskCrossoverMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
            /* 清空worker表 **/
            processTaskCrossoverMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
            currentProcessTaskStepVo.setIsActive(2);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.SUCCEED.getValue());
            currentProcessTaskStepVo.setUpdateEndTime(1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);

            /* 流转到下一步 **/
            List<ProcessTaskStepThread> processTaskStepThreadList = new ArrayList<>();
            Set<Long> nextStepIdSet = getNext(currentProcessTaskStepVo);
            //将不流转的步骤的正向输入连线的isHit设置为-1
            identifyPostInvalidStepRelIsHit(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), nextStepIdSet);
            List<ProcessTaskStepVo> nextStepList = processTaskCrossoverMapper.getProcessTaskStepListByIdList(new ArrayList<>(nextStepIdSet));
            for (ProcessTaskStepVo nextStep : nextStepList) {
                IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
                if (nextStepHandler != null) {
                    ProcessTaskStepRelVo processTaskStepRelVo = new ProcessTaskStepRelVo();
                    processTaskStepRelVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                    processTaskStepRelVo.setToProcessTaskStepId(nextStep.getId());
                    processTaskStepRelVo.setIsHit(1);
                    processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(processTaskStepRelVo);
                    nextStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                    nextStep.setStartProcessTaskStepId(currentProcessTaskStepVo.getStartProcessTaskStepId());
                    ProcessTaskStepThread thread = new ProcessTaskStepThread(ProcessTaskStepOperationType.STEP_ACTIVE, nextStep, nextStepHandler.getMode()) {
                        @Override
                        protected void myExecute(ProcessTaskStepVo processTaskStepVo) {
                            IProcessStepHandler processStepHandler = ProcessStepHandlerFactory.getHandler(processTaskStepVo.getHandler());
                            if (processStepHandler != null) {
                                processStepHandler.active(processTaskStepVo);
                            }
                        }
                    };
                    processTaskStepThreadList.add(thread);
//                    doNext(ProcessTaskOperationType.STEP_ACTIVE, new ProcessStepThread(nextStep) {
//                        @Override
//                        public void myExecute() {
//                            nextStepHandler.active(nextStep);
//                        }
//                    });
                }
            }
            doNext(processTaskStepThreadList);
            /* 写入时间审计 **/
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_ACTIVE);
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_START);
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_COMPLETE);

            /* 计算SLA并触发超时警告 **/
            processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskNotifyTriggerType.STARTPROCESS);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskNotifyTriggerType.STARTPROCESS);

            //创建全文检索索引
            IFullTextIndexHandler indexHandler = FullTextIndexHandlerFactory.getHandler(ProcessFullTextIndexType.PROCESSTASK);
            if (indexHandler != null) {
                indexHandler.createIndex(currentProcessTaskStepVo.getProcessTaskId());
            }

            //父子工单
            ProcessTaskInvokeVo processTaskInvokeVo = processTaskCrossoverMapper.getInvokeByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            if (processTaskInvokeVo != null) {
                IProcessTaskSource processTaskSource = ProcessTaskSourceFactory.getHandler(processTaskInvokeVo.getSource());
                if (processTaskSource != null) {
                    processTaskSource.startProcess(currentProcessTaskStepVo);
//                    throw new ProcessTaskSourceNotFoundException(processTaskInvokeVo.getSource());
                }

            }
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            currentProcessTaskStepVo.setError(ex.getMessage());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.FAILED);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.FAILED);
        } finally {
            /* 处理历史记录 **/
            ProcessTaskTransferReportVo processTaskTransferReportVo = processTaskCrossoverMapper.getProcessTaskTransferReportByToProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            if (processTaskTransferReportVo != null) {
                currentProcessTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.CHANNELTYPERELATION.getParamName(), processTaskTransferReportVo.getChannelTypeRelationId());
                currentProcessTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.PROCESSTASK.getParamName(), processTaskTransferReportVo.getFromProcessTaskId());
                processStepHandlerCrossoverUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.REPORTRELATION);

                ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
                processTaskStepVo.setProcessTaskId(processTaskTransferReportVo.getFromProcessTaskId());
                processTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.CHANNELTYPERELATION.getParamName(), processTaskTransferReportVo.getChannelTypeRelationId());
                processTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.PROCESSTASKLIST.getParamName(), JSON.toJSONString(Arrays.asList(currentProcessTaskStepVo.getProcessTaskId())));
                processStepHandlerCrossoverUtil.audit(processTaskStepVo, ProcessTaskAuditType.TRANSFERREPORT);
            } else {
                processStepHandlerCrossoverUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.STARTPROCESS);
            }
        }
        return 0;
    }

    protected abstract int myStartProcess(ProcessTaskStepVo processTaskStepVo) throws ProcessTaskException;

    @Override
    public final Set<Long> getNext(ProcessTaskStepVo currentProcessTaskStepVo) {
//        List<ProcessTaskStepRelVo> relList = processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
//
//        currentProcessTaskStepVo.setRelList(relList);
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        String type = ProcessFlowDirection.FORWARD.getValue();
        JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
        String action = paramObj.getString("action");
        if (ProcessTaskStepOperationType.STEP_BACK.getValue().equals(action)) {
            type = ProcessFlowDirection.BACKWARD.getValue();
        }
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        List<Long> nextStepIdList = processTaskCrossoverMapper.getToProcessTaskStepIdListByFromIdAndType(currentProcessTaskStepVo.getId(), type);
        Long nextStepId = paramObj.getLong("nextStepId");
        Set<Long> nextStepSet = null;
        try {
            nextStepSet = myGetNext(currentProcessTaskStepVo, nextStepIdList, nextStepId);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
                currentProcessTaskStepVo.appendError(ex.getMessage());
            } else {
                currentProcessTaskStepVo.appendError(ExceptionUtils.getStackTrace(ex));
            }
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            /* 异常提醒 **/
            processStepHandlerCrossoverUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getId(), ex.getMessage(), ProcessTaskStepRemindType.ERROR);
        }

        /* 更新路径isHit=1，在active方法里需要根据isHit状态判断路径是否经通过 **/
        if (nextStepSet == null) {
            nextStepSet = new HashSet<>();
        }

        return nextStepSet;
    }

    /**
     * 标识失效步骤，将失效步骤的正向流转连线isHit设置为-1
     *
     * @param processTaskId            工单id
     * @param currentProcessTaskStepId 当前步骤id
     * @param activeStepIdSet          激活步骤列表
     */
    private void identifyPostInvalidStepRelIsHit(Long processTaskId, Long currentProcessTaskStepId, Set<Long> activeStepIdSet) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        List<ProcessTaskStepRelVo> allProcessTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelByProcessTaskId(processTaskId);
        List<ProcessTaskStepRelVo> needProcessTaskStepRelList = new ArrayList<>();
        doIdentifyPostInvalidStepRelIsHit(currentProcessTaskStepId, activeStepIdSet, allProcessTaskStepRelList, needProcessTaskStepRelList);
        if (CollectionUtils.isNotEmpty(needProcessTaskStepRelList)) {
            for (ProcessTaskStepRelVo processTaskStepRelVo : needProcessTaskStepRelList) {
                processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(processTaskStepRelVo);
            }
        }
    }

    /**
     * 标识失效步骤，将失效步骤的正向流转连线isHit设置为-1
     *
     * @param currentProcessTaskStepId   当前步骤id
     * @param activeStepIdSet            激活步骤列表
     * @param allProcessTaskStepRelList  工单的所有连线列表
     * @param needProcessTaskStepRelList 需要更新isHit值为-1的连线列表
     */
    private void doIdentifyPostInvalidStepRelIsHit(Long currentProcessTaskStepId, Set<Long> activeStepIdSet, List<ProcessTaskStepRelVo> allProcessTaskStepRelList, List<ProcessTaskStepRelVo> needProcessTaskStepRelList) {
        List<Long> unactiveStepIdList = new ArrayList<>();
        for (ProcessTaskStepRelVo processTaskStepRelVo : allProcessTaskStepRelList) {
            if (Objects.equals(processTaskStepRelVo.getFromProcessTaskStepId(), currentProcessTaskStepId) && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                if (!activeStepIdSet.contains(processTaskStepRelVo.getToProcessTaskStepId())) {
                    unactiveStepIdList.add(processTaskStepRelVo.getToProcessTaskStepId());
                    if (!Objects.equals(processTaskStepRelVo.getIsHit(), -1)) {
                        processTaskStepRelVo.setIsHit(-1);
                        needProcessTaskStepRelList.add(processTaskStepRelVo);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(unactiveStepIdList)) {
            Map<Long, List<ProcessTaskStepRelVo>> toStepIdMap = new HashMap<>();

            for (ProcessTaskStepRelVo processTaskStepRelVo : allProcessTaskStepRelList) {
                if (unactiveStepIdList.contains(processTaskStepRelVo.getToProcessTaskStepId()) && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                    List<ProcessTaskStepRelVo> fromStepRelList = toStepIdMap.computeIfAbsent(processTaskStepRelVo.getToProcessTaskStepId(), k -> new ArrayList<>());
                    fromStepRelList.add(processTaskStepRelVo);
                }
            }

            for (Long unactiveStepId : unactiveStepIdList) {
                boolean invalid = true;
                List<ProcessTaskStepRelVo> fromStepRelList = toStepIdMap.computeIfAbsent(unactiveStepId, k -> new ArrayList<>());
                for (ProcessTaskStepRelVo processTaskStepRelVo : fromStepRelList) {
                    if (Objects.equals(currentProcessTaskStepId, processTaskStepRelVo.getFromProcessTaskStepId())) {
                        continue;
                    }
                    if (processTaskStepRelVo.getType().equals(ProcessFlowDirection.FORWARD.getValue())) {
                        if (!Objects.equals(processTaskStepRelVo.getIsHit(), -1)) {
                            invalid = false;
                            break;
                        }
                    }
                }
                if (invalid) {
                    //节点失效, 更新节点状态，继续判断后续节点是否也是失效的
                    doIdentifyPostInvalidStepRelIsHit(unactiveStepId, new HashSet<>(), allProcessTaskStepRelList, needProcessTaskStepRelList);
                }
            }
        }
    }

    /*
      标识失效步骤，将失效步骤的正向流转连线isHit设置为-1

      @param currentProcessTaskStepId 当前步骤id
     * @param activeStepIdSet          激活步骤列表
     */
//    private void doIdentifyPostInvalidStepRelIsHit(Long currentProcessTaskStepId, Set<Long> activeStepIdSet) {
//        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
//        ProcessTaskStepVo processTaskStep = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepId);
//        List<Long> unactiveStepIdList = null;
//        List<Long> allNextStepIdList = processTaskCrossoverMapper.getToProcessTaskStepIdListByFromIdAndType(currentProcessTaskStepId, ProcessFlowDirection.FORWARD.getValue());
//        if (CollectionUtils.isNotEmpty(activeStepIdSet)) {
//            unactiveStepIdList = ListUtils.removeAll(allNextStepIdList, activeStepIdSet);
//        } else {
//            unactiveStepIdList = allNextStepIdList;
//        }
//        if (CollectionUtils.isNotEmpty(unactiveStepIdList)) {
//            Map<Long, List<ProcessTaskStepRelVo>> toStepIdMap = new HashMap<>();
//            List<ProcessTaskStepRelVo> processTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelListByToIdList(unactiveStepIdList);
//            for (ProcessTaskStepRelVo processTaskStepRelVo : processTaskStepRelList) {
//                if (processTaskStepRelVo.getType().equals(ProcessFlowDirection.FORWARD.getValue())) {
//                    List<ProcessTaskStepRelVo> fromStepRelList = toStepIdMap.computeIfAbsent(processTaskStepRelVo.getToProcessTaskStepId(), k -> new ArrayList<>());
//                    fromStepRelList.add(processTaskStepRelVo);
//                }
//            }
//            ProcessTaskStepRelVo updateProcessTaskStepRelVo = new ProcessTaskStepRelVo();
//            updateProcessTaskStepRelVo.setFromProcessTaskStepId(currentProcessTaskStepId);
//            updateProcessTaskStepRelVo.setIsHit(-1);
//            for (Long unactiveStepId : unactiveStepIdList) {
//                boolean invalid = true;
//                updateProcessTaskStepRelVo.setToProcessTaskStepId(unactiveStepId);
//                processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(updateProcessTaskStepRelVo);
//                List<ProcessTaskStepRelVo> fromStepRelList = toStepIdMap.computeIfAbsent(unactiveStepId, k -> new ArrayList<>());
//                for (ProcessTaskStepRelVo processTaskStepRelVo : fromStepRelList) {
//                    if (Objects.equals(currentProcessTaskStepId, processTaskStepRelVo.getFromProcessTaskStepId())) {
//                        continue;
//                    }
//                    if (processTaskStepRelVo.getType().equals(ProcessFlowDirection.FORWARD.getValue())) {
//                        if (!Objects.equals(processTaskStepRelVo.getIsHit(), -1)) {
//                            invalid = false;
//                            break;
//                        }
//                    }
//                }
//                if (invalid) {
//                    //节点失效, 更新节点状态，继续判断后续节点是否也是失效的
//                    doIdentifyPostInvalidStepRelIsHit(unactiveStepId, null);
//                }
//            }
//        }
//    }

    /**
     * 将当前步骤的所有后续步骤间的连线的isHit设置为0
     *
     * @param processTaskId            工单ID
     * @param currentProcessTaskStepId 步骤ID
     */
    private void resetPostStepRelIsHit(Long processTaskId, Long currentProcessTaskStepId) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        List<ProcessTaskStepRelVo> allProcessTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelByProcessTaskId(processTaskId);
        List<ProcessTaskStepRelVo> needProcessTaskStepRelList = new ArrayList<>();
        doResetPostStepRelIsHit(currentProcessTaskStepId, new ArrayList<>(), allProcessTaskStepRelList, needProcessTaskStepRelList);
        if (CollectionUtils.isNotEmpty(needProcessTaskStepRelList)) {
            for (ProcessTaskStepRelVo processTaskStepRelVo : needProcessTaskStepRelList) {
                processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(processTaskStepRelVo);
            }
        }
    }

    /**
     * 将当前步骤的所有后续步骤间的连线的isHit设置为0
     *
     * @param currentProcessTaskStepId   步骤ID
     * @param avoidCyclingStepIdList     遍历过的步骤id列表，避免重复
     * @param allProcessTaskStepRelList  工单的所有连线列表
     * @param needProcessTaskStepRelList 需要更新isHit值为0的连线列表
     */
    private void doResetPostStepRelIsHit(Long currentProcessTaskStepId, List<Long> avoidCyclingStepIdList, List<ProcessTaskStepRelVo> allProcessTaskStepRelList, List<ProcessTaskStepRelVo> needProcessTaskStepRelList) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        ProcessTaskStepVo step = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepId);
        List<Long> toStepIdList = new ArrayList<>();
        for (ProcessTaskStepRelVo processTaskStepRelVo : allProcessTaskStepRelList) {
            if (Objects.equals(processTaskStepRelVo.getFromProcessTaskStepId(), currentProcessTaskStepId) && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                toStepIdList.add(processTaskStepRelVo.getToProcessTaskStepId());
                if (!Objects.equals(processTaskStepRelVo.getIsHit(), 0)) {
                    processTaskStepRelVo.setIsHit(0);
                    needProcessTaskStepRelList.add(processTaskStepRelVo);
                }
            }
        }
        avoidCyclingStepIdList.add(currentProcessTaskStepId);
        if (CollectionUtils.isNotEmpty(toStepIdList)) {
            for (Long toStepId : toStepIdList) {
                if (!avoidCyclingStepIdList.contains(currentProcessTaskStepId)) {
                    doResetPostStepRelIsHit(toStepId, avoidCyclingStepIdList, allProcessTaskStepRelList, needProcessTaskStepRelList);
                }
            }
        }
    }

    /*
      将当前步骤的所有后续步骤间的连线的isHit设置为0

      @param currentProcessTaskStepId 当前步骤id
     * @param avoidCyclingStepIdList 避免循环步骤id列表
     */
//    private void doResetPostStepRelIsHit(Long currentProcessTaskStepId, List<Long> avoidCyclingStepIdList) {
//        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
//        ProcessTaskStepVo step = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepId);
//        List<Long> toStepIdList = processTaskCrossoverMapper.getToProcessTaskStepIdListByFromIdAndType(currentProcessTaskStepId, ProcessFlowDirection.FORWARD.getValue());
//        if (CollectionUtils.isNotEmpty(toStepIdList)) {
//            ProcessTaskStepRelVo processTaskStepRelVo = new ProcessTaskStepRelVo();
//            processTaskStepRelVo.setFromProcessTaskStepId(currentProcessTaskStepId);
//            processTaskStepRelVo.setIsHit(0);
//            processTaskStepRelVo.setType(ProcessFlowDirection.FORWARD.getValue());
//            processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(processTaskStepRelVo);
//            avoidCyclingStepIdList.add(currentProcessTaskStepId);
//            for (Long toStepId : toStepIdList) {
//                if (!avoidCyclingStepIdList.contains(currentProcessTaskStepId)) {
//                    doResetPostStepRelIsHit(toStepId, avoidCyclingStepIdList);
//                }
//            }
//        }
//    }

    /**
     * 将当前步骤的所有后续步骤中流转过的步骤都进行挂起操作
     *
     * @param currentProcessTaskStepVo 步骤信息
     */
    private void hangPostStep(ProcessTaskStepVo currentProcessTaskStepVo) {
////        if (!currentProcessTaskStepVo.getId().equals(currentProcessTaskStepVo.getStartProcessTaskStepId())) {
//            List<ProcessTaskStepRelVo> nextTaskStepRelList = processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
//            for (ProcessTaskStepRelVo nextTaskStepRelVo : nextTaskStepRelList) {
//                // 沿着流转过的路径向后找激活过的节点并挂起
//                if (Objects.equals(nextTaskStepRelVo.getIsHit(), 1) && Objects.equals(nextTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
//                    ProcessTaskStepVo nextProcessTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(nextTaskStepRelVo.getToProcessTaskStepId());
//                    if (!Objects.equals(nextProcessTaskStepVo.getIsActive(), 0)) {
//                        // 如果下一个步骤不等于发起步骤，则继续挂起
////                    if (!nextProcessTaskStepVo.getId().equals(currentProcessTaskStepVo.getStartProcessTaskStepId())) {
//                        IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(nextProcessTaskStepVo.getHandler());
//                        if (handler != null) {
//                            // 标记挂起操作的发起步骤，避免出现死循环
//                            nextProcessTaskStepVo.setStartProcessTaskStepId(currentProcessTaskStepVo.getStartProcessTaskStepId());
//                            // 标记挂起操作来源步骤
//                            nextProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
//                            doNext(ProcessTaskOperationType.STEP_HANG, new ProcessStepThread(nextProcessTaskStepVo) {
//                                @Override
//                                public void myExecute() {
//                                    handler.hang(nextProcessTaskStepVo);
//                                }
//                            });
//                        }
//                        /** 重置路径状态 恢复路径命中状态为0，代表路径未通过 **/
//                        nextTaskStepRelVo.setIsHit(0);
//                        processTaskMapper.updateProcessTaskStepRelIsHit(nextTaskStepRelVo);
////                    }
//                    }
//                }
//            }
////        }
        List<ProcessTaskStepThread> processTaskStepThreadList = new ArrayList<>();
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        List<Long> toStepIdList = processTaskCrossoverMapper.getToProcessTaskStepIdListByFromIdAndType(currentProcessTaskStepVo.getId(), ProcessFlowDirection.FORWARD.getValue());
        if (CollectionUtils.isNotEmpty(toStepIdList)) {
            List<ProcessTaskStepVo> toStepList = processTaskCrossoverMapper.getProcessTaskStepListByIdList(toStepIdList);
            for (ProcessTaskStepVo toStepVo : toStepList) {
                if (!Objects.equals(toStepVo.getIsActive(), 0)) {
                    IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(toStepVo.getHandler());
                    if (handler != null) {
                        toStepVo.setStartProcessTaskStepId(currentProcessTaskStepVo.getStartProcessTaskStepId());
                        toStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
                        ProcessTaskStepThread thread = new ProcessTaskStepThread(ProcessTaskStepOperationType.STEP_HANG, toStepVo, handler.getMode()) {
                            @Override
                            protected void myExecute(ProcessTaskStepVo processTaskStepVo) {
                                IProcessStepHandler processStepHandler = ProcessStepHandlerFactory.getHandler(processTaskStepVo.getHandler());
                                if (processStepHandler != null) {
                                    processStepHandler.hang(processTaskStepVo);
                                }
                            }
                        };
                        processTaskStepThreadList.add(thread);
//                        doNext(ProcessTaskOperationType.STEP_HANG, new ProcessStepThread(toStepVo) {
//                            @Override
//                            protected void myExecute() {
//                                handler.hang(toStepVo);
//                            }
//                        });
                    }
                }
            }
        }
        doNext(processTaskStepThreadList);
    }

//    private void resetConvergeInfo(ProcessTaskStepVo nextStepVo) {
//        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
//        List<ProcessTaskStepVo> stepList = processTaskCrossoverMapper.getProcessTaskStepByProcessTaskIdAndType(nextStepVo.getProcessTaskId(), ProcessStepType.END.getValue());
//        ProcessTaskStepVo endStepVo = null;
//        if (stepList.size() == 1) {
//            endStepVo = stepList.get(0);
//        }
//        // 重新插入汇聚数据
//        List<List<Long>> routeList = new ArrayList<>();
//        List<Long> routeStepList = new ArrayList<>();
//        routeList.add(routeStepList);
//
//        getAllRouteList(nextStepVo.getId(), routeList, routeStepList, endStepVo);
//        // 如果最后一个步骤不是结束节点的路由全部删掉，因为这是回环路由
//        Iterator<List<Long>> routeStepIt = routeList.iterator();
//        List<Long> convergeIdList = new ArrayList<>();
//        while (routeStepIt.hasNext()) {
//            List<Long> rsList = routeStepIt.next();
//            if (!rsList.get(rsList.size() - 1).equals(endStepVo.getId())) {
//                routeStepIt.remove();
//            } else {
//                for (Long cid : rsList) {
//                    if (!convergeIdList.contains(cid) && !cid.equals(nextStepVo.getId())) {
//                        convergeIdList.add(cid);
//                    }
//                }
//            }
//        }
//        if (convergeIdList.size() > 0) {
//            for (Long convergeId : convergeIdList) {
//                ProcessTaskConvergeVo processTaskStepConvergeVo = new ProcessTaskConvergeVo(nextStepVo.getProcessTaskId(), nextStepVo.getId(), convergeId);
//                if (processTaskCrossoverMapper.checkProcessTaskConvergeIsExists(processTaskStepConvergeVo) == 0) {
//                    processTaskCrossoverMapper.insertIgnoreProcessTaskConverge(processTaskStepConvergeVo);
//                }
//            }
//        }
//    }
//
//    private void getAllRouteList(Long processTaskStepId, List<List<Long>> routeList, List<Long> routeStepList, ProcessTaskStepVo endStepVo) {
//        if (!routeStepList.contains(processTaskStepId)) {
//            routeStepList.add(processTaskStepId);
//            List<Long> tmpRouteStepList = new ArrayList<>(routeStepList);
//            if (!processTaskStepId.equals(endStepVo.getId())) {
//                IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
//                List<ProcessTaskStepVo> convergeStepList = processTaskCrossoverMapper.getProcessTaskStepByConvergeId(processTaskStepId);
//                List<Long> convergeStepIdList = convergeStepList.stream().map(ProcessTaskStepVo::getId).collect(Collectors.toList());
//                List<Long> toProcessTaskStepIdList = processTaskCrossoverMapper.getToProcessTaskStepIdListByFromIdAndType(processTaskStepId, ProcessFlowDirection.FORWARD.getValue());
//                for (int i = 0; i < toProcessTaskStepIdList.size(); i++) {
//                    Long toProcessTaskStepId = toProcessTaskStepIdList.get(i);
//                    /* 当前节点不是别人的汇聚节点时，才记录进路由，这是为了避免因为出现打回路径而产生错误的汇聚数据 **/
//                    if (!convergeStepIdList.contains(toProcessTaskStepId)) {
//                        if (i > 0) {
//                            List<Long> newRouteStepList = new ArrayList<>(tmpRouteStepList);
//                            routeList.add(newRouteStepList);
//                            getAllRouteList(toProcessTaskStepId, routeList, newRouteStepList, endStepVo);
//                        } else {
//                            getAllRouteList(toProcessTaskStepId, routeList, routeStepList, endStepVo);
//                        }
//                    }
//                }
//            }
//        }
//    }

    private void resetConvergeInfo(ProcessTaskStepVo nextStepVo) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        List<ProcessTaskStepVo> stepList = processTaskCrossoverMapper.getProcessTaskStepByProcessTaskIdAndType(nextStepVo.getProcessTaskId(), ProcessStepType.END.getValue());
        ProcessTaskStepVo endStepVo = null;
        if (stepList.size() == 1) {
            endStepVo = stepList.get(0);
        }
        List<ProcessTaskStepRelVo> allProcessTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelByProcessTaskId(nextStepVo.getProcessTaskId());
        List<ProcessTaskConvergeVo> allProcessTaskConvergeList = processTaskCrossoverMapper.getProcessTaskConvergeListByProcessTaskId(nextStepVo.getProcessTaskId());
        // 重新插入汇聚数据
        List<List<Long>> routeList = new ArrayList<>();
        List<Long> routeStepList = new ArrayList<>();
        routeList.add(routeStepList);
        getAllRouteList(nextStepVo.getId(), routeList, routeStepList, endStepVo, allProcessTaskStepRelList, allProcessTaskConvergeList);
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
        if (!convergeIdList.isEmpty()) {
            List<ProcessTaskConvergeVo> processTaskConvergeList = new ArrayList<>();
            for (Long convergeId : convergeIdList) {
                ProcessTaskConvergeVo processTaskStepConvergeVo = new ProcessTaskConvergeVo(nextStepVo.getProcessTaskId(), nextStepVo.getId(), convergeId);
                processTaskConvergeList.add(processTaskStepConvergeVo);
            }
            processTaskCrossoverMapper.insertIgnoreProcessTaskConvergeList(processTaskConvergeList);
        }
    }

    private void getAllRouteList(Long processTaskStepId, List<List<Long>> routeList, List<Long> routeStepList, ProcessTaskStepVo endStepVo, List<ProcessTaskStepRelVo> allProcessTaskStepRelList, List<ProcessTaskConvergeVo> allProcessTaskConvergeList) {
        if (!routeStepList.contains(processTaskStepId)) {
            routeStepList.add(processTaskStepId);
            List<Long> tmpRouteStepList = new ArrayList<>(routeStepList);
            if (!processTaskStepId.equals(endStepVo.getId())) {
                List<Long> convergeStepIdList = new ArrayList<>();
                for (ProcessTaskConvergeVo processTaskConvergeVo : allProcessTaskConvergeList) {
                    if (Objects.equals(processTaskConvergeVo.getConvergeId(), processTaskStepId)) {
                        convergeStepIdList.add(processTaskConvergeVo.getProcessTaskStepId());
                    }
                }
                List<Long> toProcessTaskStepIdList = new ArrayList<>();
                for (ProcessTaskStepRelVo processTaskStepRelVo : allProcessTaskStepRelList) {
                    if (Objects.equals(processTaskStepRelVo.getFromProcessTaskStepId(), processTaskStepId) && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                        toProcessTaskStepIdList.add(processTaskStepRelVo.getToProcessTaskStepId());
                    }
                }
                for (int i = 0; i < toProcessTaskStepIdList.size(); i++) {
                    Long toProcessTaskStepId = toProcessTaskStepIdList.get(i);
                    /* 当前节点不是别人的汇聚节点时，才记录进路由，这是为了避免因为出现打回路径而产生错误的汇聚数据 **/
                    if (!convergeStepIdList.contains(toProcessTaskStepId)) {
                        if (i > 0) {
                            List<Long> newRouteStepList = new ArrayList<>(tmpRouteStepList);
                            routeList.add(newRouteStepList);
                            getAllRouteList(toProcessTaskStepId, routeList, newRouteStepList, endStepVo, allProcessTaskStepRelList, allProcessTaskConvergeList);
                        } else {
                            getAllRouteList(toProcessTaskStepId, routeList, routeStepList, endStepVo, allProcessTaskStepRelList, allProcessTaskConvergeList);
                        }
                    }
                }
            }
        }
    }

    protected abstract Set<Long> myGetNext(ProcessTaskStepVo currentProcessTaskStepVo, List<Long> nextStepIdList, Long nextStepId) throws ProcessTaskException;

    protected Set<Long> defaultGetNext(List<Long> nextStepIdList, Long nextStepId) throws ProcessTaskException {
        Set<Long> nextStepIdSet = new HashSet<>();
        if (nextStepIdList.size() == 1) {
            nextStepIdSet.add(nextStepIdList.get(0));
        } else if (nextStepIdList.size() > 1) {
            if (nextStepId == null) {
                throw new ProcessTaskFindMultipleNextStepException();
            }
            for (Long processTaskStepId : nextStepIdList) {
                if (processTaskStepId.equals(nextStepId)) {
                    nextStepIdSet.add(processTaskStepId);
                    break;
                }
            }
        }
        return nextStepIdSet;
    }

    protected synchronized static void doNext(List<ProcessTaskStepThread> processTaskStepThreadList) {
        if (processTaskStepThreadList.size() > 1) {
            IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
            List<ProcessTaskStepRelVo> processTaskStepRelList = processTaskCrossoverMapper.getProcessTaskStepRelByProcessTaskId(processTaskStepThreadList.get(0).getProcessTaskId());
            List<ProcessTaskStepVo> stepList = processTaskCrossoverMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskStepThreadList.get(0).getProcessTaskId(), ProcessStepType.END.getValue());
            ProcessTaskStepThreadComparator comparator = new ProcessTaskStepThreadComparator(processTaskStepRelList, stepList.get(0).getId());
            processTaskStepThreadList.sort(comparator);
        }
        AfterTransactionJob<ProcessStepThread> afterTransactionJob = new AfterTransactionJob<>("PROCESSTASK-STEP-HANDLER");
        for (ProcessTaskStepThread processTaskStepThread : processTaskStepThreadList) {
            String operationTypeValue = "";
            if (processTaskStepThread.getOperationType() != null) {
                operationTypeValue = processTaskStepThread.getOperationType().getValue();
            }
            ProcessTaskStepInOperationVo processTaskStepInOperationVo = new ProcessTaskStepInOperationVo(
                    processTaskStepThread.getProcessTaskId(),
                    processTaskStepThread.getProcessTaskStepId(),
                    operationTypeValue
            );
            /* 后台异步操作步骤前，在`processtask_step_in_operation`表中插入一条数据，标识该步骤正在后台处理中，异步处理完删除 **/
            IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
            processTaskCrossoverMapper.insertProcessTaskStepInOperation(processTaskStepInOperationVo);
            processTaskStepThread.setInOperationId(processTaskStepInOperationVo.getId());
            afterTransactionJob.executeInOrder(processTaskStepThread);
        }
    }

    protected static synchronized void doNext(IOperationType operationType, ProcessStepThread thread) {
        String operationTypeValue = "";
        if (operationType != null) {
            operationTypeValue = operationType.getValue();
        }
        ProcessTaskStepVo processTaskStepVo = thread.getProcessTaskStepVo();
        ProcessTaskStepInOperationVo processTaskStepInOperationVo = new ProcessTaskStepInOperationVo(
                processTaskStepVo.getProcessTaskId(),
                processTaskStepVo.getId(),
                operationTypeValue
        );
        IProcessStepInternalHandler processStepInternalHandler = ProcessStepInternalHandlerFactory.getHandler(processTaskStepVo.getHandler());
        if (processStepInternalHandler == null) {
            throw new ProcessStepUtilHandlerNotFoundException(processTaskStepVo.getHandler());
        }
        /* 后台异步操作步骤前，在`processtask_step_in_operation`表中插入一条数据，标识该步骤正在后台处理中，异步处理完删除 **/
        processStepInternalHandler.insertProcessTaskStepInOperation(processTaskStepInOperationVo);
        thread.setSupplier(() -> {
            IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
            processTaskCrossoverMapper.deleteProcessTaskStepInOperationById(processTaskStepInOperationVo.getId());
            return 1;
        });
//        TransactionSynchronizationPool.execute(thread);
        AfterTransactionJob<ProcessStepThread> afterTransactionJob = new AfterTransactionJob<>("PROCESSTASK-STEP-HANDLER");
        afterTransactionJob.execute(thread);
    }

    /**
     * handle方法异步模式会调用这个方法
     **/
    protected static synchronized void doNext(ProcessStepThread thread) {
        doNext(null, thread);
    }

    public int redo(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
        new ProcessAuthManager
                .TaskOperationChecker(currentProcessTaskStepVo.getProcessTaskId(), ProcessTaskOperationType.PROCESSTASK_REDO)
                .build()
                .checkAndNoPermissionThrowException();
        try {
            List<ProcessTaskStepVo> processTaskStepList = processTaskCrossoverMapper.getProcessTaskStepByProcessTaskIdAndType(currentProcessTaskStepVo.getProcessTaskId(), ProcessStepType.END.getValue());
            Long endProcessTaskStepId = processTaskStepList.get(0).getId();
            currentProcessTaskStepVo.setFromProcessTaskStepId(endProcessTaskStepId);
            currentProcessTaskStepVo.setStartProcessTaskStepId(endProcessTaskStepId);
            List<Long> parallelActivateStepIdList = new ArrayList<>();
            parallelActivateStepIdList.add(currentProcessTaskStepVo.getId());
            currentProcessTaskStepVo.setParallelActivateStepIdList(parallelActivateStepIdList);
            //让结束节点到重做节点的回退线的isHit=1，流程图中显示绿色
            ProcessTaskStepRelVo processTaskStepRelVo = new ProcessTaskStepRelVo();
            processTaskStepRelVo.setFromProcessTaskStepId(endProcessTaskStepId);
            processTaskStepRelVo.setToProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskStepRelVo.setIsHit(1);
            processTaskCrossoverMapper.updateProcessTaskStepRelIsHit(processTaskStepRelVo);

            stepMajorUserRegulate(currentProcessTaskStepVo);
            /* 设置当前步骤状态为未开始 **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.PENDING.getValue());
            /* 保存打回原因 **/
            processStepHandlerCrossoverUtil.saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.PROCESSTASK_REDO);
            myRedo(currentProcessTaskStepVo);

            /* 遍历后续节点所有步骤，写入汇聚步骤数据 **/
            resetConvergeInfo(currentProcessTaskStepVo);

            /* 如果当前步骤是二次进入(后续路径已经走过)，则需要对所有后续流转过的步骤都进行挂起操作 **/
            hangPostStep(currentProcessTaskStepVo);
            resetPostStepRelIsHit(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
            /* 获取当前步骤状态 **/

            /* 分配处理人 **/
            assign(currentProcessTaskStepVo);
            currentProcessTaskStepVo.setIsActive(1);
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
            IProcessTaskScoreCrossoverMapper processTaskScoreCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskScoreCrossoverMapper.class);
            processTaskScoreCrossoverMapper.deleteProcessTaskAutoScoreByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());

            currentProcessTaskStepVo.getParamObj().put("operateTime", new Date());
            ProcessTaskOperatePostProcessorFactory.invokePostProcessorsAfterProcessTaskStepOperate(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_REDO);
            /* 写入时间审计 **/
            processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskOperationType.PROCESSTASK_REDO);
            if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStepStatus.RUNNING.getValue())) {
                processStepHandlerCrossoverUtil.timeAudit(currentProcessTaskStepVo, ProcessTaskStepOperationType.STEP_START);
            }

            /* 计算SLA并触发超时警告 **/
            processStepHandlerCrossoverUtil.calculateSla(currentProcessTaskStepVo);

            /* 触发通知 **/
            processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskNotifyTriggerType.REOPENPROCESSTASK);

            /* 执行动作 **/
            processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskNotifyTriggerType.REOPENPROCESSTASK);

            /* 回退提醒 **/
            processStepHandlerCrossoverUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getId(), currentProcessTaskStepVo.getParamObj().getString("content"), ProcessTaskStepRemindType.REDO);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /* 处理历史记录 **/
            processStepHandlerCrossoverUtil.audit(currentProcessTaskStepVo, ProcessTaskAuditType.REDO);
        }
        return 1;
    }

    protected abstract int myRedo(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    public int scoreProcessTask(ProcessTaskVo currentProcessTaskVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        // 锁定当前流程
        processTaskCrossoverMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
        // 只有上报人才可评分
        new ProcessAuthManager
                .TaskOperationChecker(currentProcessTaskVo.getId(), ProcessTaskOperationType.PROCESSTASK_SCORE)
                .build()
                .checkAndNoPermissionThrowException();

        JSONObject paramObj = currentProcessTaskVo.getParamObj();
        Long scoreTemplateId = paramObj.getLong("scoreTemplateId");
        String content = paramObj.getString("content");
        List<ScoreTemplateDimensionVo> scoreDimensionList = JSON.parseArray(paramObj.getJSONArray("scoreDimensionList").toJSONString(), ScoreTemplateDimensionVo.class);

        IProcessTaskScoreCrossoverMapper processTaskScoreCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskScoreCrossoverMapper.class);
        ProcessTaskScoreVo processtaskScoreVo = new ProcessTaskScoreVo();
        processtaskScoreVo.setProcessTaskId(currentProcessTaskVo.getId());
        processtaskScoreVo.setScoreTemplateId(scoreTemplateId);
        processtaskScoreVo.setFcu(UserContext.get().getUserUuid());
        processtaskScoreVo.setIsAuto(0);
        for (ScoreTemplateDimensionVo scoreTemplateDimensionVo : scoreDimensionList) {
            processtaskScoreVo.setScoreDimensionId(scoreTemplateDimensionVo.getId());
            processtaskScoreVo.setScore(scoreTemplateDimensionVo.getScore());
            processTaskScoreCrossoverMapper.insertProcessTaskScore(processtaskScoreVo);
        }

        JSONObject contentObj = new JSONObject();
        contentObj.put("scoreTemplateId", scoreTemplateId);
        contentObj.put("content", content);
        contentObj.put("dimensionList", paramObj.getJSONArray("scoreDimensionList"));
        /*
         * processtask_content表存储了两份数据： 1、评价内容content本身 2、由评分模版ID、评价内容、评分维度与分数组装而成的JSON
         */
        if (StringUtils.isNotBlank(content)) {
            ProcessTaskContentVo contentVo = new ProcessTaskContentVo(content);
            processTaskCrossoverMapper.insertIgnoreProcessTaskContent(contentVo);
            processtaskScoreVo.setContentHash(contentVo.getHash());
            processTaskScoreCrossoverMapper.insertProcessTaskScoreContent(processtaskScoreVo);
        }
        currentProcessTaskVo.setStatus(ProcessTaskStatus.SCORED.getValue());
        processTaskCrossoverMapper.updateProcessTaskStatus(currentProcessTaskVo);
        processTaskScoreCrossoverMapper.deleteProcessTaskAutoScoreByProcessTaskId(currentProcessTaskVo.getId());
        ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
        processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
        processTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.SCORE.getParamName(), contentObj);
        processTaskStepVo.getParamObj().put("source", paramObj.getString("source"));
        /* 生成活动 */
        processStepHandlerCrossoverUtil.audit(processTaskStepVo, ProcessTaskAuditType.SCORE);
        /* 触发通知 **/
        processStepHandlerCrossoverUtil.notify(processTaskStepVo, ProcessTaskNotifyTriggerType.SCOREPROCESSTASK);
        processStepHandlerCrossoverUtil.action(processTaskStepVo, ProcessTaskNotifyTriggerType.SCOREPROCESSTASK);
        return 1;
    }

    /**
     * @param currentProcessTaskStepVo 步骤信息
     * @return void
     * @Description: 步骤主处理人校正操作 判断当前用户是否是代办人，如果不是就什么都不做，如果是，进行下面3个操作 1.往processtask_step_agent表中插入一条数据，记录该步骤的原主处理人和代办人
     * 2.将processtask_step_worker表中该步骤的主处理人uuid改为代办人(当前用户)
     * 3.将processtask_step_user表中该步骤的主处理人user_uuid改为代办人(当前用户)
     */
    private void stepMajorUserRegulate(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        Long processTaskId = currentProcessTaskStepVo.getProcessTaskId();
        Long processTaskStepId = currentProcessTaskStepVo.getId();
        String currentUserUuid = UserContext.get().getUserUuid(true);
        /* 能进入这个方法，说明当前用户有权限处理当前步骤，可能是三类处理人：第一处理人(A)、代办人(B)、代办人的代办人(C) 。其中A授权给B，B授权给C **/
        ProcessTaskStepAgentVo processTaskStepAgentVo = processTaskCrossoverMapper.getProcessTaskStepAgentByProcessTaskStepId(currentProcessTaskStepVo.getId());
        if (processTaskStepAgentVo == null) {
            // 代办人还没接管，当前用户可能是A和B
            int flag = 0;
            ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
            if (Objects.equals(processTaskStepVo.getStatus(), ProcessTaskStepStatus.SUCCEED.getValue())) {
                ProcessTaskStepUserVo searchVo = new ProcessTaskStepUserVo(
                        currentProcessTaskStepVo.getProcessTaskId(),
                        currentProcessTaskStepVo.getId(),
                        currentUserUuid,
                        ProcessUserType.MAJOR.getValue()
                );
                flag = processTaskCrossoverMapper.checkIsProcessTaskStepUser(searchVo);
            } else {
                AuthenticationInfoVo authenticationInfoVo = UserContext.get().getAuthenticationInfoVo();
                flag = processTaskCrossoverMapper.checkIsWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue(), authenticationInfoVo);
            }

            if (flag == 0) {
                // 当用户是B
                String userUuid = null;
                ProcessTaskVo processTaskVo = processTaskCrossoverMapper.getProcessTaskById(currentProcessTaskStepVo.getProcessTaskId());
                IProcessTaskAgentCrossoverService processTaskAgentCrossoverService = CrossoverServiceFactory.getApi(IProcessTaskAgentCrossoverService.class);
                List<String> fromUserUuidList = processTaskAgentCrossoverService.getFromUserUuidListByToUserUuidAndChannelUuid(UserContext.get().getUserUuid(), processTaskVo.getChannelUuid());
                for (String fromUserUuid : fromUserUuidList) {
                    if (Objects.equals(processTaskStepVo.getStatus(), ProcessTaskStepStatus.SUCCEED.getValue())) {
                        ProcessTaskStepUserVo searchVo = new ProcessTaskStepUserVo(
                                currentProcessTaskStepVo.getProcessTaskId(),
                                currentProcessTaskStepVo.getId(),
                                fromUserUuid,
                                ProcessUserType.MAJOR.getValue()
                        );
                        if (processTaskCrossoverMapper.checkIsProcessTaskStepUser(searchVo) > 0) {
                            userUuid = fromUserUuid;
                            break;
                        }
                    } else {
                        AuthenticationInfoVo authenticationInfoVo = authenticationInfoService.getAuthenticationInfo(fromUserUuid);
                        if (processTaskCrossoverMapper.checkIsWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue(), authenticationInfoVo) > 0) {
                            userUuid = fromUserUuid;
                            break;
                        }
                    }
                }
//                String userUuid = userMapper.getUserUuidByAgentUuidAndFunc(UserContext.get().getUserUuid(), "processTask");
                if (StringUtils.isNotBlank(userUuid)) {
                    ProcessTaskStepAgentVo processTaskStepAgent = new ProcessTaskStepAgentVo(
                            currentProcessTaskStepVo.getProcessTaskId(),
                            currentProcessTaskStepVo.getId(),
                            userUuid,
                            currentUserUuid
                    );
                    processTaskCrossoverMapper.replaceProcessTaskStepAgent(processTaskStepAgent);
                    updateProcessTaskStepWorkerUuid(processTaskId, processTaskStepId, userUuid, currentUserUuid);
                    updateProcessTaskStepUserUserUuid(processTaskId, processTaskStepId, userUuid, currentUserUuid);
                    currentProcessTaskStepVo.setOriginalUser(userUuid);
                }
            }
        } else {
            // 代办人接管过了，当前用户可能是A、B、C
            if (currentUserUuid.equals(processTaskStepAgentVo.getUserUuid())) {
                // 当前用户是A
                processTaskCrossoverMapper.deleteProcessTaskStepAgentByProcessTaskStepId(currentProcessTaskStepVo.getId());
                updateProcessTaskStepWorkerUuid(processTaskId, processTaskStepId, processTaskStepAgentVo.getAgentUuid(), currentUserUuid);
                updateProcessTaskStepUserUserUuid(processTaskId, processTaskStepId, processTaskStepAgentVo.getAgentUuid(), currentUserUuid);
            } else if (currentUserUuid.equals(processTaskStepAgentVo.getAgentUuid())) {
                // 当前用户是B
                currentProcessTaskStepVo.setOriginalUser(processTaskStepAgentVo.getUserUuid());
            } else {
                // 当前用户是C
                ProcessTaskStepAgentVo processTaskStepAgent = new ProcessTaskStepAgentVo(
                        currentProcessTaskStepVo.getProcessTaskId(),
                        currentProcessTaskStepVo.getId(),
                        processTaskStepAgentVo.getAgentUuid(),
                        currentUserUuid
                );
                processTaskCrossoverMapper.replaceProcessTaskStepAgent(processTaskStepAgent);
                updateProcessTaskStepWorkerUuid(processTaskId, processTaskStepId, processTaskStepAgentVo.getAgentUuid(), currentUserUuid);
                updateProcessTaskStepUserUserUuid(processTaskId, processTaskStepId, processTaskStepAgentVo.getAgentUuid(), currentUserUuid);
                currentProcessTaskStepVo.setOriginalUser(processTaskStepAgentVo.getAgentUuid());
            }
        }

    }

    /**
     * 更新processtask_step_worker表的uuid字段值
     *
     * @param processTaskId     工单ID
     * @param processTaskStepId 步骤ID
     * @param oldUuid           旧值
     * @param newUuid           新值
     */
    private void updateProcessTaskStepWorkerUuid(Long processTaskId, Long processTaskStepId, String oldUuid, String newUuid) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo(
                processTaskId,
                processTaskStepId,
                GroupSearch.USER.getValue(),
                oldUuid,
                ProcessUserType.MAJOR.getValue(),
                newUuid
        );
        if (processTaskCrossoverMapper.checkProcessTaskStepWorkerIsExistsByPrimaryKey(processTaskStepWorkerVo) > 0) {
            processTaskStepWorkerVo.setUuid(newUuid);
            if (processTaskCrossoverMapper.checkProcessTaskStepWorkerIsExistsByPrimaryKey(processTaskStepWorkerVo) > 0) {
                processTaskStepWorkerVo.setUuid(oldUuid);
                processTaskCrossoverMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
            } else {
                processTaskStepWorkerVo.setUuid(oldUuid);
                processTaskCrossoverMapper.updateProcessTaskStepWorkerUuid(processTaskStepWorkerVo);
            }
        }
    }

    /**
     * 更新processtask_step_user表的user_uuid字段值
     *
     * @param processTaskId     工单ID
     * @param processTaskStepId 步骤ID
     * @param oldUserUuid       旧值
     * @param newUserUuid       新值
     */
    private void updateProcessTaskStepUserUserUuid(Long processTaskId, Long processTaskStepId, String oldUserUuid, String newUserUuid) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(
                processTaskId,
                processTaskStepId,
                oldUserUuid,
                ProcessUserType.MAJOR.getValue(),
                newUserUuid
        );
        if (processTaskCrossoverMapper.checkIsProcessTaskStepUser(processTaskStepUserVo) > 0) {
            processTaskStepUserVo.setUserUuid(newUserUuid);
            if (processTaskCrossoverMapper.checkIsProcessTaskStepUser(processTaskStepUserVo) > 0) {
                processTaskStepUserVo.setUserUuid(oldUserUuid);
                processTaskCrossoverMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
            } else {
                processTaskStepUserVo.setUserUuid(oldUserUuid);
                UserVo userVo = userMapper.getUserBaseInfoByUuid(newUserUuid);
                processTaskStepUserVo.setUserName(userVo.getUserName());
                processTaskCrossoverMapper.updateProcessTaskStepUserUserUuid(processTaskStepUserVo);
            }
        }
    }

    @Override
    public final int fail(ProcessTaskStepVo currentProcessTaskStepVo) {
        IProcessStepHandlerCrossoverUtil processStepHandlerCrossoverUtil = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverUtil.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        String majorUserUuid = null;
        List<ProcessTaskStepUserVo> processTaskStepUserList = processTaskCrossoverMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
        if (CollectionUtils.isNotEmpty(processTaskStepUserList)) {
            majorUserUuid = processTaskStepUserList.get(0).getUserUuid();
        }
        /* 更新处理人状态 **/
        ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(
                currentProcessTaskStepVo.getProcessTaskId(),
                currentProcessTaskStepVo.getId(),
                majorUserUuid
        );
        // 兼容automatic作业无用户
        processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
        processTaskMajorUser.setUserType(ProcessUserType.MAJOR.getValue());
        processTaskCrossoverMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
        /* 清空worker表 **/
        processTaskCrossoverMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
        processTaskCrossoverMapper.deleteProcessTaskStepReapprovalRestoreBackupByBackupStepId(currentProcessTaskStepVo.getId());

        String error = currentProcessTaskStepVo.getParamObj().getString("error");

        /* 更新步骤状态 **/
//        currentProcessTaskStepVo.setIsActive(0);
        currentProcessTaskStepVo.setIsActive(1);
        currentProcessTaskStepVo.setUpdateEndTime(1);
        currentProcessTaskStepVo.setStatus(ProcessTaskStepStatus.FAILED.getValue());
        currentProcessTaskStepVo.setError(error);
        updateProcessTaskStepStatus(currentProcessTaskStepVo);
        /* 异常提醒 **/
        processStepHandlerCrossoverUtil.saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getStartProcessTaskStepId(), error, ProcessTaskStepRemindType.ERROR);
        /* 触发通知 **/
        processStepHandlerCrossoverUtil.notify(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.FAILED);
        /* 执行动作 **/
        processStepHandlerCrossoverUtil.action(currentProcessTaskStepVo, ProcessTaskStepNotifyTriggerType.FAILED);
        return 1;
    }

    @Override
    public List<ProcessTaskStepWorkerVo> getMinorWorkerList(ProcessTaskStepVo taskStepVo) {
        return myMinorWorkerList(taskStepVo);
    }

    /**
     * 获取对应步骤的minor worker
     *
     * @param taskStepVo 工单步骤
     * @return 对应步骤模块的待处理人
     */
    protected List<ProcessTaskStepWorkerVo> myMinorWorkerList(ProcessTaskStepVo taskStepVo) {
        return new ArrayList<>();
    }

    @Override
    public String getMinorName() {
        return myMinorName();
    }

    protected String myMinorName() {
        return null;
    }
}
