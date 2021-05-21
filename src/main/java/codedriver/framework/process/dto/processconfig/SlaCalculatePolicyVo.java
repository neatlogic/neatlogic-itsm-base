/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto.processconfig;

import codedriver.framework.dto.condition.ConditionGroupRelVo;
import codedriver.framework.dto.condition.ConditionGroupVo;

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
