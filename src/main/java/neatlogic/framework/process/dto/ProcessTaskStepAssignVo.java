/*
 * Copyright (C) 2025  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
