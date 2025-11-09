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
