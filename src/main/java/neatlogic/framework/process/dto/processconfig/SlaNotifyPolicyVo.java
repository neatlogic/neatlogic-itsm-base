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
