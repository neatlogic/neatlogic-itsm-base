package codedriver.framework.process.stephandler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import codedriver.framework.notify.core.INotifyTriggerType;
import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.process.audithandler.core.IProcessTaskAuditType;
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

public abstract class ProcessStepUtilHandlerBase extends ProcessStepHandlerUtilBase implements IProcessStepUtilHandler {

    @Override
    public void activityAudit(ProcessTaskStepVo currentProcessTaskStepVo, IProcessTaskAuditType action) {
        AuditHandler.audit(currentProcessTaskStepVo, action);
    }

    @Override
    public void notify(ProcessTaskStepVo currentProcessTaskStepVo, INotifyTriggerType trigger) {
        NotifyHandler.notify(currentProcessTaskStepVo, trigger);
    }

    @Override
    public void calculateSla(ProcessTaskVo currentProcessTaskVo, boolean isAsync) {
        SlaHandler.calculate(currentProcessTaskVo, isAsync);
    }

    @Override
    public void calculateSla(ProcessTaskStepVo currentProcessTaskStepVo) {
        SlaHandler.calculate(currentProcessTaskStepVo);
    }

    protected abstract IOperationAuthHandlerType MyOperationAuthHandlerType();

    @Override
    public ProcessTaskVo getProcessTaskDetailById(Long processTaskId) {
        // 获取工单基本信息(title、channel_uuid、config_hash、priority_uuid、status、start_time、end_time、expire_time、owner、ownerName、reporter、reporterName)
        ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
        // 判断当前用户是否关注该工单
        if (processTaskVo != null
            && processTaskMapper.checkProcessTaskFocusExists(processTaskId, UserContext.get().getUserUuid()) > 0) {
            processTaskVo.setIsFocus(1);
        }
        // 获取工单流程图信息
        ProcessTaskConfigVo processTaskConfig =
            selectContentByHashMapper.getProcessTaskConfigByHash(processTaskVo.getConfigHash());
        if (processTaskConfig == null) {
            throw new ProcessTaskRuntimeException("没有找到工单：'" + processTaskId + "'的流程图配置信息");
        }
        processTaskVo.setConfig(processTaskConfig.getConfig());

        // 优先级
        PriorityVo priorityVo = priorityMapper.getPriorityByUuid(processTaskVo.getPriorityUuid());
        if (priorityVo == null) {
            priorityVo = new PriorityVo();
            priorityVo.setUuid(processTaskVo.getPriorityUuid());
        }
        processTaskVo.setPriority(priorityVo);
        // 上报服务路径
        ChannelVo channelVo = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
        if (channelVo != null) {
            CatalogVo catalogVo = catalogMapper.getCatalogByUuid(channelVo.getParentUuid());
            if (catalogVo != null) {
                List<CatalogVo> catalogList =
                    catalogMapper.getAncestorsAndSelfByLftRht(catalogVo.getLft(), catalogVo.getRht());
                List<String> nameList = catalogList.stream().map(CatalogVo::getName).collect(Collectors.toList());
                nameList.add(channelVo.getName());
                processTaskVo.setChannelPath(String.join("/", nameList));
            }
            ChannelTypeVo channelTypeVo = channelMapper.getChannelTypeByUuid(channelVo.getChannelTypeUuid());
            if (channelTypeVo == null) {
                channelTypeVo = new ChannelTypeVo();
                channelTypeVo.setUuid(channelVo.getChannelTypeUuid());
            }
            processTaskVo.setChannelType(new ChannelTypeVo(channelTypeVo));
        }
        // 耗时
        if (processTaskVo.getEndTime() != null) {
            long timeCost = worktimeMapper.calculateCostTime(processTaskVo.getWorktimeUuid(),
                processTaskVo.getStartTime().getTime(), processTaskVo.getEndTime().getTime());
            processTaskVo.setTimeCost(timeCost);
            processTaskVo.setTimeCostStr(TimeUtil.millisecondsTranferMaxTimeUnit(timeCost));
        }

        // 获取工单表单信息
        ProcessTaskFormVo processTaskFormVo = processTaskMapper.getProcessTaskFormByProcessTaskId(processTaskId);
        if (processTaskFormVo != null && StringUtils.isNotBlank(processTaskFormVo.getFormContentHash())) {
            String formContent =
                selectContentByHashMapper.getProcessTaskFromContentByHash(processTaskFormVo.getFormContentHash());
            if (StringUtils.isNotBlank(formContent)) {
                processTaskVo.setFormConfig(formContent);
                List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList =
                    processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(processTaskId);
                for (ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo : processTaskFormAttributeDataList) {
                    processTaskVo.getFormAttributeDataMap().put(processTaskFormAttributeDataVo.getAttributeUuid(),
                        processTaskFormAttributeDataVo.getDataObj());
                }
            }
        }
        /** 上报人公司列表 **/
        List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(processTaskVo.getOwner());
        if (CollectionUtils.isNotEmpty(teamUuidList)) {
            List<TeamVo> teamList = teamMapper.getTeamByUuidList(teamUuidList);
            for (TeamVo teamVo : teamList) {
                List<TeamVo> companyList = teamMapper.getAncestorsAndSelfByLftRht(teamVo.getLft(), teamVo.getRht(),
                    TeamLevel.COMPANY.getValue());
                if (CollectionUtils.isNotEmpty(companyList)) {
                    processTaskVo.getOwnerCompanyList().addAll(companyList);
                }
            }
        }
        /** 获取评分信息 */
        String scoreInfo = processTaskMapper.getProcessTaskScoreInfoById(processTaskId);
        processTaskVo.setScoreInfo(scoreInfo);

        /** 转报数据 **/
        Long fromProcessTaskId = processTaskMapper.getFromProcessTaskIdByToProcessTaskId(processTaskId);
        if (fromProcessTaskId != null) {
            processTaskVo.getTranferReportProcessTaskList().add(getFromProcessTasById(fromProcessTaskId));
        }
        List<Long> toProcessTaskIdList = processTaskMapper.getToProcessTaskIdListByFromProcessTaskId(processTaskId);
        for (Long toProcessTaskId : toProcessTaskIdList) {
            ProcessTaskVo toProcessTaskVo = processTaskMapper.getProcessTaskBaseInfoById(toProcessTaskId);
            if (toProcessTaskVo != null) {
                toProcessTaskVo.setTranferReportDirection("to");
                ChannelVo channel = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
                if (channel != null) {
                    ChannelTypeVo channelTypeVo = channelMapper.getChannelTypeByUuid(channel.getChannelTypeUuid());
                    if (channelTypeVo == null) {
                        channelTypeVo = new ChannelTypeVo();
                        channelTypeVo.setUuid(channel.getChannelTypeUuid());
                    }
                    processTaskVo.setChannelType(new ChannelTypeVo(channelTypeVo));
                }
                processTaskVo.getTranferReportProcessTaskList().add(toProcessTaskVo);
            }
        }
        return processTaskVo;
    }

