package codedriver.framework.process.dao.mapper;

import java.util.List;
import java.util.Map;

import codedriver.framework.process.dto.*;
import org.apache.ibatis.annotations.Param;

import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.elasticsearch.annotation.ESSearch;

public interface ProcessTaskMapper {
	public ProcessTaskSlaVo getProcessTaskSlaById(Long slaId);

	public List<ProcessTaskSlaNotifyVo> getAllProcessTaskSlaNotify();

	public List<ProcessTaskSlaTransferVo> getAllProcessTaskSlaTransfer();

	public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoBySlaId(Long slaId);

	public ProcessTaskSlaTimeVo getProcessTaskSlaTimeBySlaId(Long slaId);

	public ProcessTaskSlaNotifyVo getProcessTaskSlaNotifyById(Long id);

	public ProcessTaskSlaTransferVo getProcessTaskSlaTransferById(Long id);

	public List<ProcessTaskSlaVo> getProcessTaskSlaByProcessTaskStepId(Long processTaskStepId);
	
	public List<ProcessTaskSlaVo> getProcessTaskSlaByProcessTaskId(Long processTaskId);

	public List<ProcessTaskSlaTimeVo> getProcessTaskSlaTimeByProcessTaskStepIdList(List<Long> processTaskStepIdList);

	public ProcessTaskStepAuditDetailVo getProcessTaskStepAuditDetail(@Param("processTaskId") Long processTaskId, @Param("type") String type);

	public ProcessTaskVo getProcessTaskBaseInfoById(Long processTaskId);

	public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoByProcessTaskId(Long processTaskId);

	public List<Long> getProcessTaskStepIdByConvergeId(Long convergeId);

	public ProcessTaskFormVo getProcessTaskFormByProcessTaskId(Long processTaskId);

	public List<ProcessTaskFormAttributeDataVo> getProcessTaskStepFormAttributeDataByProcessTaskId(Long processTaskId);

	public List<ProcessTaskStepContentVo> getProcessTaskStepContentProcessTaskId(Long processTaskId);

	public List<ProcessTaskStepContentVo> getProcessTaskStepContentByProcessTaskStepId(Long processTaskStepId);

	public List<ProcessTaskStepUserVo> getProcessTaskStepUserByStepId(@Param("processTaskStepId") Long processTaskStepId, @Param("userType") String userType);

	public List<ProcessTaskStepVo> searchProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

	public List<ProcessTaskStepTimeoutPolicyVo> getProcessTaskStepTimeoutPolicyByProcessTaskStepId(Long processTaskStepId);

	public List<ProcessTaskStepWorkerPolicyVo> getProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

	public List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerByProcessTaskStepId(Long processTaskStepId);

	public Long getProcessTaskLockById(Long processTaskId);

	public int checkProcessTaskConvergeIsExists(ProcessTaskConvergeVo processTaskStepConvergeVo);

	public List<ProcessTaskStepVo> getFromProcessTaskStepByToId(Long toProcessTaskStepId);

	public List<ProcessTaskStepVo> getToProcessTaskStepByFromIdAndType(@Param("fromProcessTaskStepId")Long fromProcessTaskStepId,@Param("type") String type);

	public List<ProcessTaskStepVo> getProcessTaskStepByConvergeId(Long convergeId);

	public List<ProcessTaskStepRelVo> getProcessTaskStepRelByFromId(Long fromProcessTaskStepId);

	public List<ProcessTaskStepRelVo> getProcessTaskStepRelByToId(Long toProcessTaskStepId);

	public List<ProcessTaskStepRelVo> getProcessTaskStepRelByProcessTaskId(Long processTaskId);

	public List<ProcessTaskStepVo> getProcessTaskStepByProcessTaskIdAndType(@Param("processTaskId") Long processTaskId, @Param("type") String type);
	
	public List<ProcessTaskStepVo> getProcessTaskActiveStepByProcessTaskIdAndProcessStepType(@Param("processTaskId") Long processTaskId,@Param("processStepTypeList") List<String> processStepTypeList,@Param("isActive") Integer isActive);

	public ProcessTaskStepVo getProcessTaskStepBaseInfoById(Long processTaskStepId);

	public ProcessTaskVo getProcessTaskById(Long id);

	public List<ProcessTaskStepFormAttributeVo> getProcessTaskStepFormAttributeByProcessTaskStepId(Long processTaskStepId);

