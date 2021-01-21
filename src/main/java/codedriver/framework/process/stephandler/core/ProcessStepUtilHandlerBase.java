package codedriver.framework.process.stephandler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import codedriver.framework.dao.mapper.TeamMapper;
import codedriver.framework.file.dao.mapper.FileMapper;
import codedriver.framework.process.dao.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.constvalue.TeamLevel;
import codedriver.framework.dto.TeamVo;
import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dto.CatalogVo;
import codedriver.framework.process.dto.ChannelTypeVo;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.PriorityVo;
import codedriver.framework.process.dto.ProcessStepHandlerVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskStepReplyVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepRemindVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.process.ProcessStepHandlerNotFoundException;
import codedriver.framework.process.operationauth.core.IOperationAuthHandlerType;
import codedriver.framework.process.stepremind.core.IProcessTaskStepRemindType;
import codedriver.framework.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ProcessStepUtilHandlerBase implements IProcessStepUtilHandler {
    protected static ProcessTaskMapper processTaskMapper;
    protected static SelectContentByHashMapper selectContentByHashMapper;
    protected static TeamMapper teamMapper;
    protected static WorktimeMapper worktimeMapper;
    protected static FileMapper fileMapper;
    protected static ProcessStepHandlerMapper processStepHandlerMapper;

    @Autowired
    public void setProcessTaskMapper(ProcessTaskMapper _processTaskMapper) {
        processTaskMapper = _processTaskMapper;
    }

    @Autowired
    public void setWorktimeMapper(WorktimeMapper _worktimeMapper) {
        worktimeMapper = _worktimeMapper;
    }

    @Autowired
    public void setFileMapper(FileMapper _fileMapper) {
        fileMapper = _fileMapper;
    }

    @Autowired
    public void setTeamMapper(TeamMapper _teamMapper) {
        teamMapper = _teamMapper;
    }

    @Autowired
    public void setSelectContentByHashMapper(SelectContentByHashMapper _selectContentByHashMapper) {
        selectContentByHashMapper = _selectContentByHashMapper;
    }

    @Autowired
    public void setProcessStepHandlerMapper(ProcessStepHandlerMapper _processStepHandlerMapper) {
        processStepHandlerMapper = _processStepHandlerMapper;
    }

    protected abstract IOperationAuthHandlerType MyOperationAuthHandlerType();
    @Override
    public Map<String, String> getCustomButtonMapByProcessTaskStepId(Long processTaskStepId) {
        Map<String, String> customButtonMap = new HashMap<>();
        ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
        if (processTaskStepVo != null) {
            return getCustomButtonMapByConfigHashAndHandler(processTaskStepVo.getConfigHash(),
                processTaskStepVo.getHandler());
        }
        return customButtonMap;
    }

    @Override
    public Map<String, String> getCustomButtonMapByConfigHashAndHandler(String configHash, String handler) {
        Map<String, String> customButtonMap = new HashMap<>();
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        /** 节点设置按钮映射 **/
        JSONArray customButtonList = (JSONArray)JSONPath.read(stepConfig, "customButtonList");
        if (CollectionUtils.isNotEmpty(customButtonList)) {
            for (int i = 0; i < customButtonList.size(); i++) {
                JSONObject customButton = customButtonList.getJSONObject(i);
                String value = customButton.getString("value");
                if (StringUtils.isNotBlank(value)) {
                    customButtonMap.put(customButton.getString("name"), value);
                }
            }
        }
        ProcessStepHandlerVo processStepHandlerConfig =
            processStepHandlerMapper.getProcessStepHandlerByHandler(handler);
        JSONObject globalConfig = processStepHandlerConfig != null ? processStepHandlerConfig.getConfig() : null;
        IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(handler);
        if (processStepUtilHandler != null) {
            globalConfig = processStepUtilHandler.makeupConfig(globalConfig);
        }
        /** 节点管理按钮映射 **/
        customButtonList = (JSONArray)JSONPath.read(JSON.toJSONString(globalConfig), "customButtonList");
        if (CollectionUtils.isNotEmpty(customButtonList)) {
            for (int i = 0; i < customButtonList.size(); i++) {
                JSONObject customButton = customButtonList.getJSONObject(i);
                String name = customButton.getString("name");
                if (!customButtonMap.containsKey(name)) {
                    String value = customButton.getString("value");
                    if (StringUtils.isNotBlank(value)) {
                        customButtonMap.put(name, value);
                    }
                }

            }
        }
        return customButtonMap;
    }

    @Override
    public String getStatusTextByConfigHashAndHandler(String configHash, String handler, String status) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        /** 节点设置状态映射 **/
        JSONArray customButtonList = (JSONArray)JSONPath.read(stepConfig, "customStatusList");
        if (CollectionUtils.isNotEmpty(customButtonList)) {
            for (int i = 0; i < customButtonList.size(); i++) {
                JSONObject customButton = customButtonList.getJSONObject(i);
                String name = customButton.getString("name");
                if (name.equals(status)) {
                    String value = customButton.getString("value");
                    if (StringUtils.isNotBlank(value)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Integer getIsRequiredByConfigHash(String configHash) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer)JSONPath.read(stepConfig, "workerPolicyConfig.isRequired");
    }

    @Override
    public Integer getIsNeedContentByConfigHash(String configHash) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer)JSONPath.read(stepConfig, "workerPolicyConfig.isNeedContent");
    }
}
