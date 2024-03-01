/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.dto.AuthenticationInfoVo;
import neatlogic.framework.form.dto.FormAttributeVo;
import neatlogic.framework.process.constvalue.ProcessTaskStatus;
import neatlogic.framework.process.constvalue.ProcessTaskStepStatus;
import neatlogic.framework.process.dto.*;
import neatlogic.framework.process.exception.operationauth.ProcessTaskPermissionDeniedException;
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

    /**
     * 获取需指派处理人的步骤列表
     * @param currentProcessTaskStepVo 工单步骤信息
     * @return
     */
    Map<Long, List<AssignableWorkerStepVo>> getAssignableWorkerStepMap(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 设置下一步骤列表
     *
     * @param processTaskStepVo 步骤信息
     */
    void setNextStepList(ProcessTaskStepVo processTaskStepVo);

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
     * @param status    状态列表
     * @return
     */
    ProcessTaskPermissionDeniedException checkProcessTaskStepStatus(String stepStatus, ProcessTaskStepStatus... status);

    /**
     * 获取步骤回复模版
     *
     * @param processStepUuid      步骤uuid
     * @param authenticationInfoVo 用户授权
     * @return
     */
    ProcessCommentTemplateVo getProcessStepCommentTemplate(String processStepUuid, AuthenticationInfoVo authenticationInfoVo);

    /**
     * 根据工单id获取工单绑定的表单信息
     * @param processTaskId 工单ID
     * @return 表单属性列表
     */
    List<FormAttributeVo> getFormAttributeListByProcessTaskId(Long processTaskId);

    /**
     * 根据工单id获取表单属性数据列表
     * @param processTaskId 工单id
     * @return
     */
    List<ProcessTaskFormAttributeDataVo> getProcessTaskFormAttributeDataListByProcessTaskId(Long processTaskId);

    /**
     * 暂存工单草稿
     *
     * @param jsonObj
     * @return
     */
    JSONObject saveProcessTaskDraft(JSONObject jsonObj) throws Exception;

    /**
     * 提交上报工单
     *
     * @param jsonObj
     */
    void startProcessProcessTask(JSONObject jsonObj) throws Exception;
}

