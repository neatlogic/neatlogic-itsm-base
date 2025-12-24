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

package neatlogic.framework.process.stephandler.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.process.dto.ProcessStepRelVo;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.worktime.dto.WorktimeRangeVo;
import neatlogic.framework.worktime.dto.WorktimeVo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ProcessMessageContext {

    private JSONObject config;

    private String stepName = StringUtils.EMPTY;

    private OperationTypeEnum operationType;

    private List<String> effectiveStepUuidList;

    private List<ProcessStepRelVo> connectionList;

    private WorktimeVo worktime;

    private WorktimeRangeVo lastWorktimeRange;

//    public ProcessMessageContext(JSONObject config) {
//        this.config = config;
//    }

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public List<String> getEffectiveStepUuidList() {
        return effectiveStepUuidList;
    }

    public void setEffectiveStepUuidList(List<String> effectiveStepUuidList) {
        this.effectiveStepUuidList = effectiveStepUuidList;
    }

    public List<ProcessStepRelVo> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<ProcessStepRelVo> connectionList) {
        this.connectionList = connectionList;
    }

    public OperationTypeEnum getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypeEnum operationType) {
        this.operationType = operationType;
    }

    public WorktimeVo getWorktime() {
        return worktime;
    }

    public void setWorktime(WorktimeVo worktime) {
        this.worktime = worktime;
    }

    public WorktimeRangeVo getLastWorktimeRange() {
        return lastWorktimeRange;
    }

    public void setLastWorktimeRange(WorktimeRangeVo lastWorktimeRange) {
        this.lastWorktimeRange = lastWorktimeRange;
    }
}
