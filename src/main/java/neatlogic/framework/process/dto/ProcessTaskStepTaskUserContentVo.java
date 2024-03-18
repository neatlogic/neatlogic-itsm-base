/*Copyright (C) 2023  深圳极向量科技有限公司 All Rights Reserved.

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
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
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
    private Long processTaskStepTaskId;
    @EntityField(name = "任务用户id", type = ApiParamType.LONG)
    private Long processTaskStepTaskUserId;
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

    public ProcessTaskStepTaskUserContentVo() {
    }

    public ProcessTaskStepTaskUserContentVo(Long processtaskStepTaskId, String userUuid, String status) {
        this.processTaskStepTaskId = processtaskStepTaskId;
        this.userUuid = userUuid;
        this.status = status;
    }

    public ProcessTaskStepTaskUserContentVo(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo) {
        this.processTaskStepTaskId = processTaskStepTaskUserVo.getProcessTaskStepTaskId();
        this.processTaskStepTaskUserId = processTaskStepTaskUserVo.getId();
        this.contentHash = processTaskStepTaskUserVo.getContentHash();
        this.userUuid = processTaskStepTaskUserVo.getUserUuid();
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

    public Long getProcessTaskStepTaskUserId() {
        return processTaskStepTaskUserId;
    }

    public void setProcessTaskStepTaskUserId(Long processTaskStepTaskUserId) {
        this.processTaskStepTaskUserId = processTaskStepTaskUserId;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }
}
