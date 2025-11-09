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
