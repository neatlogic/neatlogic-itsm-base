package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.dao.mapper.TeamMapper;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.file.dao.mapper.FileMapper;
import neatlogic.framework.process.constvalue.ProcessTaskStatus;
import neatlogic.framework.process.constvalue.ProcessTaskStepUserStatus;
import neatlogic.framework.process.constvalue.ProcessUserType;
import neatlogic.framework.process.dao.mapper.ProcessStepHandlerMapper;
import neatlogic.framework.process.dao.mapper.ProcessTaskMapper;
import neatlogic.framework.process.dao.mapper.ProcessTaskStepTaskMapper;
import neatlogic.framework.process.dao.mapper.SelectContentByHashMapper;
import neatlogic.framework.process.dto.*;
import neatlogic.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import neatlogic.framework.worktime.dao.mapper.WorktimeMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ProcessStepInternalHandlerBase implements IProcessStepInternalHandler {
    protected static ProcessTaskMapper processTaskMapper;
    protected static SelectContentByHashMapper selectContentByHashMapper;
    protected static UserMapper userMapper;
    protected static TeamMapper teamMapper;
    protected static WorktimeMapper worktimeMapper;
    protected static FileMapper fileMapper;
    protected static ProcessStepHandlerMapper processStepHandlerMapper;
    protected static ProcessTaskStepTaskMapper processTaskStepTaskMapper;

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
    public void setUserMapper(UserMapper _userMapper) {
        userMapper = _userMapper;
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

    @Autowired
    public void setProcessTaskStepTaskMapper(ProcessTaskStepTaskMapper _processTaskStepTaskMapper) {
        processTaskStepTaskMapper = _processTaskStepTaskMapper;
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
    public Integer getIsNeedUploadFileByConfigHash(String configHash) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer)JSONPath.read(stepConfig, "isNeedUploadFile");
    }

    @Override
    public Integer getEnableReapprovalByConfigHash(String configHash) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        return (Integer)JSONPath.read(stepConfig, "enableReapproval");
    }

    @Override
    public int insertProcessTaskStepInOperation(ProcessTaskStepInOperationVo processTaskStepInOperationVo) {
        return processTaskMapper.insertProcessTaskStepInOperation(processTaskStepInOperationVo);
    }

    protected void defaultUpdateProcessTaskStepUserAndWorker(Long processTaskId, Long processTaskStepId) {

        ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(processTaskStepId, ProcessUserType.MINOR.getValue());
        processTaskMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
        ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo();
        processTaskStepWorkerVo.setProcessTaskId(processTaskId);
        processTaskStepWorkerVo.setProcessTaskStepId(processTaskStepId);
        processTaskStepWorkerVo.setUserType(ProcessUserType.MINOR.getValue());
        processTaskMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);

        List<ProcessTaskStepTaskVo> stepTaskList = processTaskStepTaskMapper.getStepTaskListByProcessTaskStepId(processTaskStepId);
        if (CollectionUtils.isNotEmpty(stepTaskList)) {
            processTaskStepUserVo.setProcessTaskId(processTaskId);
            processTaskStepWorkerVo.setType(GroupSearch.USER.getValue());
            /** 查出processtask_step_task表中当前步骤子任务处理人列表 **/
            Set<String> runningSubtaskUserUuidSet = new HashSet<>();
            Set<String> insertedRunningSubtaskUserUuidSet = new HashSet<>();
            Set<String> insertedsucceedSubtaskUserUuidSet = new HashSet<>();
            List<Long> stepTaskIdList = new ArrayList<>();
            Map<Long, ProcessTaskStepTaskVo> processTaskStepTaskVoMap = new HashMap<>();
            for (ProcessTaskStepTaskVo processTaskStepTaskVo : stepTaskList) {
                stepTaskIdList.add(processTaskStepTaskVo.getId());
                processTaskStepTaskVoMap.put(processTaskStepTaskVo.getId(), processTaskStepTaskVo);
            }

//            List<ProcessTaskStepTaskUserAgentVo> processTaskStepTaskUserAgentList = processTaskStepTaskMapper.getProcessTaskStepTaskUserAgentListByStepTaskIdList(stepTaskIdList);
//            Map<Long, ProcessTaskStepTaskUserAgentVo> processTaskStepTaskUserAgentMap = processTaskStepTaskUserAgentList.stream().collect(Collectors.toMap(e -> e.getProcessTaskStepTaskUserId(), e -> e));
            List<ProcessTaskStepTaskUserVo> stepTaskUserList = processTaskStepTaskMapper.getStepTaskUserByStepTaskIdList(stepTaskIdList);
            for (ProcessTaskStepTaskUserVo stepTaskUserVo : stepTaskUserList) {
                if (Objects.equals(stepTaskUserVo.getIsDelete(), 1)) {
                    continue;
                }
                if (!Objects.equals(stepTaskUserVo.getStatus(), ProcessTaskStatus.SUCCEED.getValue())) {
                    runningSubtaskUserUuidSet.add(stepTaskUserVo.getUserUuid());
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

//                ProcessTaskStepTaskUserAgentVo processTaskStepTaskUserAgentVo = processTaskStepTaskUserAgentMap.get(stepTaskUserVo.getId());
//                if (processTaskStepTaskUserAgentVo != null) {
//                    processTaskStepWorkerVo.setUuid(processTaskStepTaskUserAgentVo.getUserUuid());
//                    processTaskMapper.insertIgnoreProcessTaskStepWorker(processTaskStepWorkerVo);
//                }

                if (Objects.equals(stepTaskUserVo.getStatus(), ProcessTaskStatus.SUCCEED.getValue())) {
                    if (runningSubtaskUserUuidSet.contains(userUuid)) {
                        continue;
                    }
                    if (insertedsucceedSubtaskUserUuidSet.contains(userUuid)) {
                        continue;
                    }
                    insertedsucceedSubtaskUserUuidSet.add(userUuid);
                    processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
                    processTaskStepUserVo.setEndTime(stepTaskUserVo.getEndTime());
                } else {
                    if (insertedRunningSubtaskUserUuidSet.contains(userUuid)) {
                        continue;
                    }
                    insertedRunningSubtaskUserUuidSet.add(userUuid);
                    processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());

                    processTaskStepWorkerVo.setUuid(userUuid);
                    processTaskMapper.insertIgnoreProcessTaskStepWorker(processTaskStepWorkerVo);
                }
                processTaskMapper.insertIgnoreProcessTaskStepUser(processTaskStepUserVo);
            }
        }
