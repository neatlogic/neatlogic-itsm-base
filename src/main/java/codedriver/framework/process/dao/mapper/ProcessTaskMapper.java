/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dao.mapper;

import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.dto.AuthenticationInfoVo;
import codedriver.framework.process.dto.*;
import codedriver.framework.process.workcenter.dto.WorkcenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProcessTaskMapper {
    public ProcessTaskSlaVo getProcessTaskSlaById(Long slaId);

    public List<ProcessTaskVo> getProcessTaskByStatusList(@Param("statusList") List<String> statusList, @Param("count") Integer count);

    public List<ProcessTaskSlaNotifyVo> getAllProcessTaskSlaNotify();

    public List<ProcessTaskSlaTransferVo> getAllProcessTaskSlaTransfer();

    public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoBySlaId(Long slaId);

    public ProcessTaskSlaTimeVo getProcessTaskSlaTimeBySlaId(Long slaId);

    public ProcessTaskSlaNotifyVo getProcessTaskSlaNotifyById(Long id);

    public ProcessTaskSlaTransferVo getProcessTaskSlaTransferById(Long id);

    public List<ProcessTaskSlaVo> getProcessTaskSlaByProcessTaskStepId(Long processTaskStepId);

    public List<ProcessTaskSlaVo> getProcessTaskSlaByProcessTaskId(Long processTaskId);

    public List<ProcessTaskSlaTimeVo> getProcessTaskSlaTimeByProcessTaskStepIdList(List<Long> processTaskStepIdList);

    public ProcessTaskVo getProcessTaskBaseInfoById(Long processTaskId);


    public List<ProcessTaskVo> getTaskListByIdList(List<Long> idList);

    /**
     * 查询待处理的工单id
     * @param map 工单查询条件
     * @return 工单ID列表
     */
    public List<Long> getProcessingTaskIdListByCondition(@Param("conditionMap") Map<String, Object> map);

    /**
     * 查询待处理的工单数量
     * @param map 工单查询条件
     * @return 工单数量
     */
    public int getProcessingTaskCountByCondition(@Param("conditionMap") Map<String, Object> map);

    public List<ProcessTaskStepVo> getProcessTaskStepBaseInfoByProcessTaskId(Long processTaskId);

//    public List<Long> getProcessTaskStepIdByConvergeId(Long convergeId);

    public ProcessTaskFormVo getProcessTaskFormByProcessTaskId(Long processTaskId);

    List<ProcessTaskFormVo> getProcessTaskFormListByProcessTaskIdList(List<Long> existsProcessTaskIdList);

    public List<ProcessTaskFormAttributeDataVo> getProcessTaskStepFormAttributeDataByProcessTaskId(Long processTaskId);

    List<ProcessTaskFormAttributeDataVo> getProcessTaskStepFormAttributeDataByProcessTaskIdList(List<Long> existsFormProcessTaskIdList);

    public List<ProcessTaskStepContentVo> getProcessTaskStepContentProcessTaskId(Long processTaskId);

    public List<ProcessTaskStepContentVo> getProcessTaskStepContentByProcessTaskStepId(Long processTaskStepId);

    public List<ProcessTaskStepUserVo> getProcessTaskStepUserByStepId(
            @Param("processTaskStepId") Long processTaskStepId, @Param("userType") String userType);

    public List<ProcessTaskStepWorkerPolicyVo>
    getProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

    public List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerByProcessTaskIdAndProcessTaskStepId(
            @Param("processTaskId") Long processTaskId, @Param("processTaskStepId") Long processTaskStepId);

    public List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerListByProcessTaskIdList(List<Long> processTaskIdList);

    public Long getProcessTaskLockById(Long processTaskId);

    public int checkProcessTaskConvergeIsExists(ProcessTaskConvergeVo processTaskStepConvergeVo);

    public List<ProcessTaskStepVo> getFromProcessTaskStepByToId(Long toProcessTaskStepId);

    public List<ProcessTaskStepVo> getToProcessTaskStepByFromIdAndType(
            @Param("fromProcessTaskStepId") Long fromProcessTaskStepId, @Param("type") String type);

    public List<ProcessTaskStepVo> getProcessTaskStepByConvergeId(Long convergeId);

    public List<ProcessTaskStepRelVo> getProcessTaskStepRelByFromId(Long fromProcessTaskStepId);

    public List<ProcessTaskStepRelVo> getProcessTaskStepRelListByFromIdList(List<Long> fromProcessTaskStepIdList);

    public List<ProcessTaskStepRelVo> getProcessTaskStepRelByToId(Long toProcessTaskStepId);

    public List<ProcessTaskStepRelVo> getProcessTaskStepRelByProcessTaskId(Long processTaskId);

    public List<ProcessTaskStepRelVo> getProcessTaskStepRelListByProcessTaskIdList(List<Long> processTaskIdList);

    public ProcessTaskStepVo getStartProcessTaskStepByProcessTaskId(Long processTaskId);

    public ProcessTaskStepVo getEndProcessTaskStepByProcessTaskId(Long processTaskId);

    public List<ProcessTaskStepVo> getProcessTaskStepByProcessTaskIdAndType(@Param("processTaskId") Long processTaskId,
                                                                            @Param("type") String type);

    public List<ProcessTaskStepVo> getProcessTaskActiveStepByProcessTaskIdAndProcessStepType(
            @Param("processTaskId") Long processTaskId, @Param("processStepTypeList") List<String> processStepTypeList,
            @Param("isActive") Integer isActive);

    public ProcessTaskStepVo getProcessTaskStepBaseInfoById(Long processTaskStepId);

    public ProcessTaskVo getProcessTaskById(Long id);

    public List<ProcessTaskStepAuditVo> getProcessTaskStepAuditList(ProcessTaskStepAuditVo processTaskStepAuditVo);

    public List<ProcessTaskStepVo> getProcessTaskStepListByProcessTaskId(Long processTaskId);

    public List<ProcessTaskStepVo> getProcessTaskStepListByProcessTaskIdList(List<Long> processTaskIdList);

    public int getProcessTaskStepWorkerCountByProcessTaskIdUserUuidTeamUuidListRoleUuidList(
            ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    public List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerListByProcessTaskIdUserUuidTeamUuidListRoleUuidList(
            ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    public List<Map<String, Object>> getProcessTaskActiveStepListByStepIdList(@Param("keyword") String keyword,
                                                                              @Param("processTaskStepIdList") List<Long> processTaskStepIdList);

    public ProcessTaskFormAttributeDataVo getProcessTaskFormAttributeDataByProcessTaskIdAndAttributeUuid(
            ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo);

    public int checkIsWorker(@Param("processTaskId") Long processTaskId,
                             @Param("processTaskStepId") Long processTaskStepId, @Param("userType") String userType,
                             @Param("authenticationInfoVo") AuthenticationInfoVo authenticationInfoVo);

    public int checkIsProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

    public List<ProcessTaskAssignWorkerVo>
    getPrcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

    public ProcessTaskStepVo getProcessTaskStepBaseInfoByProcessTaskIdAndProcessStepUuid(
            @Param("processTaskId") Long processTaskId, @Param("processStepUuid") String processStepUuid);

    public List<ProcessTaskStepAuditVo> getProcessTaskAuditList(ProcessTaskStepAuditVo processTaskStepAuditVo);

    public List<ProcessTaskVo> getProcessTaskListByIdListAndStartTime(
            @Param("processTaskIdList") List<Long> processTaskIdList, @Param("fromDate") String fromDate,
            @Param("toDate") String toDate);

    public List<ProcessTaskVo> getProcessTaskListByIdList(List<Long> processTaskIdList);

    public List<ProcessTaskStepVo> getProcessTaskStepListByIdList(List<Long> processTaskStepIdList);

    public ProcessTaskStepNotifyPolicyVo
    getProcessTaskStepNotifyPolicy(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

    public Map<String, String> getProcessTaskOldFormAndPropByTaskId(Long processTaskId);

    public List<Map<String, Object>> getWorkloadByTeamUuid(String teamUuid);

    public List<Long> getFileIdListByContentId(Long contentId);

    public ProcessTaskStepContentVo getProcessTaskStepContentById(Long id);

    public int checkProcessTaskhasForm(Long processTaskId);

    public List<ProcessTaskStepUserVo> getProcessTaskStepUserList(ProcessTaskStepUserVo processTaskStepUserVo);

    public List<ProcessTaskStepUserVo> getProcessTaskStepUserListByProcessTaskIdList(List<Long> processTaskIdList);

    public List<ProcessTaskStepUserVo> getProcessTaskStepUserListByProcessTaskIdListAndStatusList(@Param("processTaskIdList") List<Long> processTaskIdList, @Param("statusList") List<String> statusList);

    public String getProcessTaskScoreInfoById(Long processtaskId);

    public ProcessTaskVo getProcessTaskAndStepById(Long processtaskId);

    public Long getFromProcessTaskIdByToProcessTaskId(Long toProcessTaskId);

    public List<Long> getToProcessTaskIdListByFromProcessTaskId(Long processTaskId);

    public int getProcessTaskRelationCountByProcessTaskId(Long processTaskId);

    public List<ProcessTaskRelationVo> getProcessTaskRelationList(ProcessTaskRelationVo processTaskRelationVo);

    public List<Long> getRelatedProcessTaskIdListByProcessTaskId(Long processTaskId);

    public List<Long> checkProcessTaskIdListIsExists(List<Long> processTaskIdList);

    public int getProcessTaskCountByKeywordAndChannelUuidList(@Param("basePageVo") BasePageVo basePageVo,
                                                              @Param("relatedProcessTaskIdList") List<Long> relatedProcessTaskIdList,
                                                              @Param("channelUuidList") List<String> channelUuidList);

    public List<ProcessTaskVo> getProcessTaskListByKeywordAndChannelUuidList(@Param("basePageVo") BasePageVo basePageVo,
                                                                             @Param("relatedProcessTaskIdList") List<Long> relatedProcessTaskIdList,
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

    public List<String> getFocusUserListByTaskId(Long processTaskId);

    public List<ProcessTaskStepContentVo> getProcessTaskStepContentByProcessTaskId(Long processTaskId);

    public List<ProcessTagVo> getProcessTaskTagListByProcessTaskId(@Param("processTaskId") Long processTaskId);

    public int getProcessTaskStepInOperationCountByProcessTaskId(Long processTaskId);

    public int getProcessTaskCountByChannelTypeUuidAndStartTime(ProcessTaskVo processTaskVo);

    public List<ProcessTaskVo> getProcessTaskListByChannelTypeUuidAndStartTime(ProcessTaskVo processTaskVo);

    public List<ProcessTaskVo> getProcessTaskDetailListByIdList(List<Long> processTaskIdList);

    public String getProcessTaskStepNameById(Long id);

    public Integer getProcessTaskCountBySql(String searchSql);

    public List<Map<String, Object>> getWorkcenterProcessTaskMapBySql(String searchSql);

    public List<ProcessTaskVo> getProcessTaskBySql(String searchSql);

    public List<ProcessTaskVo> getProcessTaskByIndexKeyword(@Param("keywordList") List<String> keywordList, @Param("limit") int limit, @Param("targetType") String targetType,@Param("columnPro") String columnPro);

    public Long getProcessTaskIdByChannelUuidLimitOne(String channelUuid);

    Long getProcessTaskIdByPriorityUuidLimitOne(String prioriryUuid);

    public List<ChannelVo> getChannelReferencedCountList();

    public int insertIgnoreProcessTaskConfig(ProcessTaskConfigVo processTaskConfigVo);

    public int replaceProcessTaskOldFormProp(@Param("processTaskId") Long processTaskId, @Param("form") String form,
                                             @Param("prop") String prop);

    public int insertProcessTaskForm(ProcessTaskFormVo processTaskFormVo);


    public int insertIgnoreProcessTaskFormContent(ProcessTaskFormVo processTaskFormVo);


    public int insertProcessTask(ProcessTaskVo processTaskVo);


    public int replaceProcessTask(ProcessTaskVo processTaskVo);

    public int insertIgnoreProcessTaskContent(ProcessTaskContentVo processTaskContentVo);

    public int insertProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

    public int replaceProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

    public int insertProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskSlaNotifyVo);

    public int insertProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);


    public int insertProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

    public int insertProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

    public int insertProcessTaskStepRel(ProcessTaskStepRelVo processTaskStepRelVo);

    public int insertProcessTaskStepContent(ProcessTaskStepContentVo processTaskStepContentVo);

    public int insertProcessTaskStepAudit(ProcessTaskStepAuditVo processTaskStepAuditVo);

    public int insertProcessTaskStepAuditDetail(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);


    public int insertProcessTaskStepWorker(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    public int insertProcessTaskConverge(ProcessTaskConvergeVo processTaskConvergeVo);

    public int insertIgnoreProcessTaskStepConfig(ProcessTaskStepConfigVo processTaskStepConfigVo);


    public int insertProcessTaskStepFormAttribute(
            ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo);

    public int insertProcessTaskSla(ProcessTaskSlaVo processTaskSlaVo);


    public int insertProcessTaskSlaTime(ProcessTaskSlaTimeVo processTaskSlaTimeVo);

    public int insertProcessTaskStepSla(@Param("processTaskStepId") Long processTaskStepId, @Param("slaId") Long slaId);


    public int replaceProcessTaskFormAttributeData(
            ProcessTaskFormAttributeDataVo processTaskFromAttributeDataVo);

    public int insertProcessTaskStepFile(ProcessTaskStepFileVo processTaskStepFileVo);

    public int insertProcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

    public int insertIgnoreProcessTaskStepNotifyPolicyConfig(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

    public int insertProcessTaskStepNotifyPolicy(ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo);

    public int insertProcessTaskTranferReport(ProcessTaskTranferReportVo processTaskTranferReportVo);

    public int replaceProcessTaskRelation(ProcessTaskRelationVo processTaskRelationVo);

    public int insertProcessTaskStepRemind(ProcessTaskStepRemindVo processTaskStepRemindVo);

    public int
    batchInsertProcessTaskImportAudit(@Param("list") List<ProcessTaskImportAuditVo> processTaskImportAuditVos);

    public int insertProcessTaskScoreTemplate(ProcessTaskScoreTemplateVo processTaskScoreTemplateVo);

    public int insertProcessTaskScoreTempleteConfig(ProcessTaskScoreTemplateConfigVo processTaskScoreTemplateConfigVo);


    public int insertProcessTaskFocus(@Param("processTaskId") Long processTaskId,
                                      @Param("userUuid") String userUuid);

    public int insertProcessTaskTag(@Param("processTaskTagList") List<ProcessTaskTagVo> processTaskTagList);

    public int replaceProcesssTaskStepAgent(ProcessTaskStepAgentVo processTaskStepAgentVo);

    public int insertProcessTaskStepInOperation(ProcessTaskStepInOperationVo processTaskStepInOperationVo);

    int insertProcessTaskStepTag(ProcessTaskStepTagVo processTaskStepTagVo);


    public int updateProcessTaskStepStatus(ProcessTaskStepVo processTaskStepVo);


    public int updateProcessTaskStatus(ProcessTaskVo processTaskVo);

    public int updateProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskNotifyVo);


    public int updateProcessTaskSlaTime(ProcessTaskSlaTimeVo processTaskSlaTimeVo);

    public int updateProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

    public int updateProcessTaskStepRelIsHit(@Param("fromProcessTaskStepId") Long fromProcessTaskStepId,
                                             @Param("toProcessTaskStepId") Long toProcessTaskStepId, @Param("isHit") Integer isHit);

//    public int updateProcessTaskStepConvergeIsCheck(@Param("isCheck") Integer isCheck,
//                                                    @Param("convergeId") Long convergeId, @Param("processTaskStepId") Long processTaskStepId);


    public int updateProcessTaskStepUserStatus(ProcessTaskStepUserVo processTaskStepUserVo);


    public int updateProcessTaskIsShow(ProcessTaskVo processTaskVo);


    public int updateProcessTaskTitleOwnerPriorityUuid(ProcessTaskVo processTaskVo);

    public int updateProcessTaskStepContentById(ProcessTaskStepContentVo processTaskStepContentVo);

    public int updateProcessTaskStepWorkerUuid(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    public int updateProcessTaskStepUserUserUuid(ProcessTaskStepUserVo processTaskStepUserVo);

    public int updateProcessTaskPriorityUuidById(@Param("id") Long processTaskId,
                                                 @Param("priorityUuid") String priorityUuid);


    public int updateProcessTaskSerialNumberById(@Param("id") Long processTaskId,
                                                 @Param("serialNumber") String serialNumber);

    public int deleteProcessTaskFormAttributeDataByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskStepWorker(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    public int deleteProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

    public int deleteProcessTaskConvergeByStepId(Long processTaskStepId);

    public int deleteProcessTaskSlaNotifyById(Long slaNotifyId);

    public int deleteProcessTaskSlaTransferById(Long slaTransferId);

    public int deleteProcessTaskStepAuditById(Long auditId);

    public int deleteProcessTaskAssignWorker(ProcessTaskAssignWorkerVo processTaskAssignWorkerVo);

    public int deleteProcessTaskStepFileByProcessTaskStepId(@Param("processTaskId") Long processTaskId, @Param("processTaskStepId") Long processTaskStepId);

    public int deleteProcessTaskStepContentByProcessTaskStepId(Long processTaskStepId);

    public int deleteProcessTaskStepFileByContentId(Long contentId);

    public int deleteProcessTaskStepContentById(Long contentId);

    public int deleteProcessTaskRelationById(Long processTaskRelationId);

    public int deleteProcessTaskStepRemind(ProcessTaskStepRemindVo processTaskStepRemindVo);


    public int deleteProcessTaskFocus(@Param("processTaskId") Long processTaskId,
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

    public int deleteProcessTaskStepInOperationByProcessTaskStepIdAndOperationType(
            ProcessTaskStepInOperationVo processTaskStepInOperationVo);
}
