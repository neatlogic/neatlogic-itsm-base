/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

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
