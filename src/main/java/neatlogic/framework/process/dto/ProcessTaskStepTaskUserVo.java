/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.dto;

import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/1 17:33
 **/
public class ProcessTaskStepTaskUserVo implements Serializable {
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.id.name", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.processtasksteptaskid.name", type = ApiParamType.LONG)
    private Long processTaskStepTaskId;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.processtasksteptaskusercontentid.name", type = ApiParamType.LONG)
    private Long processTaskStepTaskUserContentId;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.useruuid.name", type = ApiParamType.STRING)
    private String userUuid;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.uservo.name", type = ApiParamType.JSONOBJECT)
    private UserVo userVo;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.endtime.name", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.status.name", type = ApiParamType.STRING)
    private String status;
    @JSONField(serialize=false)
    private String contentHash;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.content.name", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.steptaskusercontentvolist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepTaskUserContentVo> stepTaskUserContentVoList = new ArrayList<>();
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.isdelete.name", type = ApiParamType.INTEGER)
    private Integer isDelete;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.isreplyable.name", type = ApiParamType.INTEGER)
    private Integer isReplyable;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.originaluseruuid.name", type = ApiParamType.STRING)
    private String originalUserUuid;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.originaluservo.name", type = ApiParamType.JSONOBJECT)
    private UserVo originalUserVo;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.fileidlist.name", type = ApiParamType.JSONARRAY)
    private List<Long> fileIdList;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.filelist.name", type = ApiParamType.JSONARRAY)
    private List<FileVo> fileList;
    @EntityField(name = "nfpd.processtasksteptaskuservo.entityfield.button.name", type = ApiParamType.STRING)
    private String button;

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

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
}
