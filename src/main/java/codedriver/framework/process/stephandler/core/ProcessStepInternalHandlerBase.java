package codedriver.framework.process.stephandler.core;

import codedriver.framework.dao.mapper.TeamMapper;
import codedriver.framework.file.dao.mapper.FileMapper;
import codedriver.framework.process.dao.mapper.ProcessStepHandlerMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.SelectContentByHashMapper;
import codedriver.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import codedriver.framework.worktime.dao.mapper.WorktimeMapper;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public abstract class ProcessStepInternalHandlerBase implements IProcessStepInternalHandler {
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
        if (MapUtils.isEmpty(customButtonMap)) {
            IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(handler);
            if (processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(handler);
            }
            String processStepHandlerConfig = processStepHandlerMapper.getProcessStepHandlerConfigByHandler(handler);
            JSONObject globalConfig = null;
            if (StringUtils.isNotBlank(processStepHandlerConfig)) {
                globalConfig = JSONObject.parseObject(processStepHandlerConfig);
            }
            globalConfig = processStepUtilHandler.makeupConfig(globalConfig);
            /** 节点管理按钮映射 **/
            customButtonList = globalConfig.getJSONArray("customButtonList");
            if (CollectionUtils.isNotEmpty(customButtonList)) {
                for (int i = 0; i < customButtonList.size(); i++) {
                    JSONObject customButton = customButtonList.getJSONObject(i);
                    String name = customButton.getString("name");
                    if (customButtonMap.containsKey(name)) {
                        continue;
                    }
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
        JSONArray customStatusList = (JSONArray)JSONPath.read(stepConfig, "customStatusList");
        if (CollectionUtils.isNotEmpty(customStatusList)) {
            for (int i = 0; i < customStatusList.size(); i++) {
                JSONObject customStatus = customStatusList.getJSONObject(i);
                String name = customStatus.getString("name");
                if (name.equals(status)) {
                    String value = customStatus.getString("value");
                    if (StringUtils.isNotBlank(value)) {
                        return value;
                    }
                }
            }
        }
        IProcessStepInternalHandler processStepUtilHandler = ProcessStepInternalHandlerFactory.getHandler(handler);
        if (processStepUtilHandler == null) {
            throw new ProcessStepUtilHandlerNotFoundException(handler);
        }
        String processStepHandlerConfig = processStepHandlerMapper.getProcessStepHandlerConfigByHandler(handler);
        JSONObject globalConfig = null;
        if (StringUtils.isNotBlank(processStepHandlerConfig)) {
            globalConfig = JSONObject.parseObject(processStepHandlerConfig);
        }
        globalConfig = processStepUtilHandler.makeupConfig(globalConfig);
        /** 节点管理状态映射 **/
        customStatusList = globalConfig.getJSONArray("customStatusList");
        if (CollectionUtils.isNotEmpty(customStatusList)) {
            for (int i = 0; i < customStatusList.size(); i++) {
                JSONObject customStatus = customStatusList.getJSONObject(i);
                String name = customStatus.getString("name");
                if (name.equals(status)) {
                    String value = customStatus.getString("value");
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
        return (Integer)JSONPath.read(stepConfig, "isRequired");
    }

    @Override
    public Integer getIsNeedContentByConfigHash(String configHash) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer)JSONPath.read(stepConfig, "isNeedContent");
    }

    @Override
    public Integer getEnableReapprovalByConfigHash(String configHash) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer)JSONPath.read(stepConfig, "enableReapproval");
    }
}
