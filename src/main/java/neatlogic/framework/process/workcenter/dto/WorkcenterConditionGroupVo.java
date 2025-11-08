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
