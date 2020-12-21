package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.constvalue.UserType;
import codedriver.framework.dao.mapper.TeamMapper;
import codedriver.framework.dao.mapper.UserMapper;
import codedriver.framework.process.constvalue.ProcessFlowDirection;
import codedriver.framework.process.constvalue.ProcessStepMode;
import codedriver.framework.process.constvalue.ProcessTaskGroupSearch;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dao.mapper.ProcessStepHandlerMapper;
import codedriver.framework.process.dao.mapper.SelectContentByHashMapper;
import codedriver.framework.process.dto.ProcessStepHandlerVo;
import codedriver.framework.process.dto.ProcessTaskStepRelVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import codedriver.framework.process.stephandler.core.IProcessStepHandler;
import codedriver.framework.process.stephandler.core.IProcessStepUtilHandler;
import codedriver.framework.process.stephandler.core.ProcessStepHandlerFactory;
import codedriver.framework.process.stephandler.core.ProcessStepUtilHandlerFactory;

public abstract class OperationAuthHandlerBase implements IOperationAuthHandler {
    protected static UserMapper userMapper;
    protected static TeamMapper teamMapper;
    protected static ProcessStepHandlerMapper processStepHandlerMapper;
    protected static SelectContentByHashMapper selectContentByHashMapper;
    
    @Autowired
    public void setTeamMapper(TeamMapper _teamMapper) {
        teamMapper = _teamMapper;
    }

    @Autowired
    public void setProcessStepHandlerMapper(ProcessStepHandlerMapper _processStepHandlerMapper) {
        processStepHandlerMapper = _processStepHandlerMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper _userMapper) {
        userMapper = _userMapper;
    }
    
    @Autowired
    public void setSelectContentByHashMapper(SelectContentByHashMapper _selectContentByHashMapper) {
        selectContentByHashMapper = _selectContentByHashMapper;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 判断当前用户是不是工单任意一个步骤的待处理人 
    * @param processTaskVo 工单信息
    * @param userType 处理人类型，major主处理人或minor协助处理人
    * @param userUuid 用户
    * @return boolean 
     */
    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, String userType, String userUuid) {
        List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(userUuid);
        List<String> roleUuidList = userMapper.getRoleUuidListByUserUuid(userUuid);
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
        List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(userUuid);
        List<String> roleUuidList = userMapper.getRoleUuidListByUserUuid(userUuid);
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
            for (ProcessTaskStepUserVo processTaskStepUserVo : processTaskStepVo.getUserList()) {
                if (userType == null || userType.equals(processTaskStepUserVo.getUserType())) {
                    if (userUuid.equals(processTaskStepUserVo.getUserUuid())) {
                        return true;
                    }
                }
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
        ProcessTaskOperationType operationType, String userUuid) {
        String stepConfig = processTaskStepVo.getConfig();
        if (StringUtils.isBlank(stepConfig)) {
            String taskConfig = selectContentByHashMapper.getProcessTaskConfigStringByHash(processTaskVo.getConfigHash());
            JSONArray stepList = (JSONArray)JSONPath.read(taskConfig, "process.stepList");
            for (int i = 0; i < stepList.size(); i++) {
                JSONObject stepObj = stepList.getJSONObject(i);
                if (processTaskStepVo.getProcessStepUuid().equals(stepObj.getString("uuid"))) {
                    stepConfig = stepObj.getString("stepConfig");
                    processTaskStepVo.setConfig(stepConfig);
                }
            }
        }
        JSONArray authorityList = (JSONArray)JSONPath.read(stepConfig, "authorityList");
        // 如果步骤自定义权限设置为空，则用组件的全局权限设置
        if (CollectionUtils.isEmpty(authorityList)) {
            IProcessStepUtilHandler processStepUtilHandler =
                ProcessStepUtilHandlerFactory.getHandler(processTaskStepVo.getHandler());
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(processTaskStepVo.getHandler());
            }
            ProcessStepHandlerVo processStepHandlerConfig =
                processStepHandlerMapper.getProcessStepHandlerByHandler(processTaskStepVo.getHandler());
            JSONObject globalConfig = processStepUtilHandler
                .makeupConfig(processStepHandlerConfig != null ? processStepHandlerConfig.getConfig() : null);
            authorityList = (JSONArray)JSONPath.read(JSON.toJSONString(globalConfig), "authorityList");
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
    protected boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo,
        ProcessTaskOperationType operationType, String userUuid) {
        String taskConfig = selectContentByHashMapper.getProcessTaskConfigStringByHash(processTaskVo.getConfigHash());
        JSONArray authorityList =
            (JSONArray)JSONPath.read(taskConfig, "process.processConfig.authorityList");
        if (CollectionUtils.isNotEmpty(authorityList)) {
            return checkOperationAuthIsConfigured(processTaskVo, null, operationType, authorityList, userUuid);
        }
        return false;
    }

    private boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo,
        ProcessTaskOperationType operationType, JSONArray authorityList, String userUuid) {
        for (int i = 0; i < authorityList.size(); i++) {
            JSONObject authorityObj = authorityList.getJSONObject(i);
            String action = authorityObj.getString("action");
            if (operationType.getValue().equals(action)) {
                JSONArray acceptList = authorityObj.getJSONArray("acceptList");
                if (CollectionUtils.isNotEmpty(acceptList)) {
                    List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(userUuid);
                    List<String> roleUuidList = userMapper.getRoleUuidListByUserUuid(userUuid);
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
                    return true;
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
                if (processTaskStepRelVo.getIsHit() == 1) {
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
                            ProcessTaskOperationType.STEP_RETREAT, userUuid)) {
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
    * @Description: 判断userUuid用户是否拥有pProcessTaskStepId步骤的撤回权限 
    * @param processTaskVo 工单信息
    * @param processTaskStepId 步骤id
    * @param userUuid 用户
    * @return boolean
     */
    protected boolean checkCurrentStepIsRetractableByProcessTaskStepId(ProcessTaskVo processTaskVo,
        Long processTaskStepId, String userUuid) {
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
                        return checkIsRetractableStepByProcessTaskStepId(processTaskVo, processTaskStepVo.getId(),
                            userUuid);
                    }
                }
            }
        }
        return false;
    }
}
