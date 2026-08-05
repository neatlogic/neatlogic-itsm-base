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

package neatlogic.framework.process.dto;

import com.alibaba.fastjson.JSONArray;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

/**
 * @author linbq
 * @since 2021/8/20 11:52
 **/
public class AssignableWorkerStepVo {
    @EntityField(name = "nfpd.assignableworkerstepvo.entityfield.id.name", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "nfpd.assignableworkerstepvo.entityfield.name.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "nfpd.assignableworkerstepvo.entityfield.processstepuuid.name", type = ApiParamType.STRING)
    private String processStepUuid;
    @EntityField(name = "nfpd.assignableworkerstepvo.entityfield.isrequired.name", type = ApiParamType.INTEGER)
    private Integer isRequired;
    @EntityField(name = "nfpd.assignableworkerstepvo.entityfield.rangelist.name", type = ApiParamType.JSONARRAY)
    private JSONArray rangeList;
    @EntityField(name = "nfpd.assignableworkerstepvo.entityfield.grouplist.name", type = ApiParamType.JSONARRAY)
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
