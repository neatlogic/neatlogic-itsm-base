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

package neatlogic.framework.process.dto.processconfig;

import neatlogic.framework.notify.dto.InvokeNotifyPolicyConfigVo;

/**
 * @author linbq
 * @since 2021/5/19 19:23
 **/
public class SlaNotifyPolicyVo {
    private String executeType = "once";
    private String unit = "minute";
    private String expression = "";
    private String intervalUnit;
    private Integer time = 0;
    private String uuid = "";
    private Integer intervalTime;
    private InvokeNotifyPolicyConfigVo notifyPolicyConfig;

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getIntervalUnit() {
        return intervalUnit;
    }

    public void setIntervalUnit(String intervalUnit) {
        this.intervalUnit = intervalUnit;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public InvokeNotifyPolicyConfigVo getNotifyPolicyConfig() {
        return notifyPolicyConfig;
    }

    public void setNotifyPolicyConfig(InvokeNotifyPolicyConfigVo notifyPolicyConfig) {
        this.notifyPolicyConfig = notifyPolicyConfig;
    }
}
