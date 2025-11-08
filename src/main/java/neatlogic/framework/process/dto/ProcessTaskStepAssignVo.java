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

import java.util.List;

public class ProcessTaskStepAssignVo {
    private Integer autoStart;
    private Integer isOnlyOnceExecute;
    private String message;
    private Boolean isAssignException;
    private String defaultWorkerName;
    private String stepStatus;
    private ProcessTaskStepUserVo oldStepUser;
    private ProcessTaskStepUserVo stepUser;
    private List<ProcessTaskStepWorkerVo> stepWorkerList;
    private List<ProcessTaskStepWorkerVo> finalStepWorkerList;

    public Integer getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Integer autoStart) {
        this.autoStart = autoStart;
    }

    public Integer getIsOnlyOnceExecute() {
        return isOnlyOnceExecute;
    }

    public void setIsOnlyOnceExecute(Integer isOnlyOnceExecute) {
        this.isOnlyOnceExecute = isOnlyOnceExecute;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsAssignException() {
        return isAssignException;
    }

    public void setIsAssignException(Boolean assignException) {
        isAssignException = assignException;
    }

    public String getDefaultWorkerName() {
        return defaultWorkerName;
    }

    public void setDefaultWorkerName(String defaultWorkerName) {
        this.defaultWorkerName = defaultWorkerName;
    }

    public String getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(String stepStatus) {
        this.stepStatus = stepStatus;
    }

    public ProcessTaskStepUserVo getOldStepUser() {
        return oldStepUser;
    }

    public void setOldStepUser(ProcessTaskStepUserVo oldStepUser) {
        this.oldStepUser = oldStepUser;
    }

    public ProcessTaskStepUserVo getStepUser() {
        return stepUser;
    }

    public void setStepUser(ProcessTaskStepUserVo stepUser) {
        this.stepUser = stepUser;
    }

    public List<ProcessTaskStepWorkerVo> getStepWorkerList() {
        return stepWorkerList;
    }

    public void setStepWorkerList(List<ProcessTaskStepWorkerVo> stepWorkerList) {
        this.stepWorkerList = stepWorkerList;
    }

    public List<ProcessTaskStepWorkerVo> getFinalStepWorkerList() {
        return finalStepWorkerList;
    }

    public void setFinalStepWorkerList(List<ProcessTaskStepWorkerVo> finalStepWorkerList) {
        this.finalStepWorkerList = finalStepWorkerList;
    }
}
