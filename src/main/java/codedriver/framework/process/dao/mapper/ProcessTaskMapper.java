package codedriver.framework.process.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.elasticsearch.annotation.ESParam;
import codedriver.framework.elasticsearch.annotation.ESSearch;
import codedriver.framework.process.dto.ProcessTagVo;
import codedriver.framework.process.dto.ProcessTaskAssignWorkerVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskConvergeVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskImportAuditVo;
import codedriver.framework.process.dto.ProcessTaskRelationVo;
import codedriver.framework.process.dto.ProcessTaskScoreTemplateConfigVo;
import codedriver.framework.process.dto.ProcessTaskScoreTemplateVo;
import codedriver.framework.process.dto.ProcessTaskSlaNotifyVo;
import codedriver.framework.process.dto.ProcessTaskSlaTimeVo;
import codedriver.framework.process.dto.ProcessTaskSlaTransferVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepAgentVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepConfigVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepFileVo;
import codedriver.framework.process.dto.ProcessTaskStepFormAttributeVo;
import codedriver.framework.process.dto.ProcessTaskStepInOperationVo;
import codedriver.framework.process.dto.ProcessTaskStepNotifyPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepRelVo;
import codedriver.framework.process.dto.ProcessTaskStepRemindVo;
import codedriver.framework.process.dto.ProcessTaskStepTimeoutPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskTagVo;
import codedriver.framework.process.dto.ProcessTaskTranferReportVo;
import codedriver.framework.process.dto.ProcessTaskVo;

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

    public ProcessTaskStepAuditDetailVo getProcessTaskStepAuditDetail(@Param("processTaskId") Long processTaskId,
        @Param("type") String type);

    public ProcessTaskVo getProcessTaskBaseInfoById(Long processTaskId);

    public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoByProcessTaskId(Long processTaskId);

    public List<Long> getProcessTaskStepIdByConvergeId(Long convergeId);

    public ProcessTaskFormVo getProcessTaskFormByProcessTaskId(Long processTaskId);

    public List<ProcessTaskFormAttributeDataVo> getProcessTaskStepFormAttributeDataByProcessTaskId(Long processTaskId);

    public List<ProcessTaskStepContentVo> getProcessTaskStepContentProcessTaskId(Long processTaskId);

    public List<ProcessTaskStepContentVo> getProcessTaskStepContentByProcessTaskStepId(Long processTaskStepId);

    public List<ProcessTaskStepUserVo> getProcessTaskStepUserByStepId(
        @Param("processTaskStepId") Long processTaskStepId, @Param("userType") String userType);

    public List<ProcessTaskStepVo> searchProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

    public List<ProcessTaskStepTimeoutPolicyVo>
        getProcessTaskStepTimeoutPolicyByProcessTaskStepId(Long processTaskStepId);

    public List<ProcessTaskStepWorkerPolicyVo>
        getProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

    public List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerByProcessTaskStepId(Long processTaskStepId);

    public Long getProcessTaskLockById(Long processTaskId);

    public int checkProcessTaskConvergeIsExists(ProcessTaskConvergeVo processTaskStepConvergeVo);

    public List<ProcessTaskStepVo> getFromProcessTaskStepByToId(Long toProcessTaskStepId);

    public List<ProcessTaskStepVo> getToProcessTaskStepByFromIdAndType(
        @Param("fromProcessTaskStepId") Long fromProcessTaskStepId, @Param("type") String type);

    public List<ProcessTaskStepVo> getProcessTaskStepByConvergeId(Long convergeId);

    public List<ProcessTaskStepRelVo> getProcessTaskStepRelByFromId(Long fromProcessTaskStepId);

    public List<ProcessTaskStepRelVo> getProcessTaskStepRelByToId(Long toProcessTaskStepId);

    public List<ProcessTaskStepRelVo> getProcessTaskStepRelByProcessTaskId(Long processTaskId);

    public List<ProcessTaskStepVo> getProcessTaskStepByProcessTaskIdAndType(@Param("processTaskId") Long processTaskId,
        @Param("type") String type);

    public List<ProcessTaskStepVo> getProcessTaskActiveStepByProcessTaskIdAndProcessStepType(
        @Param("processTaskId") Long processTaskId, @Param("processStepTypeList") List<String> processStepTypeList,
        @Param("isActive") Integer isActive);

    public ProcessTaskStepVo getProcessTaskStepBaseInfoById(Long processTaskStepId);

    public ProcessTaskVo getProcessTaskById(Long id);

    public List<ProcessTaskStepFormAttributeVo>
        getProcessTaskStepFormAttributeByProcessTaskStepId(Long processTaskStepId);

    public List<ProcessTaskStepAuditVo> getProcessTaskStepAuditList(ProcessTaskStepAuditVo processTaskStepAuditVo);

    public ProcessTaskStepAuditVo getProcessTaskStepAuditById(Long auditId);

    public List<ProcessTaskStepVo> getProcessTaskStepListByProcessTaskId(Long processTaskId);

    public List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerListByUserUuidTeamUuidListRoleUuidList(
        @Param("userUuid") String userUuid, @Param("teamUuidList") List<String> teamUuidList,
        @Param("roleUuidList") List<String> roleUuidList);

    public List<Map<String, Object>> getProcessTaskActiveStepListByStepIdList(@Param("keyword") String keyword,
        @Param("processTaskStepIdList") List<Long> processTaskStepIdList);

    public ProcessTaskFormAttributeDataVo getProcessTaskFormAttributeDataByProcessTaskIdAndAttributeUuid(
        ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo);

    public int checkIsWorker(@Param("processTaskId") Long processTaskId,
        @Param("processTaskStepId") Long processTaskStepId, @Param("userType") String userType,
        @Param("userUuid") String userUuid, @Param("teamUuidList") List<String> teamUuidList,
        @Param("roleUuidList") List<String> roleUuidList);

    public int checkIsProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

    public List<ProcessTaskAssignWorkerVo>
        getPrcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

    public ProcessTaskStepVo getProcessTaskStepBaseInfoByProcessTaskIdAndProcessStepUuid(
        @Param("processTaskId") Long processTaskId, @Param("processStepUuid") String processStepUuid);

    public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoByProcessTaskIdAndProcessStepUuidList(
        @Param("processTaskId") Long processTaskId, @Param("processStepUuidList") List<String> processStepUuidList);

    public List<ProcessTaskStepAuditVo> getProcessTaskAuditList(ProcessTaskStepAuditVo processTaskStepAuditVo);

    public List<ProcessTaskVo> getProcessTaskListByKeywordAndIdList(@Param("keyword") String keyword,
        @Param("processTaskIdList") List<Long> processTaskIdList, @Param("fromDate") String fromDate,
        @Param("toDate") String toDate);

    public List<ProcessTaskStepVo> getProcessTaskStepListByIdList(List<Long> processTaskStepIdList);

    public ProcessTaskStepNotifyPolicyVo
        getProcessTaskStepNotifyPolicy(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

    public Map<String, String> getProcessTaskOldFormAndPropByTaskId(Long processTaskId);

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

    public List<ProcessTaskImportAuditVo>
        searchProcessTaskImportAudit(ProcessTaskImportAuditVo processTaskImportAuditVo);

    public ProcessTaskScoreTemplateVo getProcessTaskScoreTemplateByProcessTaskId(Long processTaskId);

    public List<Long> getSlaIdListByProcessTaskStepId(Long processTaskStepId);
    
    public List<Long> getSlaIdListByProcessTaskId(Long processTaskId);

    public String getProcessTaskSlaConfigById(Long id);

    public List<Long> getProcessTaskStepIdListBySlaId(Long slaId);

    public List<ProcessTaskSlaNotifyVo> getProcessTaskSlaNotifyBySlaId(Long slaId);

    public List<ProcessTaskSlaTransferVo> getProcessTaskSlaTransferBySlaId(Long slaId);

    public Long getProcessTaskSlaLockById(Long slaId);

    public ProcessTaskStepAgentVo getProcessTaskStepAgentByProcessTaskStepId(Long processTaskStepId);

    public int checkProcessTaskFocusExists(@Param("processTaskId") Long processTaskId,
        @Param("userUuid") String userUuid);

    public List<String> getFocusUsersOfProcessTask(Long processTaskId);

    public List<ProcessTaskStepContentVo> getProcessTaskStepContentByProcessTaskId(Long processTaskId);
    
    public List<ProcessTagVo> getProcessTaskTagListByProcessTaskId(@Param("processTaskId") Long processTaskId);

    public int getProcessTaskStepInOperationCountByProcessTaskId(Long processTaskId);
    
    public int replaceProcessTaskConfig(ProcessTaskConfigVo processTaskConfigVo);

    public int replaceProcessTaskOldFormProp(@Param("processTaskId") Long processTaskId, @Param("form") String form,
        @Param("prop") String prop);

    public int insertProcessTaskForm(ProcessTaskFormVo processTaskFormVo);

    @ESSearch
    public int replaceProcessTaskFormContent(@ESParam("processtask") ProcessTaskFormVo processTaskFormVo);

    @ESSearch
    public int insertProcessTask(@ESParam("processtask") ProcessTaskVo processTaskVo);

    @ESSearch
    public int replaceProcessTask(@ESParam("processtask") ProcessTaskVo processTaskVo);

    public int replaceProcessTaskContent(ProcessTaskContentVo processTaskContentVo);

    public int insertProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

    public int replaceProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

    public int insertProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskSlaNotifyVo);

    public int insertProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

    @ESSearch
    public int insertProcessTaskStepUser(@ESParam("processtask") ProcessTaskStepUserVo processTaskStepUserVo);

    public int insertProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

    public int insertProcessTaskStepRel(ProcessTaskStepRelVo processTaskStepRelVo);

    public int insertProcessTaskStepContent(ProcessTaskStepContentVo processTaskStepContentVo);

    public int insertProcessTaskStepAudit(ProcessTaskStepAuditVo processTaskStepAuditVo);

    public int insertProcessTaskStepAuditDetail(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);

    @ESSearch
    public int insertProcessTaskStepWorker(@ESParam("processtask") ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    public int insertProcessTaskConverge(ProcessTaskConvergeVo processTaskConvergeVo);

    public int insertProcessTaskStepTimeoutPolicy(ProcessTaskStepTimeoutPolicyVo processTaskStepTimeoutPolicy);

    public int replaceProcessTaskStepConfig(ProcessTaskStepConfigVo processTaskStepConfigVo);

    @ESSearch
    public int insertProcessTaskStepFormAttribute(
        @ESParam("processtask") ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo);

    public int insertProcessTaskSla(ProcessTaskSlaVo processTaskSlaVo);

    @ESSearch
    public int insertProcessTaskSlaTime(@ESParam("processtask") ProcessTaskSlaTimeVo processTaskSlaTimeVo);

    public int insertProcessTaskStepSla(@Param("processTaskStepId") Long processTaskStepId, @Param("slaId") Long slaId);

    @ESSearch
    public int replaceProcessTaskFormAttributeData(
        @ESParam("processtask") ProcessTaskFormAttributeDataVo processTaskFromAttributeDataVo);

    public int insertProcessTaskStepFile(ProcessTaskStepFileVo processTaskStepFileVo);

    public int insertProcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

    public int replaceProcessTaskStepNotifyPolicyConfig(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

    public int insertProcessTaskStepNotifyPolicy(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

    public int insertProcessTaskTranferReport(ProcessTaskTranferReportVo processTaskTranferReportVo);

    public int replaceProcessTaskRelation(ProcessTaskRelationVo processTaskRelationVo);

    public int insertProcessTaskStepRemind(ProcessTaskStepRemindVo processTaskStepRemindVo);

    public int
        batchInsertProcessTaskImportAudit(@Param("list") List<ProcessTaskImportAuditVo> processTaskImportAuditVos);

    public int insertProcessTaskScoreTemplate(ProcessTaskScoreTemplateVo processTaskScoreTemplateVo);

    public int insertProcessTaskScoreTempleteConfig(ProcessTaskScoreTemplateConfigVo processTaskScoreTemplateConfigVo);

    @ESSearch
    public int insertProcessTaskFocus(@Param("processTask") @ESParam("processtask") ProcessTaskVo processTask,
        @Param("userUuid") String userUuid);
    
    public int insertProcessTaskTag(@Param("processTaskTagList") List<ProcessTaskTagVo> processTaskTagList);

    public int replaceProcesssTaskStepAgent(ProcessTaskStepAgentVo processTaskStepAgentVo);
    
    public int insertProcessTaskStepInOperation(ProcessTaskStepInOperationVo processTaskStepInOperationVo);

//    @ESSearch
//    public int updateProcessTaskStepExpireTime(@ESParam("processtask") ProcessTaskStepVo processTaskStepVo);

    @ESSearch
    public int updateProcessTaskStepStatus(@ESParam("processtask") ProcessTaskStepVo processTaskStepVo);

    @ESSearch
    public int updateProcessTaskStatus(@ESParam("processtask") ProcessTaskVo processTaskVo);

    public int updateProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskNotifyVo);

    @ESSearch
    public int updateProcessTaskSlaTime(@ESParam("processtask") ProcessTaskSlaTimeVo processTaskSlaTimeVo);

    public int updateProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

    public int updateProcessTaskStepRelIsHit(@Param("fromProcessTaskStepId") Long fromProcessTaskStepId,
        @Param("toProcessTaskStepId") Long toProcessTaskStepId, @Param("isHit") Integer isHit);

    public int updateProcessTaskStepConvergeIsCheck(@Param("isCheck") Integer isCheck,
        @Param("convergeId") Long convergeId, @Param("processTaskStepId") Long processTaskStepId);

    @ESSearch
    public int updateProcessTaskStepUserStatus(@ESParam("processtask") ProcessTaskStepUserVo processTaskStepUserVo);
    
    @ESSearch
    public int updateProcessTaskIsShow(@ESParam("processtask") ProcessTaskVo processTaskVo);

    @ESSearch
    public int updateProcessTaskTitleOwnerPriorityUuid(@ESParam("processtask") ProcessTaskVo processTaskVo);

    public int updateProcessTaskStepContentById(ProcessTaskStepContentVo processTaskStepContentVo);

    public int updateProcessTaskStepWorkerUuid(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    public int updateProcessTaskStepUserUserUuid(ProcessTaskStepUserVo processTaskStepUserVo);

    public int updateProcessTaskPriorityUuidById(@Param("id") Long processTaskId, @Param("priorityUuid") String priorityUuid);

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

    @ESSearch
    public int deleteProcessTaskFocus(@Param("processTask") @ESParam("processtask") ProcessTaskVo processTask,
        @Param("userUuid") String userUuid);

    public int deleteProcessTaskStepAgentByProcessTaskStepId(Long processTaskStepId);

    public int deleteProcessTaskAssignWorkerByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskConvergeByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskSlaTransferBySlaId(Long slaId);

    public int deleteProcessTaskSlaNotifyBySlaId(Long slaId);

    public int deleteProcessTaskStepAuditByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskStepContentByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskStepFileByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskStepRemindByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskStepUserByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskStepWorkerByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskFormByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskSlaTimeBySlaId(Long slaId);

    public int deleteProcessTaskStepByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskFocusByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskTagByProcessTaskId(Long processTaskId);
    
    public int deleteProcessTaskStepInOperationByProcessTaskStepIdAndOperationType(ProcessTaskStepInOperationVo processTaskStepInOperationVo);
}
