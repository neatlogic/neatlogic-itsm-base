package codedriver.framework.process.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import codedriver.framework.elasticsearch.annotation.ElasticSearch;
import codedriver.framework.process.dto.ProcessTaskAssignWorkerVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskConvergeVo;
import codedriver.framework.process.dto.ProcessTaskFileVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskSlaNotifyVo;
import codedriver.framework.process.dto.ProcessTaskSlaTimeVo;
import codedriver.framework.process.dto.ProcessTaskSlaTransferVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepCommentVo;
import codedriver.framework.process.dto.ProcessTaskStepConfigVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepFormAttributeVo;
import codedriver.framework.process.dto.ProcessTaskStepNotifyPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepRelVo;
import codedriver.framework.process.dto.ProcessTaskStepSubtaskContentVo;
import codedriver.framework.process.dto.ProcessTaskStepSubtaskVo;
import codedriver.framework.process.dto.ProcessTaskStepTeamVo;
import codedriver.framework.process.dto.ProcessTaskStepTimeoutPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;

public interface ProcessTaskMapper {
	public ProcessTaskSlaVo getProcessTaskSlaById(Long slaId);

	public List<ProcessTaskSlaNotifyVo> getAllProcessTaskSlaNotify();

	public List<ProcessTaskSlaTransferVo> getAllProcessTaskSlaTransfer();

	public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoBySlaId(Long slaId);

	public ProcessTaskSlaTimeVo getProcessTaskSlaTimeBySlaId(Long slaId);

	public ProcessTaskSlaNotifyVo getProcessTaskNotifyById(Long id);

	public ProcessTaskSlaTransferVo getProcessTaskSlaTransferById(Long id);

	public ProcessTaskConfigVo getProcessTaskConfigByHash(String hash);

	public List<ProcessTaskSlaVo> getProcessTaskSlaByProcessTaskStepId(Long processTaskStepId);
	
	public List<ProcessTaskSlaVo> getProcessTaskSlaByProcessTaskId(Long processTaskId);

	public List<ProcessTaskSlaTimeVo> getProcessTaskSlaTimeByProcessTaskStepIdList(List<Long> processTaskStepIdList);

	public ProcessTaskStepAuditDetailVo getProcessTaskStepAuditDetail(@Param("processTaskId") Long processTaskId, @Param("type") String type);

	public ProcessTaskVo getProcessTaskBaseInfoById(Long processTaskId);

	public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoByProcessTaskId(Long processTaskId);

	public List<Long> getProcessTaskStepIdByConvergeId(Long convergeId);

	public String getProcessTaskStepConfigByHash(String hash);

	public ProcessTaskFormVo getProcessTaskFormByProcessTaskId(Long processTaskId);

	public List<ProcessTaskFormAttributeDataVo> getProcessTaskStepFormAttributeDataByProcessTaskId(Long processTaskId);

	public List<ProcessTaskStepContentVo> getProcessTaskStepContentProcessTaskId(Long processTaskId);

	public ProcessTaskStepContentVo getProcessTaskStepContentByProcessTaskStepId(Long processTaskStepId);
	
	public String getProcessTaskStepContentHashByProcessTaskStepId(Long processTaskStepId);

	public ProcessTaskContentVo getProcessTaskContentByHash(String hash);

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

	public List<ProcessTaskStepFormAttributeVo> getProcessTaskStepFormAttributeByStepId(ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo);

	public ProcessTaskStepVo getProcessTaskStepBaseInfoById(Long processTaskStepId);

	public ProcessTaskVo getProcessTaskById(Long id);
	
	public List<ProcessTaskFileVo> searchProcessTaskFile(ProcessTaskFileVo processTaskFileVo);

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

	public ProcessTaskStepSubtaskVo getProcessTaskStepSubtaskById(Long processTaskStepSubtaskId);

	public List<ProcessTaskStepSubtaskVo> getProcessTaskStepSubtaskList(ProcessTaskStepSubtaskVo processTaskStepSubtaskVo);

	public List<ProcessTaskStepSubtaskContentVo> getProcessTaskStepSubtaskContentBySubtaskId(Long processTaskStepSubtaskId);

	public int checkIsWorker(
			@Param("processTaskId") Long processTaskId, 
			@Param("processTaskStepId") Long processTaskStepId, 
			@Param("userUuid") String userUuid, 
			@Param("teamUuidList") List<String> teamUuidList, 
			@Param("roleUuidList") List<String> roleUuidList
			);
	
	public int checkIsProcessTaskStepUser(
			@Param("processTaskId") Long processTaskId, 
			@Param("processTaskStepId") Long processTaskStepId, 
			@Param("userUuid") String userUuid
			);

	public List<ProcessTaskAssignWorkerVo> getPrcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

	public ProcessTaskStepVo getProcessTaskStepBaseInfoByProcessTaskIdAndProcessStepUuid(@Param("processTaskId") Long processTaskId, @Param("processStepUuid") String processStepUuid);

	public List<ProcessTaskStepCommentVo> getProcessTaskStepCommentListByProcessTaskStepId(Long processTaskStepId);

	public ProcessTaskStepCommentVo getProcessTaskStepCommentById(Long id);
	
	public List<ProcessTaskStepAuditVo> getProcessTaskAuditList(ProcessTaskStepAuditVo processTaskStepAuditVo);

	public List<ProcessTaskVo> getProcessTaskListByKeywordAndIdList(@Param("keyword")String keyword, @Param("processTaskIdList")List<Long> processTaskIdList,@Param("fromDate")String fromDate,@Param("toDate")String toDate);

	public List<ProcessTaskStepVo> getProcessTaskStepListByIdList(List<Long> processTaskStepIdList);

