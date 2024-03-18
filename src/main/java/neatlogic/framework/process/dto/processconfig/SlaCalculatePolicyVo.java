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

import neatlogic.framework.dto.condition.ConditionGroupRelVo;
import neatlogic.framework.dto.condition.ConditionGroupVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/5/19 18:31
 **/
public class SlaCalculatePolicyVo {
    private Integer enablePriority = 0;
    private Boolean isshow = false;
    private List<ConditionGroupVo> conditionGroupList = new ArrayList<>();
    private Integer time = 0;
    private List<SlaCalculatePolicyPriorityVo> priorityList = new ArrayList<>();
    private String uuid = "";
    private List<ConditionGroupRelVo> conditionGroupRelList = new ArrayList<>();
    private String unit;

    public Integer getEnablePriority() {
        return enablePriority;
    }

    public void setEnablePriority(Integer enablePriority) {
        this.enablePriority = enablePriority;
    }

    public Boolean getIsshow() {
        return isshow;
    }

    public void setIsshow(Boolean isshow) {
        this.isshow = isshow;
    }

    public List<ConditionGroupVo> getConditionGroupList() {
        return conditionGroupList;
    }

    public void setConditionGroupList(List<ConditionGroupVo> conditionGroupList) {
        this.conditionGroupList = conditionGroupList;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<SlaCalculatePolicyPriorityVo> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(List<SlaCalculatePolicyPriorityVo> priorityList) {
        this.priorityList = priorityList;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ConditionGroupRelVo> getConditionGroupRelList() {
        return conditionGroupRelList;
    }

    public void setConditionGroupRelList(List<ConditionGroupRelVo> conditionGroupRelList) {
        this.conditionGroupRelList = conditionGroupRelList;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