	public List<ProcessTaskStepAuditVo> getProcessTaskStepAuditList(ProcessTaskStepAuditVo processTaskStepAuditVo);

	public ProcessTaskStepAuditVo getProcessTaskStepAuditById(Long auditId);

	public List<ProcessTaskStepVo> getProcessTaskStepListByProcessTaskId(Long processTaskId);

	public List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerListByUserUuidTeamUuidListRoleUuidList(
			@Param("userUuid") String userUuid, 
			@Param("teamUuidList") List<String> teamUuidList, 
			@Param("roleUuidList") List<String> roleUuidList
			);

	public List<Map<String, Object>> getProcessTaskActiveStepListByStepIdList(@Param("keyword") String keyword, @Param("processTaskStepIdList") List<Long> processTaskStepIdList);

	public ProcessTaskFormAttributeDataVo getProcessTaskFormAttributeDataByProcessTaskIdAndAttributeUuid(ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo);

	public int checkIsWorker(
			@Param("processTaskId") Long processTaskId, 
			@Param("processTaskStepId") Long processTaskStepId,
			@Param("userType") String userType,
			@Param("userUuid") String userUuid, 
			@Param("teamUuidList") List<String> teamUuidList, 
			@Param("roleUuidList") List<String> roleUuidList
			);
	
	public int checkIsProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

	public List<ProcessTaskAssignWorkerVo> getPrcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

	public ProcessTaskStepVo getProcessTaskStepBaseInfoByProcessTaskIdAndProcessStepUuid(@Param("processTaskId") Long processTaskId, @Param("processStepUuid") String processStepUuid);
	
	public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoByProcessTaskIdAndProcessStepUuidList(@Param("processTaskId") Long processTaskId, @Param("processStepUuidList") List<String> processStepUuidList);
	
	public List<ProcessTaskStepAuditVo> getProcessTaskAuditList(ProcessTaskStepAuditVo processTaskStepAuditVo);

	public List<ProcessTaskVo> getProcessTaskListByKeywordAndIdList(@Param("keyword")String keyword, @Param("processTaskIdList")List<Long> processTaskIdList,@Param("fromDate")String fromDate,@Param("toDate")String toDate);

	public List<ProcessTaskStepVo> getProcessTaskStepListByIdList(List<Long> processTaskStepIdList);

	public ProcessTaskStepNotifyPolicyVo getProcessTaskStepNotifyPolicy(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);
	
	public Map<String,String> getProcessTaskOldFormAndPropByTaskId(Long processTaskId);

	public List<Map<String, Object>> getWorkloadByTeamUuid(String teamUuid);

    public List<Long> getFileIdListByContentId(Long contentId);

    public ProcessTaskStepContentVo getProcessTaskStepContentById(Long id);

    public int checkProcessTaskhasForm(Long processTaskId);

    public List<ProcessTaskStepUserVo> getProcessTaskStepUserList(ProcessTaskStepUserVo processTaskStepUserVo);

	public String getProcessTaskScoreInfoById(Long processtaskId);

	public ProcessTaskVo getProcessTaskAndStepById(Long processtaskId);

    public Long getFromProcessTaskIdByToProcessTaskId(Long toProcessTaskId);

    public List<Long> getToProcessTaskIdListByFromProcessTaskId(Long processTaskId);

    public int getProcessTaskRelationCountByProcessTaskId(Long processTaskId);

    public List<ProcessTaskRelationVo> getProcessTaskRelationList(ProcessTaskRelationVo processTaskRelationVo);

    public List<Long> checkProcessTaskIdListIsExists(List<Long> processTaskIdList);

    public int getProcessTaskCountByKeywordAndChannelUuidList(@Param("basePageVo") BasePageVo basePageVo,
        @Param("channelUuidList") List<String> channelUuidList);

    public List<ProcessTaskVo> getProcessTaskListByKeywordAndChannelUuidList(@Param("basePageVo") BasePageVo basePageVo,
        @Param("channelUuidList") List<String> channelUuidList);

    public ProcessTaskTranferReportVo getProcessTaskTranferReportByToProcessTaskId(Long toProcessTaskId);

    public ProcessTaskRelationVo getProcessTaskRelationById(Long id);

    public List<ProcessTaskStepRemindVo> getProcessTaskStepRemindListByProcessTaskStepId(Long processTaskStepId);