	public ProcessTaskStepNotifyPolicyVo getProcessTaskStepNotifyPolicy(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

	public List<Map<String, Object>> getWorkloadByTeamUuid(String teamUuid);
	
	public int replaceProcessTaskConfig(ProcessTaskConfigVo processTaskConfigVo);

	public int insertProcessTaskForm(ProcessTaskFormVo processTaskFormVo);
	
	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskFormVo.class)
	public int replaceProcessTaskFormContent(ProcessTaskFormVo processTaskFormVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskVo.class)
	public int insertProcessTask(ProcessTaskVo processTaskVo);

	public int replaceProcessTaskContent(ProcessTaskContentVo processTaskContentVo);

	public int insertProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

	public int insertProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskSlaNotifyVo);

	public int insertProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskStepUserVo.class)
	public int insertProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

	public int insertProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskStepTeamVo.class)
	public int insertProcessTaskStepTeam(ProcessTaskStepTeamVo processTaskStepTeamVo);

	public int insertProcessTaskStepRel(ProcessTaskStepRelVo processTaskStepRelVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskStepContentVo.class)
	public int replaceProcessTaskStepContent(ProcessTaskStepContentVo processTaskStepContentVo);

	public int insertProcessTaskStepAudit(ProcessTaskStepAuditVo processTaskStepAuditVo);

	public int insertProcessTaskStepAuditDetail(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);

//	public int insertProcessTaskStepAuditFormAttributeData(ProcessTaskStepAuditFormAttributeDataVo processTaskStepAuditFormAttributeDataVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskStepWorkerVo.class)
	public int insertProcessTaskStepWorker(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

	public int insertProcessTaskConverge(ProcessTaskConvergeVo processTaskConvergeVo);

	public int insertProcessTaskStepTimeoutPolicy(ProcessTaskStepTimeoutPolicyVo processTaskStepTimeoutPolicy);

	public int replaceProcessTaskStepConfig(ProcessTaskStepConfigVo processTaskStepConfigVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskStepFormAttributeVo.class)
	public int insertProcessTaskStepFormAttribute(ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo);

	public int insertProcessTaskSla(ProcessTaskSlaVo processTaskSlaVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskSlaTimeVo.class)
	public int insertProcessTaskSlaTime(ProcessTaskSlaTimeVo processTaskSlaTimeVo);

	public int insertProcessTaskStepSla(@Param("processTaskStepId") Long processTaskStepId, @Param("slaId") Long slaId);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskFormAttributeDataVo.class)
	public int replaceProcessTaskFormAttributeData(ProcessTaskFormAttributeDataVo processTaskFromAttributeDataVo);
	
	public int insertProcessTaskFile(ProcessTaskFileVo processTaskFileVo);

	public int insertProcessTaskStepSubtask(ProcessTaskStepSubtaskVo processTaskStepSubtaskVo);

	public int insertProcessTaskStepSubtaskContent(ProcessTaskStepSubtaskContentVo processTaskStepSubtaskContentVo);
	
	public int insertProcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

	public int insertProcessTaskStepComment(ProcessTaskStepCommentVo processTaskStepCommentVo);

	public int replaceProcessTaskStepNotifyPolicyConfig(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

	public int insertProcessTaskStepNotifyPolicy(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);
	
	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskStepVo.class)
	public int updateProcessTaskStepExpireTime(ProcessTaskStepVo processTaskStepVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskStepVo.class)
	public int updateProcessTaskStepStatus(ProcessTaskStepVo processTaskStepVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskVo.class)
	public int updateProcessTaskStatus(ProcessTaskVo processTaskVo);

	public int updateProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskNotifyVo);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskSlaTimeVo.class)
	public int updateProcessTaskSlaTime(ProcessTaskSlaTimeVo processTaskSlaTimeVo);

	public int updateProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

	public int updateProcessTaskStepRelIsHit(@Param("fromProcessTaskStepId") Long fromProcessTaskStepId, @Param("toProcessTaskStepId") Long toProcessTaskStepId, @Param("isHit") Integer isHit);

	public int updateProcessTaskStepConvergeIsCheck(@Param("isCheck") Integer isCheck, @Param("convergeId") Long convergeId, @Param("processTaskStepId") Long processTaskStepId);

	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskStepUserVo.class)
	public int updateProcessTaskStepUserStatus(ProcessTaskStepUserVo processTaskStepUserVo);
	
	@ElasticSearch(type = "processtask-update",paramType=ProcessTaskVo.class)
	public int updateProcessTaskTitleOwnerPriorityUuid(ProcessTaskVo processTaskVo);

	public int updateProcessTaskStepSubtaskStatus(ProcessTaskStepSubtaskVo processTaskStepSubtaskVo);

	public int updateProcessTaskStepSubtaskContent(ProcessTaskStepSubtaskContentVo processTaskStepSubtaskContentVo);

	public int updateProcessTaskStepCommentById(ProcessTaskStepCommentVo processTaskStepCommentVo);

	public int deleteProcessTaskFormAttributeValueByProcessTaskIdAndAttributeUuid(@Param("processTaskId") Long processTaskId, @Param("attributeUuid") String attributeUuid);

	public int deleteProcessTaskStepWorker(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

	public int deleteProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

	public int deleteProcessTaskConvergeByStepId(Long processTaskStepId);

	public int deleteProcessTaskSlaNotifyById(Long slaNotifyId);

	public int deleteProcessTaskSlaTransferById(Long slaTransferId);
	
	public int deleteProcessTaskFile(ProcessTaskFileVo processTaskFileVo);

	public int deleteProcessTaskStepAuditById(Long auditId);
	
	public int deleteProcessTaskStepContent(ProcessTaskStepContentVo processTaskStepContentVo);

	public int deleteProcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

	public int deleteProcessTaskStepCommentById(Long id);
}
