package neatlogic.framework.process.stephandler.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.dao.mapper.TeamMapper;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.file.dao.mapper.FileMapper;
import neatlogic.framework.process.constvalue.*;
import neatlogic.framework.process.crossover.*;
import neatlogic.framework.process.dto.*;
import neatlogic.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import neatlogic.framework.worktime.dao.mapper.WorktimeMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class ProcessStepInternalHandlerBase implements IProcessStepInternalHandler {
    protected static UserMapper userMapper;
    protected static TeamMapper teamMapper;
    protected static WorktimeMapper worktimeMapper;
    protected static FileMapper fileMapper;

    @Autowired
    public void setWorktimeMapper(WorktimeMapper _worktimeMapper) {
        worktimeMapper = _worktimeMapper;
    }

    @Autowired
    public void setFileMapper(FileMapper _fileMapper) {
        fileMapper = _fileMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper _userMapper) {
        userMapper = _userMapper;
    }

    @Autowired
    public void setTeamMapper(TeamMapper _teamMapper) {
        teamMapper = _teamMapper;
    }

    @Override
    public Map<String, String> getCustomButtonMapByProcessTaskStepId(Long processTaskStepId) {
        Map<String, String> customButtonMap = new HashMap<>();
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
        if (processTaskStepVo != null) {
            return getCustomButtonMapByConfigHashAndHandler(processTaskStepVo.getConfigHash(),
                    processTaskStepVo.getHandler());
        }
        return customButtonMap;
    }

    @Override
    public Object getStartStepInfo(ProcessTaskStepVo currentProcessTaskStepVo) {
        return this.getNonStartStepInfo(currentProcessTaskStepVo);
    }

    @Override
    public Object getNonStartStepInfo(ProcessTaskStepVo currentProcessTaskStepVo) {
       /*
       默认返回所有数据和配置信息
        */
        IProcessTaskStepDataCrossoverMapper processTaskStepDataCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskStepDataCrossoverMapper.class);
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        List<ProcessTaskStepDataVo> processTaskStepDataList = processTaskStepDataCrossoverMapper.getProcessTaskStepDataByProcessTaskIdAndStepId(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
        JSONObject returnObj = new JSONObject();
        if (CollectionUtils.isNotEmpty(processTaskStepDataList)) {
            JSONArray dataList = new JSONArray();
            for (ProcessTaskStepDataVo data : processTaskStepDataList) {
                JSONObject dataObj = new JSONObject();
                dataObj.put("type", data.getType());
                dataObj.put("data", data.getData());
                dataList.add(dataObj);
            }
            returnObj.put("dataList", dataList);
        }
        String configHash = currentProcessTaskStepVo.getConfigHash();
        if (StringUtils.isNotBlank(configHash)) {
            String config = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(configHash);
            returnObj.put("config", JSON.parseObject(config));
        }
        return returnObj;
    }

    @Override
    public Map<String, String> getCustomButtonMapByConfigHashAndHandler(String configHash, String handler) {
        Map<String, String> customButtonMap = new HashMap<>();
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(configHash);
        /** 节点设置按钮映射 **/
        JSONArray customButtonList = (JSONArray) JSONPath.read(stepConfig, "customButtonList");
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
            IProcessStepHandlerCrossoverMapper processStepHandlerCrossoverMapper = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverMapper.class);
            String processStepHandlerConfig = processStepHandlerCrossoverMapper.getProcessStepHandlerConfigByHandler(handler);
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
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(configHash);
        /** 节点设置状态映射 **/
        JSONArray customStatusList = (JSONArray) JSONPath.read(stepConfig, "customStatusList");
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
            return null;
        }
        IProcessStepHandlerCrossoverMapper processStepHandlerCrossoverMapper = CrossoverServiceFactory.getApi(IProcessStepHandlerCrossoverMapper.class);
        String processStepHandlerConfig = processStepHandlerCrossoverMapper.getProcessStepHandlerConfigByHandler(handler);
        JSONObject globalConfig = null;
        if (StringUtils.isNotBlank(processStepHandlerConfig)) {
            globalConfig = JSON.parseObject(processStepHandlerConfig);
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
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer) JSONPath.read(stepConfig, "isRequired");
    }

    @Override
    public Integer getIsNeedContentByConfigHash(String configHash) {
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer) JSONPath.read(stepConfig, "isNeedContent");
    }

    @Override
    public Integer getIsNeedUploadFileByConfigHash(String configHash) {
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer) JSONPath.read(stepConfig, "isNeedUploadFile");
    }

    @Override
    public Integer getEnableReapprovalByConfigHash(String configHash) {
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer) JSONPath.read(stepConfig, "enableReapproval");
    }

    @Override
    public String getFormSceneUuidByConfigHash(String configHash) {
        ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
        String stepConfig = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(configHash);
        return (String) JSONPath.read(stepConfig, "formSceneUuid");
    }

    @Override
    public int insertProcessTaskStepInOperation(ProcessTaskStepInOperationVo processTaskStepInOperationVo) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        return processTaskCrossoverMapper.insertProcessTaskStepInOperation(processTaskStepInOperationVo);
    }

    protected void defaultUpdateProcessTaskStepUserAndWorker(Long processTaskId, Long processTaskStepId) {
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        ProcessTaskStepVo processTaskStepVo = processTaskCrossoverMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
        if (Objects.equals(processTaskStepVo.getStatus(), ProcessTaskStepStatus.RUNNING.getValue())) {
            ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(processTaskStepId, ProcessUserType.MINOR.getValue());
            processTaskCrossoverMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
            ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo();
            processTaskStepWorkerVo.setProcessTaskId(processTaskId);
            processTaskStepWorkerVo.setProcessTaskStepId(processTaskStepId);
            processTaskStepWorkerVo.setUserType(ProcessUserType.MINOR.getValue());
            processTaskCrossoverMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
            IProcessTaskStepTaskCrossoverMapper processTaskStepTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskStepTaskCrossoverMapper.class);
            List<ProcessTaskStepTaskVo> stepTaskList = processTaskStepTaskCrossoverMapper.getStepTaskListByProcessTaskStepId(processTaskStepId);
            if (CollectionUtils.isNotEmpty(stepTaskList)) {
                processTaskStepUserVo.setProcessTaskId(processTaskId);
                processTaskStepWorkerVo.setType(GroupSearch.USER.getValue());
                /** 查出processtask_step_task表中当前步骤任务处理人列表 **/
                Set<String> runningTaskUserUuidSet = new HashSet<>();
                Set<String> insertedRunningTaskUserUuidSet = new HashSet<>();
                Set<String> insertedsucceedTaskUserUuidSet = new HashSet<>();
                List<Long> stepTaskIdList = new ArrayList<>();
                Map<Long, ProcessTaskStepTaskVo> processTaskStepTaskVoMap = new HashMap<>();
                for (ProcessTaskStepTaskVo processTaskStepTaskVo : stepTaskList) {
                    stepTaskIdList.add(processTaskStepTaskVo.getId());
                    processTaskStepTaskVoMap.put(processTaskStepTaskVo.getId(), processTaskStepTaskVo);
                }

                List<ProcessTaskStepTaskUserVo> stepTaskUserList = processTaskStepTaskCrossoverMapper.getStepTaskUserByStepTaskIdList(stepTaskIdList);
                for (ProcessTaskStepTaskUserVo stepTaskUserVo : stepTaskUserList) {
                    if (Objects.equals(stepTaskUserVo.getIsDelete(), 1)) {
                        continue;
                    }
                    if (!Objects.equals(stepTaskUserVo.getStatus(), ProcessTaskStepTaskUserStatus.SUCCEED.getValue())) {
                        runningTaskUserUuidSet.add(stepTaskUserVo.getUserUuid());
                    }
                }
                for (ProcessTaskStepTaskUserVo stepTaskUserVo : stepTaskUserList) {
                    if (Objects.equals(stepTaskUserVo.getIsDelete(), 1)) {
                        continue;
                    }
                    ProcessTaskStepTaskVo processTaskStepTaskVo = processTaskStepTaskVoMap.get(stepTaskUserVo.getProcessTaskStepTaskId());
                    processTaskStepUserVo.setActiveTime(processTaskStepTaskVo.getCreateTime());
                    processTaskStepUserVo.setStartTime(processTaskStepTaskVo.getCreateTime());
                    String userUuid = stepTaskUserVo.getUserUuid();
                    processTaskStepUserVo.setUserUuid(userUuid);

                    if (Objects.equals(stepTaskUserVo.getStatus(), ProcessTaskStepTaskUserStatus.SUCCEED.getValue())) {
                        if (runningTaskUserUuidSet.contains(userUuid)) {
                            continue;
                        }
                        if (insertedsucceedTaskUserUuidSet.contains(userUuid)) {
                            continue;
                        }
                        insertedsucceedTaskUserUuidSet.add(userUuid);
                        processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
                        processTaskStepUserVo.setEndTime(stepTaskUserVo.getEndTime());
                    } else {
                        if (insertedRunningTaskUserUuidSet.contains(userUuid)) {
                            continue;
                        }
                        insertedRunningTaskUserUuidSet.add(userUuid);
                        processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());

                        processTaskStepWorkerVo.setUuid(userUuid);
                        processTaskCrossoverMapper.insertIgnoreProcessTaskStepWorker(processTaskStepWorkerVo);
                    }
                    processTaskCrossoverMapper.insertIgnoreProcessTaskStepUser(processTaskStepUserVo);
                }
            }
        } else {
            ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo();
            processTaskStepWorkerVo.setProcessTaskId(processTaskId);
            processTaskStepWorkerVo.setProcessTaskStepId(processTaskStepId);
            processTaskStepWorkerVo.setUserType(ProcessUserType.MINOR.getValue());
            processTaskCrossoverMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
        }
    }

    @Override
    public List<ProcessTaskStepVo> getNextStepList(ProcessTaskStepVo processTaskStepVo, ProcessFlowDirection processFlowDirection) {
        String type = null;
        if (processFlowDirection != null) {
            type = processFlowDirection.getValue();
        }
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        return processTaskCrossoverMapper.getToProcessTaskStepByFromIdAndType(processTaskStepVo.getId(), type);
    }
}
