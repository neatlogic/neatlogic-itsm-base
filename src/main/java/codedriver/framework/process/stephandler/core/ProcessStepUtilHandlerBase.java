package codedriver.framework.process.stephandler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
import codedriver.framework.process.exception.processtask.ProcessTaskNoPermissionException;
import codedriver.framework.process.operationauth.core.IOperationAuthHandlerType;
import codedriver.framework.process.operationauth.core.OperationAuthHandlerType;
import codedriver.framework.process.operationauth.core.ProcessOperateManager;
import codedriver.framework.process.stepremind.core.IProcessTaskStepRemindType;
import codedriver.framework.util.JSONUtil;
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
	@Override
	public List<ProcessTaskOperationType> getOperateList(Long processTaskId, Long processTaskStepId){
        ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
        ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
//        setCurrentUserProcessUserTypeList(processTaskVo, processTaskStepVo);
//        setProcessTaskStepConfig(processTaskStepVo);
        return getOperateList(processTaskVo, processTaskStepVo);
	}
	
	@Override
    public List<ProcessTaskOperationType> getOperateList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo){
        ProcessOperateManager.Builder builder = new ProcessOperateManager.Builder()
            .setNext(OperationAuthHandlerType.TASK);
        if(processTaskStepVo != null) {
            builder.setNext(OperationAuthHandlerType.STEP);
            IOperationAuthHandlerType type = MyOperationAuthHandlerType();
            if(type != null) {
                builder.setNext(type);
            }
        }
        ProcessOperateManager processOperateManager = builder.build();
//        setCurrentUserProcessUserTypeList(processTaskVo, processTaskStepVo);
//        setProcessTaskStepConfig(processTaskStepVo);
        List<ProcessTaskOperationType> resultList = processOperateManager.getOperateList(processTaskVo, processTaskStepVo);
        /** 如果当前用户接受了其他用户的授权，查出其他用户拥有的权限，叠加当前用户权限里 **/
        String userUuid = userMapper.getUserUuidByAgentUuidAndFunc(UserContext.get().getUserUuid(true), "processtask");
        if(StringUtils.isNotBlank(userUuid)) {
            List<String> roleUuidList = userMapper.getRoleUuidListByUserUuid(userUuid);
            String currentUserUuid = UserContext.get().getUserUuid(true);
            String currentUserId = UserContext.get().getUserId(true);
            String currentUserName = UserContext.get().getUserName();
            List<String> currentRoleUuidList = UserContext.get().getRoleUuidList();
            UserContext.get().setUserUuid(userUuid);
            UserContext.get().setUserId(null);
            UserContext.get().setUserName(null);
            UserContext.get().setRoleUuidList(roleUuidList);
//            processTaskVo.getCurrentUserProcessUserTypeList().clear();
//            if(processTaskStepVo != null) {
//                processTaskStepVo.getCurrentUserProcessUserTypeList().clear();                   
//            }
//            setCurrentUserProcessUserTypeList(processTaskVo, processTaskStepVo);
            processTaskVo.getStepList().clear();
            List<ProcessTaskOperationType> operationTypeList = processOperateManager.getOperateList(processTaskVo, processTaskStepVo);
            UserContext.get().setUserUuid(currentUserUuid);
            UserContext.get().setUserId(currentUserId);
            UserContext.get().setUserName(currentUserName);
            UserContext.get().setRoleUuidList(currentRoleUuidList);
            for(ProcessTaskOperationType type : operationTypeList) {
                if(!resultList.contains(type)) {
                    resultList.add(type);
                }
            }
        }
        return resultList;
    }
	
	@Override
    public List<ProcessTaskOperationType> getOperateList(Long processTaskId, Long processTaskStepId, List<ProcessTaskOperationType> operationTypeList){
        ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
        ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
//        setCurrentUserProcessUserTypeList(processTaskVo, processTaskStepVo);
//        setProcessTaskStepConfig(processTaskStepVo);
	    if(CollectionUtils.isNotEmpty(operationTypeList)) {
	        return getOperateList(processTaskVo, processTaskStepVo, operationTypeList);
	    }else {
	        return getOperateList(processTaskVo, processTaskStepVo);
	    }
    }
	
	@Override
    public List<ProcessTaskOperationType> getOperateList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, List<ProcessTaskOperationType> operationTypeList){
        if(CollectionUtils.isNotEmpty(operationTypeList)) {
            ProcessOperateManager.Builder builder = new ProcessOperateManager.Builder();
            if(OperationAuthHandlerType.TASK.getOperationTypeList().removeAll(operationTypeList)) {
                builder.setNext(OperationAuthHandlerType.TASK);
            }
            if(processTaskStepVo != null) {
                if(OperationAuthHandlerType.STEP.getOperationTypeList().removeAll(operationTypeList)) {
                    builder.setNext(OperationAuthHandlerType.STEP);
                }
                IOperationAuthHandlerType type = MyOperationAuthHandlerType();
                if(type != null && CollectionUtils.isNotEmpty(type.getOperationTypeList()) && type.getOperationTypeList().removeAll(operationTypeList)) {
                    builder.setNext(type);
                }
            }
            ProcessOperateManager processOperateManager = builder.build();
//            setCurrentUserProcessUserTypeList(processTaskVo, processTaskStepVo);
//            setProcessTaskStepConfig(processTaskStepVo);
            List<ProcessTaskOperationType> resultList = processOperateManager.getOperateList(processTaskVo, processTaskStepVo, operationTypeList);
            /** 如果当前用户接受了其他用户的授权，查出其他用户拥有的权限，叠加当前用户权限里 **/
            String userUuid = userMapper.getUserUuidByAgentUuidAndFunc(UserContext.get().getUserUuid(true), "processtask");
            if(StringUtils.isNotBlank(userUuid)) {
                List<String> roleUuidList = userMapper.getRoleUuidListByUserUuid(userUuid);
                String currentUserUuid = UserContext.get().getUserUuid(true);
                String currentUserId = UserContext.get().getUserId(true);
                String currentUserName = UserContext.get().getUserName();
                List<String> currentRoleUuidList = UserContext.get().getRoleUuidList();
                UserContext.get().setUserUuid(userUuid);
                UserContext.get().setUserId(null);
                UserContext.get().setUserName(null);
                UserContext.get().setRoleUuidList(roleUuidList);
//                processTaskVo.getCurrentUserProcessUserTypeList().clear();
//                if(processTaskStepVo != null) {
//                    processTaskStepVo.getCurrentUserProcessUserTypeList().clear();                   
//                }
//                setCurrentUserProcessUserTypeList(processTaskVo, processTaskStepVo);
                //processTaskVo.getStepList().clear();//工单中心外层有for 循环 不允许 clear
                List<ProcessTaskOperationType> typeList = processOperateManager.getOperateList(processTaskVo, processTaskStepVo, operationTypeList);
                UserContext.get().setUserUuid(currentUserUuid);
                UserContext.get().setUserId(currentUserId);
                UserContext.get().setUserName(currentUserName);
                UserContext.get().setRoleUuidList(currentRoleUuidList);
                for(ProcessTaskOperationType type : typeList) {
                    if(!resultList.contains(type)) {
                        resultList.add(type);
                    }
                }
            }
            return resultList;
        }else {
            return getOperateList(processTaskVo, processTaskStepVo);
        }
    }
	
    /**
     * 
     * @Time:2020年4月3日
     * @Description: 获取当前用户在当前步骤中工单干系人列表
     * @param processTaskVo     工单信息
     * @param processTaskStepId 步骤id
     * @return List<String>
     */
