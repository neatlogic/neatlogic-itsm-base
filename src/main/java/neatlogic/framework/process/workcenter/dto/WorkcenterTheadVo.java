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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.process.column.core.IProcessTaskColumn;
import neatlogic.framework.process.column.core.ProcessTaskColumnFactory;
import neatlogic.framework.process.constvalue.ProcessFieldType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.$;

import java.io.Serializable;

public class WorkcenterTheadVo implements Serializable {

    @JSONField(serialize = false)
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.workcenteruuid.name", type = ApiParamType.STRING)
    private String workcenterUuid;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.name.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.displayname.name", type = ApiParamType.STRING)
    private String displayName;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.sort.name", type = ApiParamType.INTEGER)
    private Integer sort = 100;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.width.name", type = ApiParamType.INTEGER)
    private Integer width = 1;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.isshow.name", type = ApiParamType.INTEGER)
    private Integer isShow = 1;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.disabled.name", type = ApiParamType.INTEGER)
    private Integer disabled;
    @JSONField(serialize = false)
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.useruuid.name", type = ApiParamType.STRING)
    private String userUuid;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.type.name", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.classname.name", type = ApiParamType.STRING)
    private String className;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.isexport.name", type = ApiParamType.INTEGER)
    private Integer isExport;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.issortable.name", type = ApiParamType.INTEGER)
    private int isSortable;
    @EntityField(name = "nfpwd.workcentertheadvo.entityfield.config.name", type = ApiParamType.JSONOBJECT)
    private JSONObject config;

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
        this.displayName = $.t(column.getDisplayName());
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

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }
}
