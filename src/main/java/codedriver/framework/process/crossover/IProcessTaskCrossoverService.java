/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.crossover;

import codedriver.framework.crossover.ICrossoverService;
import codedriver.framework.dto.AuthenticationInfoVo;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.dto.AssignableWorkerStepVo;
import codedriver.framework.process.dto.ProcessCommentTemplateVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.operationauth.ProcessTaskPermissionDeniedException;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface IProcessTaskCrossoverService extends ICrossoverService {

    ProcessTaskVo checkProcessTaskParamsIsLegal(Long processTaskId, Long processTaskStepId) throws Exception;

    ProcessTaskVo checkProcessTaskParamsIsLegal(Long processTaskId) throws Exception;

    void setProcessTaskFormInfo(ProcessTaskVo processTaskVo);

    ProcessTaskStepVo getStartProcessTaskStepByProcessTaskId(Long processTaskId);

    /**
     * 获取用户拥有此工单的哪些工单干系人身份
     *
     * @param processTaskId
     * @param authenticationInfoVo
     * @return
     */
    List<String> getProcessUserTypeList(Long processTaskId, AuthenticationInfoVo authenticationInfoVo);

    /**
     * 根据fileId 和 processTaskIdList 获取对应用户是否有该工单附件的下载权限
     *
     * @param fileId
     * @param processTaskIdList
     * @return true：有权限   false：没有权限
     */
    boolean getProcessFileHasDownloadAuthWithFileIdAndProcessTaskIdList(Long fileId, List<Long> processTaskIdList);

    /**
     * 某个用户的待办的工单中当前处理节点是打了某个标签的节点的工单列表
     *
     * @param jsonObj 参数结构见processtask/currentstepistagstepofmine/list接口
     * @return
     */
    List<Map<String, Object>> getProcessTaskListWhichIsProcessingByUserAndTag(JSONObject jsonObj);

    /**
     * 批量审批工单
     *
     * @param jsonObj 参数结构见processtask/step/batch/complete接口
     * @return
     */
    JSONObject batchCompleteProcessTaskStep(JSONObject jsonObj);

    List<AssignableWorkerStepVo> getAssignableWorkerStepList(Long processTaskId, String processStepUuid);

    List<ProcessTaskStepVo> getForwardNextStepListByProcessTaskStepId(Long processTaskStepId);

    List<ProcessTaskStepVo> getBackwardNextStepListByProcessTaskStepId(Long processTaskStepId);

    /**
     * 检查工单状态，如果processTaskStatus属于status其中一员，则返回对应的异常对象，否则返回null
     *
     * @param processTaskStatus 工单状态
     * @param statuss           状态列表
     * @return
     */
    ProcessTaskPermissionDeniedException checkProcessTaskStatus(String processTaskStatus, ProcessTaskStatus... statuss);

    /**
     * 检查步骤状态，如果stepStatus属于status其中一员，则返回对应的异常对象，否则返回null
     *
     * @param stepStatus 步骤状态
     * @param statuss    状态列表
     * @return
     */
    ProcessTaskPermissionDeniedException checkProcessTaskStepStatus(String stepStatus, ProcessTaskStatus... statuss);

    /**
     * 获取步骤回复模版
     *
     * @param processStepUuid      步骤uuid
     * @param authenticationInfoVo 用户授权
     * @return
     */
    ProcessCommentTemplateVo getProcessStepCommentTemplate(String processStepUuid, AuthenticationInfoVo authenticationInfoVo);
}

