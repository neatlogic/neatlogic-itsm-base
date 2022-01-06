/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.crossover;

import codedriver.framework.crossover.ICrossoverService;
import codedriver.framework.dto.AuthenticationInfoVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;

import java.util.List;

public interface IProcessTaskCrossoverService extends ICrossoverService {

    ProcessTaskVo checkProcessTaskParamsIsLegal(Long processTaskId, Long processTaskStepId) throws Exception;

    ProcessTaskVo checkProcessTaskParamsIsLegal(Long processTaskId) throws Exception;

    void setProcessTaskFormInfo(ProcessTaskVo processTaskVo);

    ProcessTaskStepVo getStartProcessTaskStepByProcessTaskId(Long processTaskId);

    ProcessTaskVo getProcessTaskDetailById(Long processTaskId);

    /**
     * @param processTaskStepId 步骤id
     * @return ProcessTaskStepVo
     * @Author: linbq
     * @Time:2020年8月21日
     * @Description: 获取当前步骤信息
     */
    ProcessTaskStepVo getCurrentProcessTaskStepById(Long processTaskStepId);

    /**
     * 获取用户拥有此工单的哪些工单干系人身份
     * @param processTaskId
     * @param authenticationInfoVo
     * @return
     */
    List<String> getProcessUserTypeList(Long processTaskId, AuthenticationInfoVo authenticationInfoVo);

    /**
     * 根据fileId 和 processTaskStepVo 获取对应用户是否有该工单附件的下载权限
     * @param fileId
     * @param processTaskStepVo
     * @return true：有权限   false：没有权限
     */
    boolean getProcessFileHasDownloadAuthWithFileIdAndProcessTaskStepVo(Long fileId, ProcessTaskStepVo processTaskStepVo);
}
