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
 * @since 2022/2/22 15:07
 **/
public class ProcessTaskStepSlaTimeVo {
    private Long processTaskId;
    private Long processTaskStepId;
    private String type;
    private Long slaId;
    private Long timeSum;
    private Long timeCost;
    private Long realTimeCost;
    private Integer isTimeout;

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getProcessTaskStepId() {
        return processTaskStepId;
    }

    public void setProcessTaskStepId(Long processTaskStepId) {
        this.processTaskStepId = processTaskStepId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSlaId() {
        return slaId;
    }

    public void setSlaId(Long slaId) {
        this.slaId = slaId;
    }

    public Long getTimeSum() {
        return timeSum;
    }

    public void setTimeSum(Long timeSum) {
        this.timeSum = timeSum;
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

    public Integer getIsTimeout() {
        if (isTimeout == null && timeSum != null && timeCost != null) {
            isTimeout = timeSum > timeCost ? 0 : 1;
        }
        return isTimeout;
    }

    public void setIsTimeout(Integer isTimeout) {
        this.isTimeout = isTimeout;
    }
}
