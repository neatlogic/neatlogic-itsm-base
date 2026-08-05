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
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.id.name", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.processtasksteptaskid.name", type = ApiParamType.LONG)
    private Long processTaskStepTaskId;
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.processtasksteptaskuserid.name", type = ApiParamType.LONG)
    private Long processTaskStepTaskUserId;
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.useruuid.name", type = ApiParamType.STRING)
    private String userUuid;
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.uservo.name", type = ApiParamType.JSONOBJECT)
    private UserVo userVo;
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.endtime.name", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.status.name", type = ApiParamType.STRING)
    private String status;
    @JSONField(serialize=false)
    private String contentHash;
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.content.name", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "nfpd.processtasksteptaskusercontentvo.entityfield.button.name", type = ApiParamType.STRING)
    private String button;

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

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
}
