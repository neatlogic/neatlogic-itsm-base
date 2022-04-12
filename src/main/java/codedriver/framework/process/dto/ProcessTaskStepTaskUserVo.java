/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.dto.UserVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/1 17:33
 **/
public class ProcessTaskStepTaskUserVo implements Serializable {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "任务id", type = ApiParamType.LONG)
    private Long processTaskStepTaskId;
    @EntityField(name = "任务id", type = ApiParamType.LONG)
    private Long processTaskStepTaskUserContentId;
    @EntityField(name = "用户uuid", type = ApiParamType.STRING)
    private String userUuid;
    @EntityField(name = "用户", type = ApiParamType.JSONOBJECT)
    private UserVo userVo;
    @EntityField(name = "结束时间", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "任务状态", type = ApiParamType.STRING)
    private String status;
    @JSONField(serialize=false)
    private String contentHash;
    @EntityField(name = "内容", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "内容VoList", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepTaskUserContentVo> stepTaskUserContentVoList = new ArrayList<>();
    @EntityField(name = "是否删除", type = ApiParamType.INTEGER)
    private Integer isDelete;
    @EntityField(name = "是否可以回复", type = ApiParamType.INTEGER)
    private Integer isReplyable;
    @EntityField(name = "原始处理人uuid", type = ApiParamType.STRING)
    private String originalUserUuid;
    @EntityField(name = "用户", type = ApiParamType.JSONOBJECT)
    private UserVo originalUserVo;

    public ProcessTaskStepTaskUserVo() {
    }

    public ProcessTaskStepTaskUserVo(Long processtaskStepTaskId, String userUuid, String status) {
        this.processTaskStepTaskId = processtaskStepTaskId;
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

    public Long getProcessTaskStepTaskId() {
        return processTaskStepTaskId;
    }

    public void setProcessTaskStepTaskId(Long processTaskStepTaskId) {
        this.processTaskStepTaskId = processTaskStepTaskId;
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

    public List<ProcessTaskStepTaskUserContentVo> getStepTaskUserContentVoList() {
        return stepTaskUserContentVoList;
    }

    public void setStepTaskUserContentVoList(List<ProcessTaskStepTaskUserContentVo> stepTaskUserContentVoList) {
        this.stepTaskUserContentVoList = stepTaskUserContentVoList;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public Long getProcessTaskStepTaskUserContentId() {
        return processTaskStepTaskUserContentId;
    }

    public void setProcessTaskStepTaskUserContentId(Long processTaskStepTaskUserContentId) {
        this.processTaskStepTaskUserContentId = processTaskStepTaskUserContentId;
    }

    public Integer getIsReplyable() {
        return isReplyable;
    }

    public void setIsReplyable(Integer isReplyable) {
        this.isReplyable = isReplyable;
    }

    public String getOriginalUserUuid() {
        return originalUserUuid;
    }

    public void setOriginalUserUuid(String originalUserUuid) {
        this.originalUserUuid = originalUserUuid;
    }

    public UserVo getOriginalUserVo() {
        return originalUserVo;
    }

    public void setOriginalUserVo(UserVo originalUserVo) {
        this.originalUserVo = originalUserVo;
    }
}