    public int searchProcessTaskImportAuditCount(ProcessTaskImportAuditVo processTaskImportAuditVo);

    public List<ProcessTaskImportAuditVo> searchProcessTaskImportAudit(ProcessTaskImportAuditVo processTaskImportAuditVo);

    public ProcessTaskScoreTemplateVo getProcessTaskScoreTemplateByProcessTaskId(Long processTaskId);

    public List<Long> getSlaIdListByProcessTaskStepId(Long processTaskStepId);

    public String getProcessTaskSlaConfigById(Long id);

    public List<Long> getProcessTaskStepIdListBySlaId(Long slaId);

    public List<ProcessTaskSlaNotifyVo> getProcessTaskSlaNotifyBySlaId(Long slaId);

    public List<ProcessTaskSlaTransferVo> getProcessTaskSlaTransferBySlaId(Long slaId);

    public Long getProcessTaskSlaLockById(Long slaId);

    public ProcessTaskStepAgentVo getProcessTaskStepAgentByProcessTaskStepId(Long processTaskStepId);

    public int checkProcessTaskFocusExists(@Param("processTaskId") Long processTaskId, @Param("userUuid") String userUuid);

    public List<String> getFocusUsersOfProcessTask(Long processTaskId);

	public int replaceProcessTaskConfig(ProcessTaskConfigVo processTaskConfigVo);
	
	public int replaceProcessTaskOldFormProp(@Param("processTaskId")Long processTaskId,@Param("form")String form,@Param("prop")String prop);

	public int insertProcessTaskForm(ProcessTaskFormVo processTaskFormVo);
	
	@ESSearch(type = "processtask",paramType=ProcessTaskFormVo.class)
	public int replaceProcessTaskFormContent(ProcessTaskFormVo processTaskFormVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskVo.class)
	public int insertProcessTask(ProcessTaskVo processTaskVo);
	
	@ESSearch(type = "processtask",paramType=ProcessTaskVo.class)
    public int replaceProcessTask(ProcessTaskVo processTaskVo);

	public int replaceProcessTaskContent(ProcessTaskContentVo processTaskContentVo);

	public int insertProcessTaskStep(ProcessTaskStepVo processTaskStepVo);
	
	public int replaceProcessTaskStep(ProcessTaskStepVo processTaskStepVo);
	