//        /** 查出processtask_step_worker表中当前步骤子任务处理人列表 **/
//        Set<String> workerMinorUserUuidSet = new HashSet<>();
//        List<ProcessTaskStepWorkerVo> workerList = processTaskMapper.getProcessTaskStepWorkerByProcessTaskIdAndProcessTaskStepId(processTaskId, processTaskStepId);
//        for (ProcessTaskStepWorkerVo workerVo : workerList) {
//            if (ProcessUserType.MINOR.getValue().equals(workerVo.getUserType())) {
//                workerMinorUserUuidSet.add(workerVo.getUuid());
//            }
//        }
//
//        /** 查出processtask_step_user表中当前步骤子任务处理人列表 **/
//        Set<String> doingMinorUserUuidSet = new HashSet<>();
//        Set<String> doneMinorUserUuidSet = new HashSet<>();
//        List<ProcessTaskStepUserVo> minorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MINOR.getValue());
//        for (ProcessTaskStepUserVo userVo : minorUserList) {
//            if (ProcessTaskStepUserStatus.DOING.getValue().equals(userVo.getStatus())) {
//                doingMinorUserUuidSet.add(userVo.getUserUuid());
//            } else if (ProcessTaskStepUserStatus.DONE.getValue().equals(userVo.getStatus())) {
//                doneMinorUserUuidSet.add(userVo.getUserUuid());
//            }
//        }
//
//        ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo();
//        processTaskStepWorkerVo.setProcessTaskId(processTaskId);
//        processTaskStepWorkerVo.setProcessTaskStepId(processTaskStepId);
//        processTaskStepWorkerVo.setType(GroupSearch.USER.getValue());
//        processTaskStepWorkerVo.setUserType(ProcessUserType.MINOR.getValue());
//
//        ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
//        processTaskStepUserVo.setProcessTaskId(processTaskId);
//        processTaskStepUserVo.setProcessTaskStepId(processTaskStepId);
//        processTaskStepUserVo.setUserType(ProcessUserType.MINOR.getValue());
//        /** 删除processtask_step_worker表中当前步骤多余的子任务处理人 **/
//        List<String> needDeleteUserList = ListUtils.removeAll(workerMinorUserUuidSet, runningSubtaskUserUuidSet);
//        for (String userUuid : needDeleteUserList) {
//            processTaskStepWorkerVo.setUuid(userUuid);
//            processTaskMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
//            if (succeedSubtaskUserUuidSet.contains(userUuid)) {
//                if (doingMinorUserUuidSet.contains(userUuid)) {
//                    /** 完成子任务 **/
//                    processTaskStepUserVo.setUserUuid(userUuid);
//                    processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
//                    processTaskMapper.updateProcessTaskStepUserStatus(processTaskStepUserVo);
//                }
//            } else {
//                if (doingMinorUserUuidSet.contains(userUuid)) {
//                    /** 取消子任务 **/
//                    processTaskStepUserVo.setUserUuid(userUuid);
//                    processTaskMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
//                }
//            }
//        }
//        /** 向processtask_step_worker表中插入当前步骤的子任务处理人 **/
//        List<String> needInsertUserList = ListUtils.removeAll(runningSubtaskUserUuidSet, workerMinorUserUuidSet);
//        for (String userUuid : needInsertUserList) {
//            processTaskStepWorkerVo.setUuid(userUuid);
//            processTaskMapper.insertIgnoreProcessTaskStepWorker(processTaskStepWorkerVo);
//
//            if (doneMinorUserUuidSet.contains(userUuid)) {
//                /** 重做子任务 **/
//                processTaskStepUserVo.setUserUuid(userUuid);
//                processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());
//                processTaskMapper.updateProcessTaskStepUserStatus(processTaskStepUserVo);
//            } else if (!doingMinorUserUuidSet.contains(userUuid)) {
//                /** 创建子任务 **/
//                UserVo userVo = userMapper.getUserBaseInfoByUuid(userUuid);
//                if (userVo != null) {
//                    processTaskStepUserVo.setUserUuid(userVo.getUuid());
//                    processTaskStepUserVo.setUserName(userVo.getUserName());
//                    processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());
//                    processTaskMapper.insertProcessTaskStepUser(processTaskStepUserVo);
//                }
//            }
//        }
    }
}
