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

package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
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
    @EntityField(name = "附件id列表", type = ApiParamType.JSONARRAY)
    private List<Long> fileIdList;
    @EntityField(name = "附件信息列表", type = ApiParamType.JSONARRAY)
    private List<FileVo> fileList;
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

    public List<Long> getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(List<Long> fileIdList) {
        this.fileIdList = fileIdList;
    }

    public List<FileVo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileVo> fileList) {
        this.fileList = fileList;
    }
}
