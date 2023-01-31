/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.workcenter.dto;

import java.util.List;

public class WorkcenterConditionGroupVo {
    private String uuid;
    private List<WorkcenterConditionVo> conditionList;
    private List<String> channelUuidList;
    private List<WorkcenterConditionRelVo> conditionRelList;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<WorkcenterConditionVo> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<WorkcenterConditionVo> conditionList) {
        this.conditionList = conditionList;
    }

    public List<String> getChannelUuidList() {
        return channelUuidList;
    }

    public void setChannelUuidList(List<String> channelUuidList) {
        this.channelUuidList = channelUuidList;
    }

    public List<WorkcenterConditionRelVo> getConditionRelList() {
        return conditionRelList;
    }

    public void setConditionRelList(List<WorkcenterConditionRelVo> conditionRelList) {
        this.conditionRelList = conditionRelList;
    }
}
