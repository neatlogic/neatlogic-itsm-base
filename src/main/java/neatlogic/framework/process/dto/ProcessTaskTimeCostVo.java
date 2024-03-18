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
 * @since 2022/2/24 14:25
 **/
public class ProcessTaskTimeCostVo {
    private Long processTaskId;
    private Long timeCost;
    private Long realTimeCost;

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Long timeCost) {
        this.timeCost = timeCost;
    }

    public Long getRealTimeCost() {
        return realTimeCost;
    }

    public void setRealTimeCost(Long realTimeCost) {
        this.realTimeCost = realTimeCost;
    }
}
