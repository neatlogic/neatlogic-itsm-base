package codedriver.framework.process.operationauth.core;

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
import codedriver.framework.process.constvalue.ProcessTaskGroupSearch;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dao.mapper.ProcessStepHandlerMapper;
import codedriver.framework.process.dto.ProcessStepHandlerVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import codedriver.framework.process.stephandler.core.IProcessStepUtilHandler;
import codedriver.framework.process.stephandler.core.ProcessStepUtilHandlerFactory;

public abstract class OperationAuthHandlerBase implements IOperationAuthHandler {
    // protected static SelectContentByHashMapper selectContentByHashMapper;
    protected static UserMapper userMapper;
    protected static TeamMapper teamMapper;
    protected static ProcessStepHandlerMapper processStepHandlerMapper;

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

    // @Autowired
    // public void setSelectContentByHashMapper(SelectContentByHashMapper _selectContentByHashMapper) {
    // selectContentByHashMapper = _selectContentByHashMapper;
    // }

    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, Long processTaskStepId, String userType,
        String userUuid) {
        List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(userUuid);
        List<String> roleUuidList = userMapper.getRoleUuidListByUserUuid(userUuid);
        for (ProcessTaskStepVo processTaskStepVo : processTaskVo.getStepList()) {
            if (processTaskStepId == null || processTaskStepId.equals(processTaskStepVo.getId())) {
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
        }
        return false;
    }

    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, String userType, String userUuid) {
        return checkIsWorker(processTaskVo, null, userType, userUuid);
    }

    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, Long processTaskStepId, String userUuid) {
        return checkIsWorker(processTaskVo, processTaskStepId, null, userUuid);
    }

    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, String userUuid) {
        return checkIsWorker(processTaskVo, null, null, userUuid);
    }

    protected boolean checkIsProcessTaskStepUser(ProcessTaskVo processTaskVo, Long processTaskStepId, String userType,
        String userUuid) {
        for (ProcessTaskStepVo processTaskStepVo : processTaskVo.getStepList()) {
            if (processTaskStepId.equals(processTaskStepVo.getId())) {
                for (ProcessTaskStepUserVo processTaskStepUserVo : processTaskStepVo.getUserList()) {
                    if (userType == null || userType.equals(processTaskStepUserVo.getUserType())) {
                        if (userUuid.equals(processTaskStepUserVo.getUserUuid())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo,
        ProcessTaskOperationType operationType, String userUuid) {
        String stepConfig = processTaskStepVo.getConfig();
        if (StringUtils.isNotBlank(stepConfig)) {
            JSONArray stepList = (JSONArray)JSONPath.read(processTaskVo.getConfig(), "process.stepList");
            for (int i = 0; i < stepList.size(); i++) {
                JSONObject stepObj = stepList.getJSONObject(i);
                if (processTaskStepVo.getProcessUuid().equals(stepObj.getString("uuid"))) {
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
            return checkOperationAuthIsConfigured(processTaskVo, processTaskStepVo.getId(), operationType,
                authorityList, userUuid);
        }
        return false;
    }

    protected boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo,
        ProcessTaskOperationType operationType, String userUuid) {
        JSONArray authorityList =
            (JSONArray)JSONPath.read(processTaskVo.getConfig(), "process.processConfig.authorityList");
        if (CollectionUtils.isNotEmpty(authorityList)) {
            return checkOperationAuthIsConfigured(processTaskVo, operationType, authorityList, userUuid);
        }
        return false;
    }

    private boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, ProcessTaskOperationType operationType,
        JSONArray authorityList, String userUuid) {
        return checkOperationAuthIsConfigured(processTaskVo, null, operationType, authorityList, userUuid);
    }

    private boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, Long processTaskStepId,
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
                                if (checkIsProcessTaskStepUser(processTaskVo, processTaskStepId,
                                    ProcessUserType.MAJOR.getValue(), userUuid)) {
                                    return true;
                                }
                            } else if (ProcessUserType.MINOR.getValue().equals(split[1])) {
                                if (checkIsProcessTaskStepUser(processTaskVo, processTaskStepId,
                                    ProcessUserType.MINOR.getValue(), userUuid)) {
                                    return true;
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
}
