/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
import java.util.Set;

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

    List<ProcessTaskStepVo> getForwardNextStepListByProcessTaskStepId(ProcessTaskStepVo processTaskStepVo);

    List<ProcessTaskStepVo> getBackwardNextStepListByProcessTaskStepId(ProcessTaskStepVo processTaskStepVo);

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
     * 根据工单id获取工单绑定的表单信息
     * @param processTaskId 工单ID
     * @param tag 标签
     * @return
     */
    List<FormAttributeVo> getFormAttributeListByProcessTaskIdAngTag(Long processTaskId, String tag);

    /**
     * 根据工单id获取工单绑定的表单信息
     * @param processTaskId 工单ID
     * @param tag 标签
     * @return
     */
    List<FormAttributeVo> getFormAttributeListByProcessTaskIdAngTagNew(Long processTaskId, String tag);

    /**
     * 根据工单id获取表单属性数据列表
     * @param processTaskId 工单id
     * @return
     */
    List<ProcessTaskFormAttributeDataVo> getProcessTaskFormAttributeDataListByProcessTaskId(Long processTaskId);

    /**
     * 根据工单id获取表单属性数据列表
     * @param processTaskId 工单id
     * @param tag 标签
     * @return
     */
    List<ProcessTaskFormAttributeDataVo> getProcessTaskFormAttributeDataListByProcessTaskIdAndTag(Long processTaskId, String tag);

    /**
     * 根据工单id获取表单属性数据列表
     * @param processTaskId 工单id
     * @param tag 标签
     * @return
     */
    List<ProcessTaskFormAttributeDataVo> getProcessTaskFormAttributeDataListByProcessTaskIdAndTagNew(Long processTaskId, String tag);

    /**
     * 完成工单步骤
     *
     * @param paramObj 参数结构见processtask/complete接口
     * @return
     * @throws Exception
     */
    void completeProcessTaskStep(JSONObject paramObj) throws Exception;

    /**
     * 开始工单步骤
     *
     * @param paramObj 参数结构见processtask/start接口
     * @return
     * @throws Exception
     */
    void startProcessTaskStep(JSONObject paramObj) throws Exception;

    /**
     * 根据工单id和表单属性uuid获取表单属性数据
     * @param processTaskId 工单id
     * @param attributeUuid 表单属性uuid
     * @return
     */
    ProcessTaskFormAttributeDataVo getProcessTaskFormAttributeDataByProcessTaskIdAndAttributeUuid(Long processTaskId, String attributeUuid);

    /**
     * @param processTaskVo
     * @return Set<ProcessTaskStepVo>
     * @Time:2020年11月26日
     * @Description: 获取当前用户有转交权限的步骤列表
     */
    public Set<ProcessTaskStepVo> getTransferableStepListByProcessTask(ProcessTaskVo processTaskVo, String userUuid);
}

