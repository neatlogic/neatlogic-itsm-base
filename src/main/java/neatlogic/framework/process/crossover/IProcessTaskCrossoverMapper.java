/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.dto.AuthenticationInfoVo;
import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.process.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface IProcessTaskCrossoverMapper extends ICrossoverService {

    List<ProcessTaskStepVo> getProcessTaskStepBaseInfoByProcessTaskId(Long processTaskId);

    ProcessTaskFormVo getProcessTaskFormByProcessTaskId(Long processTaskId);

    List<ProcessTaskStepUserVo> getProcessTaskStepUserByStepId(
            @Param("processTaskStepId") Long processTaskStepId, @Param("userType") String userType);

    List<ProcessTaskStepWorkerPolicyVo>
    getProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

    List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerByProcessTaskIdAndProcessTaskStepId(
            @Param("processTaskId") Long processTaskId, @Param("processTaskStepId") Long processTaskStepId);

    List<ProcessTaskStepWorkerVo> getProcessTaskStepWorkerListByProcessTaskIdList(List<Long> processTaskIdList);

    int checkProcessTaskStepWorkerIsExistsByPrimaryKey(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    Long getProcessTaskLockById(Long processTaskId);

    int checkProcessTaskConvergeIsExists(ProcessTaskConvergeVo processTaskStepConvergeVo);

    List<ProcessTaskConvergeVo> getProcessTaskConvergeListByStepId(Long processTaskStepId);

    List<ProcessTaskConvergeVo> getProcessTaskConvergeListByProcessTaskId(Long processTaskId);

    List<ProcessTaskStepVo> getToProcessTaskStepByFromIdAndType(
            @Param("fromProcessTaskStepId") Long fromProcessTaskStepId, @Param("type") String type);

    List<Long> getToProcessTaskStepIdListByFromIdAndType(
            @Param("fromProcessTaskStepId") Long fromProcessTaskStepId, @Param("type") String type);

    List<ProcessTaskStepVo> getProcessTaskStepByConvergeId(Long convergeId);

    List<ProcessTaskStepRelVo> getProcessTaskStepRelByFromId(Long fromProcessTaskStepId);

    List<ProcessTaskStepRelVo> getProcessTaskStepRelByToId(Long toProcessTaskStepId);

    List<ProcessTaskStepRelVo> getProcessTaskStepRelListByToIdList(List<Long> toStepIdList);

    List<ProcessTaskStepRelVo> getProcessTaskStepRelByProcessTaskId(Long processTaskId);

    List<ProcessTaskStepRelVo> getProcessTaskStepRelListByProcessTaskIdList(List<Long> processTaskIdList);

    List<ProcessTaskStepVo> getProcessTaskStepByProcessTaskIdAndType(@Param("processTaskId") Long processTaskId,
                                                                     @Param("type") String type);

    ProcessTaskStepVo getProcessTaskStepBaseInfoById(Long processTaskStepId);

    ProcessTaskVo getProcessTaskById(Long id);

    List<ProcessTaskStepVo> getProcessTaskStepListByProcessTaskIdList(List<Long> processTaskIdList);

    Set<Long> getProcessTaskIdSetByChannelUuidListAndAuthenticationInfo(@Param("channelUuidList") List<String> channelUuidList, @Param("authenticationInfoVo") AuthenticationInfoVo authenticationInfoVo);

    int checkIsWorker(@Param("processTaskId") Long processTaskId,
                      @Param("processTaskStepId") Long processTaskStepId, @Param("userType") String userType,
                      @Param("authenticationInfoVo") AuthenticationInfoVo authenticationInfoVo);

    int checkIsProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

    ProcessTaskStepVo getProcessTaskStepBaseInfoByProcessTaskIdAndProcessStepUuid(
            @Param("processTaskId") Long processTaskId, @Param("processStepUuid") String processStepUuid);

    List<ProcessTaskVo> getProcessTaskListByIdList(List<Long> processTaskIdList);

    List<ProcessTaskStepVo> getProcessTaskStepListByIdList(List<Long> processTaskStepIdList);

    List<ProcessTaskStepUserVo> getProcessTaskStepUserListByProcessTaskIdList(List<Long> processTaskIdList);

    ProcessTaskTransferReportVo getProcessTaskTransferReportByToProcessTaskId(Long toProcessTaskId);

    ProcessTaskStepAgentVo getProcessTaskStepAgentByProcessTaskStepId(Long processTaskStepId);

    List<ProcessTaskStepAgentVo> getProcessTaskStepAgentListByProcessTaskIdList(List<Long> processTaskIdList);

    Long getRepeatGroupIdByProcessTaskId(Long processTaskId);

    List<Long> getProcessTaskIdListByRepeatGroupId(Long repeatGroupId);

    Integer getProcessTaskStepReapprovalRestoreBackupMaxSortByBackupStepId(Long processTaskStepId);

    List<ProcessTaskStepReapprovalRestoreBackupVo> getProcessTaskStepReapprovalRestoreBackupListByBackupStepId(Long processTaskStepId);

    List<ProcessTaskVo> getProcessTaskStepVoListByFileId(Long fileId);

    ProcessTaskInvokeVo getInvokeByProcessTaskId(Long processTaskId);

    ProcessTaskStepVo getStartProcessTaskStepByProcessTaskId(Long processTaskId);

    /**
     * 获取工单基本信息（已删除则忽略）
     *
     * @param processTaskId
     * @return
     */
    ProcessTaskVo getProcessTaskBaseInfoById(Long processTaskId);

    List<FileVo> getFileListByProcessTaskId(Long processTaskId);

    List<ProcessTaskStepFileVo> getProcessTaskStepFileListByTaskId(Long taskId);

    List<Long> checkProcessTaskIdListIsExists(List<Long> processTaskIdList);

    List<ProcessTaskStepContentVo> getProcessTaskStepContentByProcessTaskStepId(Long processTaskStepId);

    int getProcessTaskCountByOwner(ProcessTaskVo vo);

    List<ProcessTaskVo> getProcessTaskListByOwner(ProcessTaskVo vo);

    List<ProcessTaskStepVo> getProcessTaskActiveStepByProcessTaskIdAndProcessStepType(
            @Param("processTaskId") Long processTaskId, @Param("processStepTypeList") List<String> processStepTypeList,
            @Param("isActive") Integer isActive);

    List<Long> getFromProcessTaskStepIdListByToId(Long toProcessTaskStepId);

    List<ProcessTaskStepFileVo> getProcessTaskStepFileListByTaskStepId(Long taskId);

    List<ProcessTaskFormVo> getProcessTaskFormListByProcessTaskIdList(List<Long> existsProcessTaskIdList);

    int insertIgnoreProcessTaskConfig(ProcessTaskConfigVo processTaskConfigVo);

    int insertProcessTaskForm(ProcessTaskFormVo processTaskFormVo);

    int insertIgnoreProcessTaskFormContent(ProcessTaskFormVo processTaskFormVo);

    int insertProcessTask(ProcessTaskVo processTaskVo);

    int insertIgnoreProcessTaskContent(ProcessTaskContentVo processTaskContentVo);

    int insertProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

    int insertProcessTaskStepList(List<ProcessTaskStepVo> processTaskStepList);

    int insertProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

    int insertProcessTaskStepWorkerPolicy(ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo);

    int insertProcessTaskStepWorkerPolicyList(List<ProcessTaskStepWorkerPolicyVo> processTaskStepWorkerPolicyList);

    int insertProcessTaskStepRel(ProcessTaskStepRelVo processTaskStepRelVo);

    int insertProcessTaskStepRelList(List<ProcessTaskStepRelVo> processTaskStepRelList);

    int insertIgnoreProcessTaskStepWorker(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    int insertIgnoreProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

    int insertIgnoreProcessTaskConverge(ProcessTaskConvergeVo processTaskConvergeVo);

    int insertIgnoreProcessTaskConvergeList(List<ProcessTaskConvergeVo> processTaskConvergeList);

    int insertIgnoreProcessTaskStepConfig(ProcessTaskStepConfigVo processTaskStepConfigVo);

    int insertIgnoreProcessTaskStepConfigList(List<ProcessTaskStepConfigVo> processTaskStepConfigList);

    int insertProcessTaskTransferReport(ProcessTaskTransferReportVo processTaskTransferReportVo);

    int insertProcessTaskScoreTemplate(ProcessTaskScoreTemplateVo processTaskScoreTemplateVo);

    int insertProcessTaskScoreTemplateConfig(ProcessTaskScoreTemplateConfigVo processTaskScoreTemplateConfigVo);

    int replaceProcessTaskStepAgent(ProcessTaskStepAgentVo processTaskStepAgentVo);

    int insertProcessTaskStepInOperation(ProcessTaskStepInOperationVo processTaskStepInOperationVo);

    int insertProcessTaskStepTag(ProcessTaskStepTagVo processTaskStepTagVo);

    int insertProcessTaskStepTagList(List<ProcessTaskStepTagVo> processTaskStepTagList);

    int insertProcessTaskStepReapprovalRestoreBackup(ProcessTaskStepReapprovalRestoreBackupVo processTaskStepReapprovalRestoreBackupVo);

    void insertProcessTaskTimeCost(ProcessTaskTimeCostVo processTaskTimeCostVo);

    int insertProcessTaskInvoke(@Param("processTaskId") Long processTaskId, @Param("source") String invoke, @Param("sourceType") String invokeType, @Param("invokeId") Long invokeId);

    int replaceProcessTaskRelation(ProcessTaskRelationVo processTaskRelationVo);

    int replaceProcessTaskStep(ProcessTaskStepVo processTaskStepVo);

    int insertProcessTaskStepContent(ProcessTaskStepContentVo processTaskStepContentVo);

    int batchInsertProcessTaskStepAudit(List<ProcessTaskStepAuditVo> list);

    int batchInsertProcessTaskStepAuditDetail(List<ProcessTaskStepAuditDetailVo> list);

    int batchInsertIgnoreProcessTaskContent(List<ProcessTaskContentVo> list);

    int updateProcessTaskStepStatus(ProcessTaskStepVo processTaskStepVo);

    int updateProcessTaskStatus(ProcessTaskVo processTaskVo);

    int updateProcessTaskStepRelIsHit(ProcessTaskStepRelVo processTaskStepRelVo);

    int updateProcessTaskStepUserStatus(ProcessTaskStepUserVo processTaskStepUserVo);

    int updateProcessTaskTitleOwnerPriorityUuid(ProcessTaskVo processTaskVo);

    int updateProcessTaskStepWorkerUuid(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    int updateProcessTaskStepUserUserUuid(ProcessTaskStepUserVo processTaskStepUserVo);

    int updateProcessTaskPriorityUuidById(@Param("id") Long processTaskId,
                                          @Param("priorityUuid") String priorityUuid);

    int deleteProcessTaskStepWorker(ProcessTaskStepWorkerVo processTaskStepWorkerVo);

    int deleteProcessTaskStepUser(ProcessTaskStepUserVo processTaskStepUserVo);

    int deleteProcessTaskConvergeByStepId(Long processTaskStepId);

    int deleteProcessTaskStepFileByProcessTaskStepId(@Param("processTaskId") Long processTaskId, @Param("processTaskStepId") Long processTaskStepId);

    int deleteProcessTaskStepContentByProcessTaskStepId(Long processTaskStepId);

    int deleteProcessTaskStepRemind(ProcessTaskStepRemindVo processTaskStepRemindVo);

    int deleteProcessTaskStepAgentByProcessTaskStepId(Long processTaskStepId);

    int deleteProcessTaskStepInOperationById(Long id);

    int deleteProcessTaskStepReapprovalRestoreBackupByBackupStepId(Long processTaskStepId);

    int deleteProcessTaskTimeCostByProcessTaskId(Long processTaskId);

}
