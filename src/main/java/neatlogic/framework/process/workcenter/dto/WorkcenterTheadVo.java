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

import neatlogic.framework.process.column.core.ProcessTaskColumnFactory;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.process.column.core.IProcessTaskColumn;
import neatlogic.framework.process.constvalue.ProcessFieldType;
import neatlogic.framework.restful.annotation.EntityField;

public class WorkcenterTheadVo {

    @JSONField(serialize = false)
    @EntityField(name = "工单中心分类唯一标识段", type = ApiParamType.STRING)
    private String workcenterUuid;
    @EntityField(name = "字段名（表单属性则存属性uuid）", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "字段中文名", type = ApiParamType.STRING)
    private String displayName;
    @EntityField(name = "字段排序", type = ApiParamType.INTEGER)
    private Integer sort = 100;
    @EntityField(name = "字段宽度", type = ApiParamType.INTEGER)
    private Integer width = 1;
    @EntityField(name = "字段是否展示", type = ApiParamType.INTEGER)
    private Integer isShow = 1;
    @EntityField(name = "是否可以控制该字段是否显示", type = ApiParamType.INTEGER)
    private Integer disabled;
    @JSONField(serialize = false)
    @EntityField(name = "所属用户", type = ApiParamType.STRING)
    private String userUuid;
    @EntityField(name = "字段类型", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "字段样式", type = ApiParamType.STRING)
    private String className;
    @EntityField(name = "字段是否可导出", type = ApiParamType.INTEGER)
    private Integer isExport;
    @EntityField(name = "是否支持排序", type = ApiParamType.INTEGER)
    private int isSortable;

    public WorkcenterTheadVo(String name) {
        this.name = name;
    }

    public WorkcenterTheadVo(JSONObject obj) {
        this.name = obj.getString("name");
        this.sort = obj.getIntValue("sort");
        this.width = obj.getIntValue("width");
        this.isShow = obj.getIntValue("isShow");
        this.userUuid = UserContext.get().getUserUuid();
        this.type = obj.getString("type");
        this.className = obj.getString("className");
        this.isSortable = obj.getIntValue("isSortable");
    }

    public WorkcenterTheadVo(IProcessTaskColumn column) {
        this.name = column.getName();
        this.userUuid = UserContext.get().getUserUuid();
        this.displayName = column.getDisplayName();
        this.type = ProcessFieldType.COMMON.getValue();
        this.className = column.getClassName();
        this.sort = column.getSort();
        this.disabled = column.getDisabled() ? 1 : 0;
        this.isExport = column.getIsExport() ? 1 : 0;
        this.isShow = column.getIsShow() ? 1 : 0;
        this.isSortable = column.getIsSort() ? 1 : 0;
    }

    public WorkcenterTheadVo(String _workcenterUuid, String _userUuid) {
        this.workcenterUuid = _workcenterUuid;
        this.userUuid = _userUuid;
    }

    public WorkcenterTheadVo() {

    }

    public String getWorkcenterUuid() {
        return workcenterUuid;
    }

    public void setWorkcenterUuid(String workcenterUuid) {
        this.workcenterUuid = workcenterUuid;
    }

    public int getIsSortable() {
        IProcessTaskColumn column = ProcessTaskColumnFactory.getHandler(this.name);
        if (column != null) {
            isSortable = column.getIsSort() ? 1 : 0;
        }
        return isSortable;
    }

    public void setIsSortable(int isSortable) {
        this.isSortable = isSortable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Integer getIsExport() {
        return isExport;
    }

    public void setIsExport(Integer isExport) {
        this.isExport = isExport;
    }
}
