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

import java.util.Date;

public class ProcessTaskStepCostVo {
    private Long id;
    private Long processTaskId;
    private Long processTaskStepId;

    private String startOperate;
    private String startStatus;
    private Date startTime;
    private String startUserUuid;

    private String endOperate;
    private String endStatus;
    private Date endTime;
    private String endUserUuid;

    private Long timeCost;
    private Long realTimeCost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStartOperate() {
        return startOperate;
    }

    public void setStartOperate(String startOperate) {
        this.startOperate = startOperate;
    }

    public String getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(String startStatus) {
        this.startStatus = startStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getStartUserUuid() {
        return startUserUuid;
    }

    public void setStartUserUuid(String startUserUuid) {
        this.startUserUuid = startUserUuid;
    }

    public String getEndOperate() {
        return endOperate;
    }

    public void setEndOperate(String endOperate) {
        this.endOperate = endOperate;
    }

    public String getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEndUserUuid() {
        return endUserUuid;
    }

    public void setEndUserUuid(String endUserUuid) {
        this.endUserUuid = endUserUuid;
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