    @Override
    public ProcessTaskStepVo getStartProcessTaskStepByProcessTaskId(Long processTaskId) {
        // 获取开始步骤id
        List<ProcessTaskStepVo> processTaskStepList =
            processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.START.getValue());
        if (processTaskStepList.size() != 1) {
            throw new ProcessTaskRuntimeException("工单：'" + processTaskId + "'有" + processTaskStepList.size() + "个开始步骤");
        }

        ProcessTaskStepVo startProcessTaskStepVo = processTaskStepList.get(0);
        ProcessTaskStepReplyVo comment = new ProcessTaskStepReplyVo();
        // 获取上报描述内容
        List<Long> fileIdList = new ArrayList<>();
        List<ProcessTaskStepContentVo> processTaskStepContentList =
            processTaskMapper.getProcessTaskStepContentByProcessTaskStepId(startProcessTaskStepVo.getId());
        for (ProcessTaskStepContentVo processTaskStepContent : processTaskStepContentList) {
            if (ProcessTaskOperationType.TASK_START.getValue().equals(processTaskStepContent.getType())) {
                fileIdList = processTaskMapper.getFileIdListByContentId(processTaskStepContent.getId());
                comment.setContent(selectContentByHashMapper
                    .getProcessTaskContentStringByHash(processTaskStepContent.getContentHash()));
                break;
            }
        }
        // 附件
        if (CollectionUtils.isNotEmpty(fileIdList)) {
            comment.setFileList(fileMapper.getFileListByIdList(fileIdList));
        }
        startProcessTaskStepVo.setComment(comment);
        /** 当前步骤特有步骤信息 **/
        IProcessStepUtilHandler startProcessStepUtilHandler =
            ProcessStepUtilHandlerFactory.getHandler(startProcessTaskStepVo.getHandler());
        if (startProcessStepUtilHandler == null) {
            throw new ProcessStepHandlerNotFoundException(startProcessTaskStepVo.getHandler());
        }
        startProcessTaskStepVo
            .setHandlerStepInfo(startProcessStepUtilHandler.getHandlerStepInfo(startProcessTaskStepVo));
        return startProcessTaskStepVo;
    }

    @Override
    public ProcessTaskVo getFromProcessTasById(Long processTaskId) {
        ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
        if (processTaskVo != null) {
            ChannelVo channelVo = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
            if (channelVo != null) {
                ChannelTypeVo channelTypeVo = channelMapper.getChannelTypeByUuid(channelVo.getChannelTypeUuid());
                if (channelTypeVo == null) {
                    channelTypeVo = new ChannelTypeVo();
                    channelTypeVo.setUuid(channelVo.getChannelTypeUuid());
                }
                processTaskVo.setChannelType(new ChannelTypeVo(channelTypeVo));
            }
            // 获取工单表单信息
            ProcessTaskFormVo processTaskFormVo = processTaskMapper.getProcessTaskFormByProcessTaskId(processTaskId);
            if (processTaskFormVo != null && StringUtils.isNotBlank(processTaskFormVo.getFormContentHash())) {
                String formContent =
                    selectContentByHashMapper.getProcessTaskFromContentByHash(processTaskFormVo.getFormContentHash());
                if (StringUtils.isNotBlank(formContent)) {
                    processTaskVo.setFormConfig(formContent);
                    List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList =
                        processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(processTaskId);
                    for (ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo : processTaskFormAttributeDataList) {
                        processTaskVo.getFormAttributeDataMap().put(processTaskFormAttributeDataVo.getAttributeUuid(),
                            processTaskFormAttributeDataVo.getDataObj());
                    }
                }
            }

            processTaskVo.setStartProcessTaskStep(getStartProcessTaskStepByProcessTaskId(processTaskId));
            processTaskVo.setTranferReportDirection("from");
        }
        return processTaskVo;
    }

    @Override
    public void getReceiverMap(ProcessTaskStepVo currentProcessTaskStepVo,
        Map<String, List<NotifyReceiverVo>> receiverMap) {
        ProcessTaskVo processTaskVo =
            processTaskMapper.getProcessTaskBaseInfoById(currentProcessTaskStepVo.getProcessTaskId());
        if (processTaskVo != null) {
            /** 上报人 **/
            if (StringUtils.isNotBlank(processTaskVo.getOwner())) {
                receiverMap.computeIfAbsent(ProcessUserType.OWNER.getValue(), k -> new ArrayList<>())
                    .add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskVo.getOwner()));
            }
            /** 代报人 **/
            if (StringUtils.isNotBlank(processTaskVo.getReporter())) {
                receiverMap.computeIfAbsent(ProcessUserType.REPORTER.getValue(), k -> new ArrayList<>())
                    .add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskVo.getReporter()));
            }
        }
        ProcessTaskStepUserVo processTaskStepUser = new ProcessTaskStepUserVo();
        processTaskStepUser.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
        processTaskStepUser.setProcessTaskStepId(currentProcessTaskStepVo.getId());
        /** 主处理人 **/
        processTaskStepUser.setUserType(ProcessUserType.MAJOR.getValue());
        List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserList(processTaskStepUser);
        for (ProcessTaskStepUserVo processTaskStepUserVo : majorUserList) {
            receiverMap.computeIfAbsent(ProcessUserType.MAJOR.getValue(), k -> new ArrayList<>())
                .add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskStepUserVo.getUserUuid()));
        }
        /** 子任务处理人 **/
        processTaskStepUser.setUserType(ProcessUserType.MINOR.getValue());
        List<ProcessTaskStepUserVo> minorUserList = processTaskMapper.getProcessTaskStepUserList(processTaskStepUser);
        for (ProcessTaskStepUserVo processTaskStepUserVo : minorUserList) {
            receiverMap.computeIfAbsent(ProcessUserType.MINOR.getValue(), k -> new ArrayList<>())
                .add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskStepUserVo.getUserUuid()));
        }
        /** 待处理人 **/
        List<ProcessTaskStepWorkerVo> workerList =
            processTaskMapper.getProcessTaskStepWorkerByProcessTaskIdAndProcessTaskStepId(
                currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
        for (ProcessTaskStepWorkerVo processTaskStepWorkerVo : workerList) {
            receiverMap.computeIfAbsent(ProcessUserType.WORKER.getValue(), k -> new ArrayList<>())
                .add(new NotifyReceiverVo(processTaskStepWorkerVo.getType(), processTaskStepWorkerVo.getUuid()));
        }

        /** 工单关注人 */
        List<String> focusUserList =
            processTaskMapper.getFocusUsersOfProcessTask(currentProcessTaskStepVo.getProcessTaskId());
        for (String user : focusUserList) {
            String[] split = user.split("#");
            receiverMap.computeIfAbsent(ProcessUserType.FOCUS_USER.getValue(), k -> new ArrayList<>())
                .add(new NotifyReceiverVo(split[0], split[1]));
        }

        /** 异常处理人 **/
        if (StringUtils.isNotBlank(currentProcessTaskStepVo.getConfig())) {
            String defaultWorker =
                (String)JSONPath.read(currentProcessTaskStepVo.getConfig(), "workerPolicyConfig.defaultWorker");
            if (StringUtils.isNotBlank(defaultWorker)) {
                String[] split = defaultWorker.split("#");
                receiverMap.computeIfAbsent(ProcessUserType.DEFAULT_WORKER.getValue(), k -> new ArrayList<>())
                    .add(new NotifyReceiverVo(split[0], split[1]));
            }
        }
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
    public int saveStepRemind(ProcessTaskStepVo currentProcessTaskStepVo, Long targerProcessTaskStepId, String reason,
        IProcessTaskStepRemindType ation) {
        ProcessTaskStepRemindVo processTaskStepRemindVo = new ProcessTaskStepRemindVo();
        processTaskStepRemindVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
        processTaskStepRemindVo.setProcessTaskStepId(targerProcessTaskStepId);
        processTaskStepRemindVo.setAction(ation.getValue());
        processTaskStepRemindVo.setFcu(UserContext.get().getUserUuid(true));
        String title = ation.getTitle();
        title = title.replace("processTaskStepName", currentProcessTaskStepVo.getName());
        processTaskStepRemindVo.setTitle(title);
        if (StringUtils.isNotBlank(reason)) {
            ProcessTaskContentVo contentVo = new ProcessTaskContentVo(reason);
            processTaskMapper.replaceProcessTaskContent(contentVo);
            processTaskStepRemindVo.setContentHash(contentVo.getHash());
        }
        return processTaskMapper.insertProcessTaskStepRemind(processTaskStepRemindVo);
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
