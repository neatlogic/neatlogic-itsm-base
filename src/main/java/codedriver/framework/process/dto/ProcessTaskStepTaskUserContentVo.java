/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author lvzk
 * @since 2021/9/1 17:33
 **/
public class ProcessTaskStepTaskUserContentVo extends BaseEditorVo {
    private static final long serialVersionUID = -7440360822572699974L;
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "任务id", type = ApiParamType.LONG)
    private Long processtaskStepTaskId;
    @EntityField(name = "任务用户id", type = ApiParamType.LONG)
    private Long processtaskStepTaskUserId;
    @EntityField(name = "用户uuid", type = ApiParamType.STRING)
    private String userUuid;
    @EntityField(name = "结束时间", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "任务状态", type = ApiParamType.STRING)
    private String status;
    @JSONField(serialize=false)
    private String contentHash;
    @EntityField(name = "内容", type = ApiParamType.STRING)
    private String content;

    public ProcessTaskStepTaskUserContentVo() {
    }

    public ProcessTaskStepTaskUserContentVo(Long processtaskStepTaskId, String userUuid, String status) {
        this.processtaskStepTaskId = processtaskStepTaskId;
        this.userUuid = userUuid;
        this.status = status;
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcesstaskStepTaskId() {
        return processtaskStepTaskId;
    }

    public void setProcesstaskStepTaskId(Long processtaskStepTaskId) {
        this.processtaskStepTaskId = processtaskStepTaskId;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getProcesstaskStepTaskUserId() {
        return processtaskStepTaskUserId;
    }

    public void setProcesstaskStepTaskUserId(Long processtaskStepTaskUserId) {
        this.processtaskStepTaskUserId = processtaskStepTaskUserId;
    }
}