//	@Override
//    public void setCurrentUserProcessUserTypeList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo) {
//
//        if(!processTaskVo.getCurrentUserProcessUserTypeList().contains(UserType.ALL.getValue())) {
//            processTaskVo.getCurrentUserProcessUserTypeList().add(UserType.ALL.getValue());
//        }
//        if(!processTaskVo.getCurrentUserProcessUserTypeList().contains(ProcessUserType.OWNER.getValue())) {
//            if (UserContext.get().getUserUuid(true).equals(processTaskVo.getOwner())) {
//                processTaskVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.OWNER.getValue());
//            }
//        }
//        
//        if(!processTaskVo.getCurrentUserProcessUserTypeList().contains(ProcessUserType.REPORTER.getValue())) {
//            if (UserContext.get().getUserUuid(true).equals(processTaskVo.getReporter())) {
//                processTaskVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.REPORTER.getValue());
//            }
//        }
//        
//        List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));           
//        if(processTaskStepVo != null) {
//            processTaskStepVo.getCurrentUserProcessUserTypeList().addAll(processTaskVo.getCurrentUserProcessUserTypeList());
//            if(processTaskMapper.checkIsWorker(processTaskVo.getId(), processTaskStepVo.getId(), null, UserContext.get().getUserUuid(true), teamUuidList, UserContext.get().getRoleUuidList()) > 0) {
//                processTaskStepVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.WORKER.getValue());
//                processTaskVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.WORKER.getValue());
//            }else {
//                processTaskStepVo.getCurrentUserProcessUserTypeList().remove(ProcessUserType.WORKER.getValue());
//            }
//            ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(processTaskStepVo.getProcessTaskId(), processTaskStepVo.getId(), UserContext.get().getUserUuid(true));
//            List<ProcessTaskStepUserVo> processTaskStepUserList = processTaskMapper.getProcessTaskStepUserList(processTaskStepUserVo);
//            for(ProcessTaskStepUserVo processTaskStepUser : processTaskStepUserList) {
//                if(ProcessUserType.MAJOR.getValue().equals(processTaskStepUser.getUserType())) {
//                    processTaskStepVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.MAJOR.getValue());
//                }else if(ProcessUserType.MINOR.getValue().equals(processTaskStepUser.getUserType())) {
//                    processTaskStepVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.MINOR.getValue());
//                }
//            }
//            if(!processTaskStepVo.getCurrentUserProcessUserTypeList().contains(ProcessUserType.MAJOR.getValue())) {
//                ProcessTaskStepAgentVo processTaskStepAgentVo = processTaskMapper.getProcessTaskStepAgentByProcessTaskStepId(processTaskStepVo.getId());
//                if(processTaskStepAgentVo != null) {
//                    if(UserContext.get().getUserUuid(true).equals(processTaskStepAgentVo.getUserUuid())) {
//                        processTaskStepVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.MAJOR.getValue());
//                        processTaskStepVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.WORKER.getValue());
//                        processTaskVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.WORKER.getValue());
//                    }
//                }
//            }
//        }
//        
//        if(!processTaskVo.getCurrentUserProcessUserTypeList().contains(ProcessUserType.WORKER.getValue())) {
//            if(processTaskMapper.checkIsWorker(processTaskVo.getId(), null, null, UserContext.get().getUserUuid(true), teamUuidList, UserContext.get().getRoleUuidList()) > 0) {
//                processTaskVo.getCurrentUserProcessUserTypeList().add(ProcessUserType.WORKER.getValue());
//            }
//        } 
//    }
    /**
     * 
    * @Author: linbq
    * @Time:2020年8月24日
    * @Description: 设置步骤配置、处理器全局配置信息 
    * @param processTaskStepVo 
    * @return void
     */
