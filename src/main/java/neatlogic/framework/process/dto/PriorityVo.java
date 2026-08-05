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

import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.UUID;

public class PriorityVo extends BasePageVo implements Serializable {

    private static final long serialVersionUID = -4831712599845296278L;
    @EntityField(name = "nfpd.priorityvo.entityfield.uuid.name", type = ApiParamType.STRING)
    private String uuid;
    @EntityField(name = "nfpd.priorityvo.entityfield.name.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "nfpd.priorityvo.entityfield.isactive.name", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "nfpd.priorityvo.entityfield.icon.name", type = ApiParamType.STRING)
    private String icon;
    @EntityField(name = "nfpd.priorityvo.entityfield.color.name", type = ApiParamType.STRING)
    private String color;
    @EntityField(name = "nfpd.priorityvo.entityfield.desc.name", type = ApiParamType.STRING)
    private String desc;
    @EntityField(name = "nfpd.priorityvo.entityfield.sort.name", type = ApiParamType.INTEGER)
    private Integer sort;
    @JSONField(serialize = false)
    private String channelUuid;

    public synchronized String getUuid() {
        if (StringUtils.isBlank(uuid)) {
            uuid = UUID.randomUUID().toString().replace("-", "");
        }
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getChannelUuid() {
        return channelUuid;
    }

    public void setChannelUuid(String channelUuid) {
        this.channelUuid = channelUuid;
    }

}
