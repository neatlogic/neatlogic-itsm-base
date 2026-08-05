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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;

/**
 * @author lvzk
 * @since 2021/9/1 17:33
 **/
public class ProcessTaskStepTaskUserFileVo extends BaseEditorVo {
    private static final long serialVersionUID = -7440360822572699974L;
    @EntityField(name = "nfpd.processtasksteptaskuserfilevo.entityfield.processtasksteptaskid.name", type = ApiParamType.LONG)
    private Long processTaskStepTaskId;
    @EntityField(name = "nfpd.processtasksteptaskuserfilevo.entityfield.processtasksteptaskuserid.name", type = ApiParamType.LONG)
    private Long processTaskStepTaskUserId;
    @EntityField(name = "nfpd.processtasksteptaskuserfilevo.entityfield.fileid.name", type = ApiParamType.LONG)
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