//	@Override
//    public void setProcessTaskStepConfig(ProcessTaskStepVo processTaskStepVo) {
//        if(processTaskStepVo != null) {
//            String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
//            processTaskStepVo.setConfig(stepConfig);
//            IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(processTaskStepVo.getHandler());
//            if(processStepUtilHandler == null) {
//                throw new ProcessStepUtilHandlerNotFoundException(processTaskStepVo.getHandler());
//            }
//            ProcessStepHandlerVo processStepHandlerConfig = processStepHandlerMapper.getProcessStepHandlerByHandler(processTaskStepVo.getHandler());
//            processTaskStepVo.setGlobalConfig(processStepUtilHandler.makeupConfig(processStepHandlerConfig != null ? processStepHandlerConfig.getConfig() : null));
//        }
//    }
	@Override
	public boolean verifyOperationAuthoriy(Long processTaskId, Long processTaskStepId, ProcessTaskOperationType operationType, boolean isThrowException) {
	    List<ProcessTaskOperationType> operationTypeList = new ArrayList<>();
	    operationTypeList.add(operationType);
	    List<ProcessTaskOperationType> resultList = getOperateList(processTaskId, processTaskStepId, operationTypeList);
	    if(resultList.contains(operationType)) {
	        return true;
	    }else {
	        if(isThrowException) {
	            throw new ProcessTaskNoPermissionException(operationType.getText());
	        }else {
	            return false;
	        }
	    }
	}
	
	@Override
    public boolean verifyOperationAuthoriy(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, ProcessTaskOperationType operationType, boolean isThrowException) {
        List<ProcessTaskOperationType> operationTypeList = new ArrayList<>();
        operationTypeList.add(operationType);
        List<ProcessTaskOperationType> resultList = getOperateList(processTaskVo, processTaskStepVo, operationTypeList);
        if(resultList.contains(operationType)) {
            return true;
        }else {
            if(isThrowException) {
                throw new ProcessTaskNoPermissionException(operationType.getText());
            }else {
                return false;
            }
        }
    }
	
	@Override
    public boolean verifyOperationAuthoriy(Long processTaskId, ProcessTaskOperationType operationType, boolean isThrowException) {
	    return verifyOperationAuthoriy(processTaskId, null, operationType, isThrowException);
    }
	@Override
    public boolean verifyOperationAuthoriy(ProcessTaskVo processTaskVo, ProcessTaskOperationType operationType, boolean isThrowException) {
        return verifyOperationAuthoriy(processTaskVo, null, operationType, isThrowException);
    }
	protected abstract IOperationAuthHandlerType MyOperationAuthHandlerType();
	
    @Override
    public ProcessTaskVo getProcessTaskDetailById(Long processTaskId) {
      //获取工单基本信息(title、channel_uuid、config_hash、priority_uuid、status、start_time、end_time、expire_time、owner、ownerName、reporter、reporterName)
        ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
        //获取工单流程图信息
        ProcessTaskConfigVo processTaskConfig = selectContentByHashMapper.getProcessTaskConfigByHash(processTaskVo.getConfigHash());
        if(processTaskConfig == null) {
            throw new ProcessTaskRuntimeException("没有找到工单：'" + processTaskId + "'的流程图配置信息");
        }
        processTaskVo.setConfig(processTaskConfig.getConfig());
        
        //优先级
        PriorityVo priorityVo = priorityMapper.getPriorityByUuid(processTaskVo.getPriorityUuid());
        if(priorityVo == null) {
            priorityVo = new PriorityVo();
            priorityVo.setUuid(processTaskVo.getPriorityUuid());
        }
        processTaskVo.setPriority(priorityVo);
        //上报服务路径
        ChannelVo channelVo = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
        if(channelVo != null) {
            CatalogVo catalogVo = catalogMapper.getCatalogByUuid(channelVo.getParentUuid());
            if(catalogVo != null) {
                List<CatalogVo> catalogList = catalogMapper.getAncestorsAndSelfByLftRht(catalogVo.getLft(), catalogVo.getRht());
                List<String> nameList = catalogList.stream().map(CatalogVo::getName).collect(Collectors.toList());
                nameList.add(channelVo.getName());
                processTaskVo.setChannelPath(String.join("/", nameList));
            }
            ChannelTypeVo channelTypeVo =  channelMapper.getChannelTypeByUuid(channelVo.getChannelTypeUuid());
            if(channelTypeVo == null) {
                channelTypeVo = new ChannelTypeVo();
                channelTypeVo.setUuid(channelVo.getChannelTypeUuid());
            }
            processTaskVo.setChannelType(new ChannelTypeVo(channelTypeVo));
        }
        //耗时
        if(processTaskVo.getEndTime() != null) {
            long timeCost = worktimeMapper.calculateCostTime(processTaskVo.getWorktimeUuid(), processTaskVo.getStartTime().getTime(), processTaskVo.getEndTime().getTime());
            processTaskVo.setTimeCost(timeCost);
            processTaskVo.setTimeCostStr(TimeUtil.millisecondsTranferMaxTimeUnit(timeCost));
        }
        
        //获取工单表单信息
        ProcessTaskFormVo processTaskFormVo = processTaskMapper.getProcessTaskFormByProcessTaskId(processTaskId);
        if(processTaskFormVo != null && StringUtils.isNotBlank(processTaskFormVo.getFormContentHash())) {
            String formContent = selectContentByHashMapper.getProcessTaskFromContentByHash(processTaskFormVo.getFormContentHash());
            if(StringUtils.isNotBlank(formContent)) {
                processTaskVo.setFormConfig(formContent);            
                List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(processTaskId);
                for(ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo : processTaskFormAttributeDataList) {
                    processTaskVo.getFormAttributeDataMap().put(processTaskFormAttributeDataVo.getAttributeUuid(), processTaskFormAttributeDataVo.getDataObj());
                }
            }
        }
        /** 上报人公司列表 **/
        List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(processTaskVo.getOwner());
        if(CollectionUtils.isNotEmpty(teamUuidList)) {
            List<TeamVo> teamList = teamMapper.getTeamByUuidList(teamUuidList);
            for(TeamVo teamVo : teamList) {
                List<TeamVo> companyList = teamMapper.getAncestorsAndSelfByLftRht(teamVo.getLft(), teamVo.getRht(), TeamLevel.COMPANY.getValue());
                if(CollectionUtils.isNotEmpty(companyList)) {
                    processTaskVo.getOwnerCompanyList().addAll(companyList);
                }
            }
        }
        processTaskVo.setStartProcessTaskStep(getStartProcessTaskStepByProcessTaskId(processTaskId));
        return processTaskVo;
    }
    
    private ProcessTaskStepVo getStartProcessTaskStepByProcessTaskId(Long processTaskId) {
        //获取开始步骤id
        List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.START.getValue());
        if(processTaskStepList.size() != 1) {
            throw new ProcessTaskRuntimeException("工单：'" + processTaskId + "'有" + processTaskStepList.size() + "个开始步骤");
        }

        ProcessTaskStepVo startProcessTaskStepVo = processTaskStepList.get(0);
