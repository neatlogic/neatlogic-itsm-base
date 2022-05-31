/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.dto.UserVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author lvzk
 * @since 2021/9/1 17:33
 **/
public class ProcessTaskStepTaskUserFileVo extends BaseEditorVo {
    private static final long serialVersionUID = -7440360822572699974L;
    @EntityField(name = "任务id", type = ApiParamType.LONG)
    private Long processTaskStepTaskId;
    @EntityField(name = "任务用户id", type = ApiParamType.LONG)
    private Long processTaskStepTaskUserId;
    @EntityField(name = "附件id", type = ApiParamType.LONG)
    private Long fileId;

    public ProcessTaskStepTaskUserFileVo() {
    }

    public ProcessTaskStepTaskUserFileVo(Long processTaskStepTaskId, Long processTaskStepTaskUserId, Long fileId) {
        this.processTaskStepTaskId = processTaskStepTaskId;
        this.processTaskStepTaskUserId = processTaskStepTaskUserId;
        this.fileId = fileId;
    }

    public Long getProcessTaskStepTaskId() {
        return processTaskStepTaskId;
    }

    public void setProcessTaskStepTaskId(Long processTaskStepTaskId) {
        this.processTaskStepTaskId = processTaskStepTaskId;
    }

    public Long getProcessTaskStepTaskUserId() {
        return processTaskStepTaskUserId;
    }

    public void setProcessTaskStepTaskUserId(Long processTaskStepTaskUserId) {
        this.processTaskStepTaskUserId = processTaskStepTaskUserId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