	public int insertProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskSlaNotifyVo);

	public int insertProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskStepUserVo.class)
	public int insertProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

	public int insertProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

	public int insertProcessTaskStepRel(ProcessTaskStepRelVo processTaskStepRelVo);

	public int insertProcessTaskStepContent(ProcessTaskStepContentVo processTaskStepContentVo);

	public int insertProcessTaskStepAudit(ProcessTaskStepAuditVo processTaskStepAuditVo);

	public int insertProcessTaskStepAuditDetail(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskStepWorkerVo.class)
	public int insertProcessTaskStepWorker(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

	public int insertProcessTaskConverge(ProcessTaskConvergeVo processTaskConvergeVo);

	public int insertProcessTaskStepTimeoutPolicy(ProcessTaskStepTimeoutPolicyVo processTaskStepTimeoutPolicy);

	public int replaceProcessTaskStepConfig(ProcessTaskStepConfigVo processTaskStepConfigVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskStepFormAttributeVo.class)
	public int insertProcessTaskStepFormAttribute(ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo);

	public int insertProcessTaskSla(ProcessTaskSlaVo processTaskSlaVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskSlaTimeVo.class)
	public int insertProcessTaskSlaTime(ProcessTaskSlaTimeVo processTaskSlaTimeVo);

	public int insertProcessTaskStepSla(@Param("processTaskStepId") Long processTaskStepId, @Param("slaId") Long slaId);

	@ESSearch(type = "processtask",paramType=ProcessTaskFormAttributeDataVo.class)
	public int replaceProcessTaskFormAttributeData(ProcessTaskFormAttributeDataVo processTaskFromAttributeDataVo);
	
	public int insertProcessTaskStepFile(ProcessTaskStepFileVo processTaskStepFileVo);
	
	public int insertProcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

	public int replaceProcessTaskStepNotifyPolicyConfig(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

	public int insertProcessTaskStepNotifyPolicy(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

    public int insertProcessTaskTranferReport(ProcessTaskTranferReportVo processTaskTranferReportVo);

    public int replaceProcessTaskRelation(ProcessTaskRelationVo processTaskRelationVo);

    public int insertProcessTaskStepRemind(ProcessTaskStepRemindVo processTaskStepRemindVo);

    public int batchInsertProcessTaskImportAudit(@Param("list") List<ProcessTaskImportAuditVo> processTaskImportAuditVos);

    public int insertProcessTaskScoreTemplate(ProcessTaskScoreTemplateVo processTaskScoreTemplateVo);

    public int insertProcessTaskScoreTempleteConfig(ProcessTaskScoreTemplateConfigVo processTaskScoreTemplateConfigVo);

    @ESSearch(type = "processtask",paramType=ProcessTaskVo.class)
    public int insertProcessTaskFocus(@Param("processTask") ProcessTaskVo processTask, @Param("userUuid") String userUuid);

    public int replaceProcesssTaskStepAgent(ProcessTaskStepAgentVo processTaskStepAgentVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskStepVo.class)
	public int updateProcessTaskStepExpireTime(ProcessTaskStepVo processTaskStepVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskStepVo.class)
	public int updateProcessTaskStepStatus(ProcessTaskStepVo processTaskStepVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskVo.class)
	public int updateProcessTaskStatus(ProcessTaskVo processTaskVo);

	public int updateProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskNotifyVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskSlaTimeVo.class)
	public int updateProcessTaskSlaTime(ProcessTaskSlaTimeVo processTaskSlaTimeVo);

	public int updateProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

	public int updateProcessTaskStepRelIsHit(@Param("fromProcessTaskStepId") Long fromProcessTaskStepId, @Param("toProcessTaskStepId") Long toProcessTaskStepId, @Param("isHit") Integer isHit);

	public int updateProcessTaskStepConvergeIsCheck(@Param("isCheck") Integer isCheck, @Param("convergeId") Long convergeId, @Param("processTaskStepId") Long processTaskStepId);

	@ESSearch(type = "processtask",paramType=ProcessTaskStepUserVo.class)
	public int updateProcessTaskStepUserStatus(ProcessTaskStepUserVo processTaskStepUserVo);
	
	@ESSearch(type = "processtask",paramType=ProcessTaskVo.class)
	public int updateProcessTaskTitleOwnerPriorityUuid(ProcessTaskVo processTaskVo);

    public int updateProcessTaskStepContentById(ProcessTaskStepContentVo processTaskStepContentVo);

    public int updateProcessTaskStepWorkerUuid(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    public int updateProcessTaskStepUserUserUuid(ProcessTaskStepUserVo processTaskStepUserVo);

	public int deleteProcessTaskFormAttributeDataByProcessTaskId(Long processTaskId);

	public int deleteProcessTaskStepWorker(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

	public int deleteProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

	public int deleteProcessTaskConvergeByStepId(Long processTaskStepId);

	public int deleteProcessTaskSlaNotifyById(Long slaNotifyId);

	public int deleteProcessTaskSlaTransferById(Long slaTransferId);

	public int deleteProcessTaskStepAuditById(Long auditId);

	public int deleteProcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

    public int deleteProcessTaskStepFileByProcessTaskStepId(Long processTaskStepId);

    public int deleteProcessTaskStepContentByProcessTaskStepId(Long processTaskStepId);

    public int deleteProcessTaskStepFileByContentId(Long contentId);

    public int deleteProcessTaskStepContentById(Long contentId);

    public int deleteProcessTaskRelationById(Long processTaskRelationId);

    public int deleteProcessTaskStepRemind(ProcessTaskStepRemindVo processTaskStepRemindVo);

	@ESSearch(type = "processtask",paramType=ProcessTaskVo.class)
    public int deleteProcessTaskFocus(@Param("processTask") ProcessTaskVo processTask,@Param("userUuid") String userUuid);

    public int deleteProcessTaskStepAgentByProcessTaskStepId(Long processTaskStepId);
    
    public int deleteProcessTaskAssignWorkerByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskConvergeByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskSlaTransferBySlaId(Long slaId);
    
    public int deleteProcessTaskStepAuditByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskStepContentByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskStepFileByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskStepRemindByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskStepUserByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskStepWorkerByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskFormByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskSlaTimeBySlaId(Long processTaskId);
    
    public int deleteProcessTaskStepByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskFocusByProcessTaskId(Long processTaskId);
}