//        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(startProcessTaskStepVo.getConfigHash());
//        startProcessTaskStepVo.setConfig(stepConfig);
//        IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(startProcessTaskStepVo.getHandler());
//        if(processStepUtilHandler == null) {
//            throw new ProcessStepUtilHandlerNotFoundException(startProcessTaskStepVo.getHandler());
//        }
//        ProcessStepHandlerVo processStepHandlerConfig = processStepHandlerMapper.getProcessStepHandlerByHandler(startProcessTaskStepVo.getHandler());
//        startProcessTaskStepVo.setGlobalConfig(processStepUtilHandler.makeupConfig(processStepHandlerConfig != null ? processStepHandlerConfig.getConfig() : null));                    

        ProcessTaskStepReplyVo comment = new ProcessTaskStepReplyVo();
        //获取上报描述内容
        List<Long> fileIdList = new ArrayList<>();
        List<ProcessTaskStepContentVo> processTaskStepContentList = processTaskMapper.getProcessTaskStepContentByProcessTaskStepId(startProcessTaskStepVo.getId());
        for(ProcessTaskStepContentVo processTaskStepContent : processTaskStepContentList) {
            if (ProcessTaskOperationType.STARTPROCESS.getValue().equals(processTaskStepContent.getType())) {
                fileIdList = processTaskMapper.getFileIdListByContentId(processTaskStepContent.getId());
                comment.setContent(selectContentByHashMapper.getProcessTaskContentStringByHash(processTaskStepContent.getContentHash()));
                break;
            }
        }
        //附件       
        if(CollectionUtils.isNotEmpty(fileIdList)) {
            comment.setFileList(fileMapper.getFileListByIdList(fileIdList));
        }
        startProcessTaskStepVo.setComment(comment);
        /** 当前步骤特有步骤信息 **/
        IProcessStepUtilHandler startProcessStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(startProcessTaskStepVo.getHandler());
        if(startProcessStepUtilHandler == null) {
            throw new ProcessStepHandlerNotFoundException(startProcessTaskStepVo.getHandler());
        }
        startProcessTaskStepVo.setHandlerStepInfo(startProcessStepUtilHandler.getHandlerStepInfo(startProcessTaskStepVo));
        return startProcessTaskStepVo;
    }
    
    @Override
    public void getReceiverMap(Long processTaskId, Long processTaskStepId,
        Map<String, List<NotifyReceiverVo>> receiverMap) {
        ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
        if (processTaskVo != null) {
            /** 上报人 **/
            if(StringUtils.isNotBlank(processTaskVo.getOwner())) {
                List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.OWNER.getValue());
                if(notifyReceiverList == null) {
                    notifyReceiverList = new ArrayList<>();
                    receiverMap.put(ProcessUserType.OWNER.getValue(), notifyReceiverList);
                }
                notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskVo.getOwner()));
            }
            /** 代报人 **/
            if(StringUtils.isNotBlank(processTaskVo.getReporter())) {
                List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.REPORTER.getValue());
                if(notifyReceiverList == null) {
                    notifyReceiverList = new ArrayList<>();
                    receiverMap.put(ProcessUserType.REPORTER.getValue(), notifyReceiverList);
                }
                notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskVo.getReporter()));
            }
        }
        /** 主处理人 **/
        List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MAJOR.getValue());
        if (CollectionUtils.isNotEmpty(majorUserList)) {
            List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.MAJOR.getValue());
            if(notifyReceiverList == null) {
                notifyReceiverList = new ArrayList<>();
                receiverMap.put(ProcessUserType.MAJOR.getValue(), notifyReceiverList);
            }
            notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), majorUserList.get(0).getUserUuid()));
        }
        /** 子任务处理人 **/
        List<ProcessTaskStepUserVo> minorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MINOR.getValue());
        if(CollectionUtils.isNotEmpty(minorUserList)) {
            List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.MINOR.getValue());
            if(notifyReceiverList == null) {
                notifyReceiverList = new ArrayList<>();
                receiverMap.put(ProcessUserType.MINOR.getValue(), notifyReceiverList);
            }
            for(ProcessTaskStepUserVo processTaskStepUserVo : minorUserList) {
                notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskStepUserVo.getUserUuid()));
            }
        }
        /** 待办人 **/
        List<ProcessTaskStepUserVo> agentUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.AGENT.getValue());
        if(CollectionUtils.isNotEmpty(agentUserList)) {
            List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.AGENT.getValue());
            if(notifyReceiverList == null) {
                notifyReceiverList = new ArrayList<>();
                receiverMap.put(ProcessUserType.AGENT.getValue(), notifyReceiverList);
            }
            for(ProcessTaskStepUserVo processTaskStepUserVo : agentUserList) {
                notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskStepUserVo.getUserUuid()));
            }
        }
        /** 待处理人 **/
        List<ProcessTaskStepWorkerVo> workerList = processTaskMapper.getProcessTaskStepWorkerByProcessTaskStepId(processTaskStepId);
        if(CollectionUtils.isNotEmpty(workerList)) {
            List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.WORKER.getValue());
            if(notifyReceiverList == null) {
                notifyReceiverList = new ArrayList<>();
                receiverMap.put(ProcessUserType.WORKER.getValue(), notifyReceiverList);
            }
            for(ProcessTaskStepWorkerVo processTaskStepWorkerVo : workerList) {
                notifyReceiverList.add(new NotifyReceiverVo(processTaskStepWorkerVo.getType(), processTaskStepWorkerVo.getUuid()));
            }
        }

        /** 工单关注人 */
        List<String> focusUserList = processTaskMapper.getFocusUsersOfProcessTask(processTaskId);
        if(CollectionUtils.isNotEmpty(focusUserList)){
            focusUserList = focusUserList.stream().map(user -> user.replace("user#","")).collect(Collectors.toList());
            List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.FOCUS_USER.getValue());
            if(notifyReceiverList == null){
                notifyReceiverList = new ArrayList<>();
                receiverMap.put(ProcessUserType.FOCUS_USER.getValue(), notifyReceiverList);
            }
            for(String user : focusUserList){
                notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(),user));
            }
        }
    }
    
    @Override
    public Map<String, String> getCustomButtonMapByProcessTaskStepId(Long processTaskStepId) {
        Map<String, String> customButtonMap = new HashMap<>();
        ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
        if(processTaskStepVo != null) {
            return getCustomButtonMapByConfigHashAndHandler(processTaskStepVo.getConfigHash(), processTaskStepVo.getHandler());
        }          
        return customButtonMap;
    }
    @Override
    public Map<String, String> getCustomButtonMapByConfigHashAndHandler(String configHash, String handler){
        Map<String, String> customButtonMap = new HashMap<>();
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        /** 节点设置按钮映射 **/
        JSONArray customButtonList = JSONUtil.getJSONArray(stepConfig, "customButtonList");
        if(CollectionUtils.isNotEmpty(customButtonList)) {
            for(int i = 0; i < customButtonList.size(); i++) {
                JSONObject customButton = customButtonList.getJSONObject(i);
                String value = customButton.getString("value");
                if(StringUtils.isNotBlank(value)) {
                    customButtonMap.put(customButton.getString("name"), value);
                }
            }
        }
        ProcessStepHandlerVo processStepHandlerConfig = processStepHandlerMapper.getProcessStepHandlerByHandler(handler);
        JSONObject globalConfig = processStepHandlerConfig != null ? processStepHandlerConfig.getConfig() : null;
        IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(handler);
        if(processStepUtilHandler != null) {
            globalConfig = processStepUtilHandler.makeupConfig(globalConfig);
        }
        /** 节点管理按钮映射 **/
        customButtonList = JSONUtil.getJSONArray(globalConfig, "customButtonList");
        if(CollectionUtils.isNotEmpty(customButtonList)) {
            for(int i = 0; i < customButtonList.size(); i++) {
                JSONObject customButton = customButtonList.getJSONObject(i);
                String name = customButton.getString("name");
                if(!customButtonMap.containsKey(name)) {
                    String value = customButton.getString("value");
                    if(StringUtils.isNotBlank(value)) {
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
        JSONArray customButtonList = JSONUtil.getJSONArray(stepConfig, "customStatusList");
        if(CollectionUtils.isNotEmpty(customButtonList)) {
            for(int i = 0; i < customButtonList.size(); i++) {
                JSONObject customButton = customButtonList.getJSONObject(i);
                String name = customButton.getString("name");
                if(name.equals(status)) {
                    String value = customButton.getString("value");
                    if(StringUtils.isNotBlank(value)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }
    @Override
    public int saveStepRemind(ProcessTaskStepVo currentProcessTaskStepVo,Long targerProcessTaskStepId, String reason, IProcessTaskStepRemindType ation) {
        ProcessTaskStepRemindVo processTaskStepRemindVo = new ProcessTaskStepRemindVo();
        processTaskStepRemindVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
        processTaskStepRemindVo.setProcessTaskStepId(targerProcessTaskStepId);
        processTaskStepRemindVo.setAction(ation.getValue());
        processTaskStepRemindVo.setFcu(UserContext.get().getUserUuid(true));
        String title = ation.getTitle();
        title = title.replace("processTaskStepName", currentProcessTaskStepVo.getName());
        processTaskStepRemindVo.setTitle(title);
        if(StringUtils.isNotBlank(reason)) {
            ProcessTaskContentVo contentVo = new ProcessTaskContentVo(reason);
            processTaskMapper.replaceProcessTaskContent(contentVo);
            processTaskStepRemindVo.setContentHash(contentVo.getHash());                            
        }
        return processTaskMapper.insertProcessTaskStepRemind(processTaskStepRemindVo);
    }
    
    @Override
    public Integer getIsRequiredByConfigHash(String configHash) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        return JSONUtil.getInteger(stepConfig, "workerPolicyConfig.isRequired");
    }
    
    @Override
    public Integer getIsNeedContentByConfigHash(String configHash) {
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(configHash);
        return JSONUtil.getInteger(stepConfig, "workerPolicyConfig.isNeedContent");
    }
}
