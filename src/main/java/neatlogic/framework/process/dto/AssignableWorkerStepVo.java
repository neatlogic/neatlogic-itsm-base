/*
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * @author linbq
 * @since 2021/8/20 11:52
 **/
public class AssignableWorkerStepVo {
    @EntityField(name = "工单步骤id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "步骤名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "步骤uuid", type = ApiParamType.STRING)
    private String processStepUuid;
    @EntityField(name = "回复是否必填", type = ApiParamType.INTEGER)
    private Integer isRequired;
    @EntityField(name = "范围列表", type = ApiParamType.JSONARRAY)
    private JSONArray rangeList;
    @EntityField(name = "目标列表", type = ApiParamType.JSONARRAY)
    private JSONArray groupList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessStepUuid() {
        return processStepUuid;
    }

    public void setProcessStepUuid(String processStepUuid) {
        this.processStepUuid = processStepUuid;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public JSONArray getRangeList() {
        return rangeList;
    }

    public void setRangeList(JSONArray rangeList) {
        this.rangeList = rangeList;
    }

    public JSONArray getGroupList() {
        return groupList;
    }

    public void setGroupList(JSONArray groupList) {
        this.groupList = groupList;
    }
}
