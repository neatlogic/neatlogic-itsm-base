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
