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

/**
 * @author linbq
 * @since 2021/9/13 16:55
 **/
public class ProcessTaskRepeatVo {
    private Long processTaskId;
    private Long repeatGroupId;

    public ProcessTaskRepeatVo() {
    }

    public ProcessTaskRepeatVo(Long processTaskId, Long repeatGroupId) {
        this.processTaskId = processTaskId;
        this.repeatGroupId = repeatGroupId;
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getRepeatGroupId() {
        return repeatGroupId;
    }

    public void setRepeatGroupId(Long repeatGroupId) {
        this.repeatGroupId = repeatGroupId;
    }
}
