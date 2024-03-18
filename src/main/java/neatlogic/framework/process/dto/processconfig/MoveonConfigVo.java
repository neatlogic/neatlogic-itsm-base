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
 * @since 2021/5/21 15:29
 **/
public class MoveonConfigVo {
    private List<String> targetStepList = new ArrayList<>();
    private String type = "";
    private List<ConditionGroupVo> conditionGroupList = new ArrayList<>();
    private List<ConditionGroupRelVo> conditionGroupRelList = new ArrayList<>();

    public List<String> getTargetStepList() {
        return targetStepList;
    }

    public void setTargetStepList(List<String> targetStepList) {
        this.targetStepList = targetStepList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ConditionGroupVo> getConditionGroupList() {
        return conditionGroupList;
    }

    public void setConditionGroupList(List<ConditionGroupVo> conditionGroupList) {
        this.conditionGroupList = conditionGroupList;
    }

    public List<ConditionGroupRelVo> getConditionGroupRelList() {
        return conditionGroupRelList;
    }

    public void setConditionGroupRelList(List<ConditionGroupRelVo> conditionGroupRelList) {
        this.conditionGroupRelList = conditionGroupRelList;
    }
}
