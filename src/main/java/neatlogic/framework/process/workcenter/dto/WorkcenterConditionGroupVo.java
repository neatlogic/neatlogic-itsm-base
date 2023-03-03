/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
