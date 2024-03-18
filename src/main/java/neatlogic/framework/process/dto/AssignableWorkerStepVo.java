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
