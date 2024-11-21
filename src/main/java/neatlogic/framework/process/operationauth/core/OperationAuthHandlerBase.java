package neatlogic.framework.process.operationauth.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.common.constvalue.UserType;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.dto.AuthenticationInfoVo;
import neatlogic.framework.process.constvalue.*;
import neatlogic.framework.process.crossover.IProcessStepHandlerCrossoverMapper;
import neatlogic.framework.process.crossover.ISelectContentByHashCrossoverMapper;
import neatlogic.framework.process.dto.*;
import neatlogic.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import neatlogic.framework.process.stephandler.core.IProcessStepHandler;
import neatlogic.framework.process.stephandler.core.IProcessStepInternalHandler;
import neatlogic.framework.process.stephandler.core.ProcessStepHandlerFactory;
import neatlogic.framework.process.stephandler.core.ProcessStepInternalHandlerFactory;
import neatlogic.framework.service.AuthenticationInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class OperationAuthHandlerBase implements IOperationAuthHandler {
    protected static AuthenticationInfoService authenticationInfoService;

    @Autowired
    public void setAuthenticationInfoService(AuthenticationInfoService _authenticationInfoService) {
        authenticationInfoService = _authenticationInfoService;
    }

    /**
     * 
    * @Description: 判断当前用户是不是工单任意一个步骤的待处理人
    * @param processTaskVo 工单信息
    * @param userType 处理人类型，major主处理人或minor协助处理人
    * @param userUuid 用户
    * @return boolean 
     */
    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, String userType, String userUuid) {
        AuthenticationInfoVo authenticationInfoVo = null;
        if (Objects.equals(UserContext.get().getUserUuid(), userUuid)) {
            authenticationInfoVo = UserContext.get().getAuthenticationInfoVo();
        } else {
            authenticationInfoVo = authenticationInfoService.getAuthenticationInfo(userUuid);
        }
        List<String> teamUuidList = authenticationInfoVo.getTeamUuidList();
        List<String> roleUuidList = authenticationInfoVo.getRoleUuidList();
        for (ProcessTaskStepVo processTaskStepVo : processTaskVo.getStepList()) {
            for (ProcessTaskStepWorkerVo workerVo : processTaskStepVo.getWorkerList()) {
                if (userType == null || userType.equals(workerVo.getUserType())) {
                    if (GroupSearch.USER.getValue().equals(workerVo.getType())) {
                        if (userUuid.equals(workerVo.getUuid())) {
                            return true;
                        }
                    } else if (GroupSearch.TEAM.getValue().equals(workerVo.getType())) {
                        if (teamUuidList.contains(workerVo.getUuid())) {
                            return true;
                        }
                    } else if (GroupSearch.ROLE.getValue().equals(workerVo.getType())) {
                        if (roleUuidList.contains(workerVo.getUuid())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, String userUuid) {
        return checkIsWorker(processTaskVo, null, userUuid);
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 判断当前用户是不是步骤的待处理人 
    * @param processTaskStepVo 步骤信息
    * @param userType 处理人类型，major主处理人或minor协助处理人
    * @param userUuid 用户
    * @return boolean
     */
    protected boolean checkIsWorker(ProcessTaskStepVo processTaskStepVo, String userType, String userUuid) {
        AuthenticationInfoVo authenticationInfoVo = null;
        if (Objects.equals(UserContext.get().getUserUuid(), userUuid)) {
            authenticationInfoVo = UserContext.get().getAuthenticationInfoVo();
        }
        if (authenticationInfoVo == null) {
            authenticationInfoVo = authenticationInfoService.getAuthenticationInfo(userUuid);
        }
        List<String> teamUuidList = authenticationInfoVo.getTeamUuidList();
        List<String> roleUuidList = authenticationInfoVo.getRoleUuidList();
        for (ProcessTaskStepWorkerVo workerVo : processTaskStepVo.getWorkerList()) {
            if (userType == null || userType.equals(workerVo.getUserType())) {
                if (GroupSearch.USER.getValue().equals(workerVo.getType())) {
                    if (userUuid.equals(workerVo.getUuid())) {
                        return true;
                    }
                } else if (GroupSearch.TEAM.getValue().equals(workerVo.getType())) {
                    if (teamUuidList.contains(workerVo.getUuid())) {
                        return true;
                    }
                } else if (GroupSearch.ROLE.getValue().equals(workerVo.getType())) {
                    if (roleUuidList.contains(workerVo.getUuid())) {
                        return true;
                    }
                }
            }
        }
        ProcessTaskStepAgentVo processTaskStepAgentVo = processTaskStepVo.getProcessTaskStepAgentVo();
        if (processTaskStepAgentVo != null) {
            if (Objects.equals(processTaskStepAgentVo.getUserUuid(), userUuid)) {
                authenticationInfoVo = null;
                if (Objects.equals(UserContext.get().getUserUuid(), processTaskStepAgentVo.getAgentUuid())) {
                    authenticationInfoVo = UserContext.get().getAuthenticationInfoVo();
                }
                if (authenticationInfoVo == null) {
                    authenticationInfoVo = authenticationInfoService.getAuthenticationInfo(processTaskStepAgentVo.getAgentUuid());
                }
                teamUuidList = authenticationInfoVo.getTeamUuidList();
                roleUuidList = authenticationInfoVo.getRoleUuidList();
                for (ProcessTaskStepWorkerVo workerVo : processTaskStepVo.getWorkerList()) {
                    if (userType == null || userType.equals(workerVo.getUserType())) {
                        if (GroupSearch.USER.getValue().equals(workerVo.getType())) {
                            if (processTaskStepAgentVo.getAgentUuid().equals(workerVo.getUuid())) {
                                return true;
                            }
                        } else if (GroupSearch.TEAM.getValue().equals(workerVo.getType())) {
                            if (teamUuidList.contains(workerVo.getUuid())) {
                                return true;
                            }
                        } else if (GroupSearch.ROLE.getValue().equals(workerVo.getType())) {
                            if (roleUuidList.contains(workerVo.getUuid())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean checkIsWorker(ProcessTaskStepVo processTaskStepVo, String userUuid) {
        return checkIsWorker(processTaskStepVo, null, userUuid);
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 判断当前用户是不是工单任意一个步骤的处理人 
    * @param processTaskVo 工单信息
    * @param userType 处理人类型，major主处理人或minor协助处理人
    * @param userUuid 用户
    * @return boolean 
     */
    protected boolean checkIsProcessTaskStepUser(ProcessTaskVo processTaskVo, String userType, String userUuid) {
        for (ProcessTaskStepVo processTaskStepVo : processTaskVo.getStepList()) {
            if (checkIsProcessTaskStepUser(processTaskStepVo, userType, userUuid)) {
                return true;
            }
        }
        return false;
    }

    protected boolean checkIsProcessTaskStepUser(ProcessTaskVo processTaskVo, String userUuid) {
        return checkIsProcessTaskStepUser(processTaskVo, null, userUuid);
    }
    
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 判断当前用户是不是步骤的待处理人 
    * @param processTaskStepVo 步骤信息
    * @param userType 处理人类型，major主处理人或minor协助处理人
    * @param userUuid 用户
    * @return boolean
     */
    protected boolean checkIsProcessTaskStepUser(ProcessTaskStepVo processTaskStepVo, String userType,
        String userUuid) {
        for (ProcessTaskStepUserVo processTaskStepUserVo : processTaskStepVo.getUserList()) {
            if (userType == null || userType.equals(processTaskStepUserVo.getUserType())) {
                if (userUuid.equals(processTaskStepUserVo.getUserUuid())) {
                    return true;
                }
            }
        }
        // 判断当前用户是否是原处理人
        if (userType == null || Objects.equals(userType, ProcessUserType.MAJOR.getValue())) {
//            if (Objects.equals(processTaskStepVo.getOriginalUser(), userUuid)) {
//                return true;
//            }
            ProcessTaskStepAgentVo processTaskStepAgentVo = processTaskStepVo.getProcessTaskStepAgentVo();
            if (processTaskStepAgentVo != null) {
                if (Objects.equals(processTaskStepAgentVo.getUserUuid(), userUuid)) {
                    for (ProcessTaskStepUserVo processTaskStepUserVo : processTaskStepVo.getUserList()) {
                        if (userType == null || userType.equals(processTaskStepUserVo.getUserType())) {
                            if (Objects.equals(processTaskStepAgentVo.getAgentUuid(), processTaskStepUserVo.getUserUuid())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean checkIsProcessTaskStepUser(ProcessTaskStepVo processTaskStepVo, String userUuid) {
        return checkIsProcessTaskStepUser(processTaskStepVo, null, userUuid);
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 判断userUuid用户是否在流程节点（或节点管理）设置中获得operationType权限的授权
    * @param processTaskVo 工单信息
    * @param processTaskStepVo 步骤信息
    * @param operationType 权限类型
    * @param userUuid 用户
    * @return boolean
     */
    protected boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo,
                                                     IOperationType operationType, String userUuid) {
        JSONArray authorityList = null;
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
        Integer enableAuthority = (Integer) JSONPath.read(stepConfig, "enableAuthority");
        if (Objects.equals(enableAuthority, 1)) {
            authorityList = (JSONArray) JSONPath.read(stepConfig, "authorityList");
        } else {
            String handler = processTaskStepVo.getHandler();
            IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(handler);
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(handler);
            }
            IProcessStepHandlerCrossoverMapper processStepHandlerCrossoverMapper = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverMapper.class);
            String processStepHandlerConfig = processStepHandlerCrossoverMapper.getProcessStepHandlerConfigByHandler(handler);
            JSONObject globalConfig = null;
            if (StringUtils.isNotBlank(processStepHandlerConfig)) {
                globalConfig = JSONObject.parseObject(processStepHandlerConfig);
            }
            globalConfig = processStepUtilHandler.makeupConfig(globalConfig);
            authorityList = globalConfig.getJSONArray("authorityList");
        }

        if (CollectionUtils.isNotEmpty(authorityList)) {
            return checkOperationAuthIsConfigured(processTaskVo, processTaskStepVo, operationType, authorityList,
                userUuid);
        }
        return false;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 判断userUuid用户是否在流程设置中获得operationType权限的授权
    * @param processTaskVo 工单信息
    * @param operationType 权限类型
    * @param userUuid 用户
    * @return boolean
     */
    protected boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, IOperationType operationType, String userUuid) {
        JSONArray authorityList =
            (JSONArray)JSONPath.read(processTaskVo.getConfig(), "process.processConfig.authorityList");
        if (CollectionUtils.isNotEmpty(authorityList)) {
            return checkOperationAuthIsConfigured(processTaskVo, null, operationType, authorityList, userUuid);
        }
        return false;
    }

    private boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, IOperationType operationType, JSONArray authorityList, String userUuid) {
        for (int i = 0; i < authorityList.size(); i++) {
            JSONObject authorityObj = authorityList.getJSONObject(i);
            String action = authorityObj.getString("action");
            if (operationType.getValue().equals(action)) {
                JSONArray acceptList = authorityObj.getJSONArray("acceptList");
                if (acceptList == null) {
                    acceptList = authorityObj.getJSONArray("defaultValue");
                }
                if (CollectionUtils.isNotEmpty(acceptList)) {
                    AuthenticationInfoVo authenticationInfoVo = null;
                    if (Objects.equals(UserContext.get().getUserUuid(), userUuid)) {
                        authenticationInfoVo = UserContext.get().getAuthenticationInfoVo();
                    } else {
                        authenticationInfoVo = authenticationInfoService.getAuthenticationInfo(userUuid);
                    }
                    List<String> teamUuidList = authenticationInfoVo.getTeamUuidList();
                    List<String> roleUuidList = authenticationInfoVo.getRoleUuidList();
                    for (int j = 0; j < acceptList.size(); j++) {
                        String accept = acceptList.getString(j);
                        String[] split = accept.split("#");
                        if (GroupSearch.COMMON.getValue().equals(split[0])) {
                            if (UserType.ALL.getValue().equals(split[1])) {
                                return true;
                            }
                        } else if (ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue().equals(split[0])) {
                            if (ProcessUserType.OWNER.getValue().equals(split[1])) {
                                if (userUuid.equals(processTaskVo.getOwner())) {
                                    return true;
                                }
                            } else if (ProcessUserType.REPORTER.getValue().equals(split[1])) {
                                if (userUuid.equals(processTaskVo.getReporter())) {
                                    return true;
                                }
                            } else if (ProcessUserType.MAJOR.getValue().equals(split[1])) {
                                if (processTaskStepVo != null) {
                                    if (checkIsProcessTaskStepUser(processTaskStepVo, ProcessUserType.MAJOR.getValue(),
                                        userUuid)) {
                                        return true;
                                    }
                                } else {
                                    if (checkIsProcessTaskStepUser(processTaskVo, ProcessUserType.MAJOR.getValue(),
                                        userUuid)) {
                                        return true;
                                    }
                                }
                            } else if (ProcessUserType.MINOR.getValue().equals(split[1])) {
                                if (processTaskStepVo != null) {
                                    if (checkIsProcessTaskStepUser(processTaskStepVo, ProcessUserType.MINOR.getValue(),
                                        userUuid)) {
                                        return true;
                                    }
                                } else {
                                    if (checkIsProcessTaskStepUser(processTaskVo, ProcessUserType.MINOR.getValue(),
                                        userUuid)) {
                                        return true;
                                    }
                                }
                            }
                        } else if (GroupSearch.USER.getValue().equals(split[0])) {
                            if (userUuid.equals(split[1])) {
                                return true;
                            }
                        } else if (GroupSearch.TEAM.getValue().equals(split[0])) {
                            if (teamUuidList.contains(split[1])) {
                                return true;
                            }
                        } else if (GroupSearch.ROLE.getValue().equals(split[0])) {
                            if (roleUuidList.contains(split[1])) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 根据流程图连线，判断当前步骤是否有flowDirection方向的连线 
    * @param processTaskVo 工单信息
    * @param processTaskStepId 步骤id
    * @param flowDirection 流转方向，forward（前进）或backward（后退）
    * @return boolean
     */
    protected boolean checkNextStepIsExistsByProcessTaskStepIdAndProcessFlowDirection(ProcessTaskVo processTaskVo,
        Long processTaskStepId, ProcessFlowDirection flowDirection) {
        for (ProcessTaskStepRelVo processTaskStepRelVo : processTaskVo.getStepRelList()) {
            if (processTaskStepRelVo.getFromProcessTaskStepId().equals(processTaskStepId)) {
                if (processTaskStepRelVo.getType().equals(flowDirection.getValue())) {
                    if (flowDirection == ProcessFlowDirection.BACKWARD) {
                        List<ProcessTaskStepVo> processTaskStepList = processTaskVo.getStepList();
                        for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
                            if (Objects.equals(processTaskStepVo.getId(), processTaskStepRelVo.getToProcessTaskStepId())) {
                                if (!Objects.equals(processTaskStepVo.getIsActive(), 0)) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 判断userUuid用户是否拥有工单的任意一个步骤的撤回权限 
    * @param processTaskVo 工单信息
    * @param processTaskStepId 步骤id
    * @param userUuid 用户
    * @return boolean
     */
    protected boolean checkIsRetractableStepByProcessTaskStepId(ProcessTaskVo processTaskVo, Long processTaskStepId,
        String userUuid) {
        /** 所有前置步骤id **/
        List<Long> fromStepIdList = new ArrayList<>();
        for (ProcessTaskStepRelVo processTaskStepRelVo : processTaskVo.getStepRelList()) {
            if (processTaskStepRelVo.getToProcessTaskStepId().equals(processTaskStepId)) {
                if (processTaskStepRelVo.getIsHit() == 1 && Objects.equals(processTaskStepRelVo.getType(), ProcessFlowDirection.FORWARD.getValue())) {
                    fromStepIdList.add(processTaskStepRelVo.getFromProcessTaskStepId());
                }
            }
        }
        /** 找到所有已完成步骤 **/
        for (ProcessTaskStepVo processTaskStepVo : processTaskVo.getStepList()) {
            if (fromStepIdList.contains(processTaskStepVo.getId())) {
                IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(processTaskStepVo.getHandler());
                if (handler != null) {
                    if (ProcessStepMode.MT == handler.getMode()) {// 手动处理节点
                        if (checkOperationAuthIsConfigured(processTaskVo, processTaskStepVo,
                                ProcessTaskStepOperationType.STEP_RETREAT, userUuid)) {
                            return true;
                        }
                    } else {// 自动处理节点，继续找前置节点
                        return checkIsRetractableStepByProcessTaskStepId(processTaskVo, processTaskStepVo.getId(),
                            userUuid);
                    }
                }
            }
        }
        return false;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 判断userUuid用户是否拥有processTaskStepId步骤的撤回权限
    * @param processTaskVo 工单信息
    * @param processTaskStepId 步骤id
    * @return boolean
     */
    protected boolean checkCurrentStepIsRetractableByProcessTaskStepId(ProcessTaskVo processTaskVo,
        Long processTaskStepId) {
        /** 所有后置置步骤id **/
        List<Long> toStepIdList = new ArrayList<>();
        for (ProcessTaskStepRelVo processTaskStepRelVo : processTaskVo.getStepRelList()) {
            if (processTaskStepRelVo.getFromProcessTaskStepId().equals(processTaskStepId)) {
                if (processTaskStepRelVo.getIsHit() == 1) {
                    toStepIdList.add(processTaskStepRelVo.getToProcessTaskStepId());
                }
            }
        }
        for (ProcessTaskStepVo processTaskStepVo : processTaskVo.getStepList()) {
            if (toStepIdList.contains(processTaskStepVo.getId())) {
                IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(processTaskStepVo.getHandler());
                if (handler != null) {
                    if (ProcessStepMode.MT == handler.getMode()) {// 手动处理节点
                        if (processTaskStepVo.getIsActive() == 1) {
                            return true;
                        }
                    } else {// 自动处理节点，继续找前置节点
                        return checkCurrentStepIsRetractableByProcessTaskStepId(processTaskVo, processTaskStepVo.getId());
                    }
                }
            }
        }
        return false;
    }

//    /**
//     * 检查工单状态，如果processTaskStatus属于status其中一员，则返回对应的异常对象，否则返回null
//     * @param processTaskStatus 工单状态
//     * @param statuss 状态列表
//     * @return
//     */
//    protected ProcessTaskPermissionDeniedException checkProcessTaskStatus(String processTaskStatus, ProcessTaskStatus ... statuss) {
//        if (statuss != null) {
//            for (ProcessTaskStatus status : statuss) {
//                switch (status) {
//                    case DRAFT:
//                        if (ProcessTaskStatus.DRAFT.getValue().equals(processTaskStatus)) {
//                            return new ProcessTaskUnsubmittedException();
//                        }
//                        break;
//                    case SUCCEED:
//                        if (ProcessTaskStatus.SUCCEED.getValue().equals(processTaskStatus)) {
//                            return new ProcessTaskSucceededException();
//                        }
//                        break;
//                    case ABORTED:
//                        if (ProcessTaskStatus.ABORTED.getValue().equals(processTaskStatus)) {
//                            return new ProcessTaskAbortedException();
//                        }
//                        break;
//                    case FAILED:
//                        if (ProcessTaskStatus.FAILED.getValue().equals(processTaskStatus)) {
//                            return new ProcessTaskFailedException();
//                        }
//                        break;
//                    case HANG:
//                        if (ProcessTaskStatus.HANG.getValue().equals(processTaskStatus)) {
//                            return new ProcessTaskHangException();
//                        }
//                        break;
//                    case SCORED:
//                        if (ProcessTaskStatus.SCORED.getValue().equals(processTaskStatus)) {
//                            return new ProcessTaskScoredException();
//                        }
//                        break;
//                    case RUNNING:
//                        if (ProcessTaskStatus.RUNNING.getValue().equals(processTaskStatus)) {
//                            return new ProcessTaskRunningException();
//                        }
//                        break;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 检查步骤状态，如果stepStatus属于status其中一员，则返回对应的异常对象，否则返回null
//     * @param stepStatus 步骤状态
//     * @param statuss 状态列表
//     * @return
//     */
//    protected ProcessTaskPermissionDeniedException checkProcessTaskStepStatus(String stepStatus, ProcessTaskStatus ... statuss) {
//        if (statuss != null) {
//            for (ProcessTaskStatus status : statuss) {
//                switch (status) {
//                    case DRAFT:
//                        if (ProcessTaskStatus.DRAFT.getValue().equals(stepStatus)) {
//                            return new ProcessTaskStepUnsubmittedException();
//                        }
//                        break;
//                    case PENDING:
//                        if (ProcessTaskStatus.PENDING.getValue().equals(stepStatus)) {
//                            return new ProcessTaskStepPendingException();
//                        }
//                        break;
//                    case RUNNING:
//                        if (ProcessTaskStatus.RUNNING.getValue().equals(stepStatus)) {
//                            return new ProcessTaskStepRunningException();
//                        }
//                        break;
//                    case SUCCEED:
//                        if (ProcessTaskStatus.SUCCEED.getValue().equals(stepStatus)) {
//                            return new ProcessTaskStepSucceededException();
//                        }
//                        break;
//                    case FAILED:
//                        if (ProcessTaskStatus.FAILED.getValue().equals(stepStatus)) {
//                            return new ProcessTaskStepFailedException();
//                        }
//                        break;
//                    case HANG:
//                        if (ProcessTaskStatus.HANG.getValue().equals(stepStatus)) {
//                            return new ProcessTaskStepHangException();
//                        }
//                        break;
//                }
//            }
//        }
//        return null;
//    }
}
