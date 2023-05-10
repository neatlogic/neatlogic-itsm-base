/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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
