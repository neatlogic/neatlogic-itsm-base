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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;
import neatlogic.framework.process.processtaskserialnumberpolicy.core.ProcessTaskSerialNumberPolicyHandlerFactory;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.UUID;

public class ChannelTypeVo extends BasePageVo implements Serializable, Cloneable {

    private static final long serialVersionUID = -3747925860575582286L;
    @EntityField(name = "nfpd.channeltypevo.entityfield.uuid.name", type = ApiParamType.STRING)
    private String uuid;
    @EntityField(name = "nfpd.channeltypevo.entityfield.name.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "nfpd.channeltypevo.entityfield.isactive.name", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "nfpd.channeltypevo.entityfield.icon.name", type = ApiParamType.STRING)
    private String icon;
    @EntityField(name = "nfpd.channeltypevo.entityfield.color.name", type = ApiParamType.STRING)
    private String color;
    @EntityField(name = "nfpd.channeltypevo.entityfield.description.name", type = ApiParamType.STRING)
    private String description;
    @EntityField(name = "nfpd.channeltypevo.entityfield.sort.name", type = ApiParamType.INTEGER)
    private Integer sort;
    @EntityField(name = "nfpd.channeltypevo.entityfield.prefix.name", type = ApiParamType.STRING)
    private String prefix;
    @EntityField(name = "nfpd.channeltypevo.entityfield.handler.name", type = ApiParamType.STRING)
    private String handler;
    @EntityField(name = "nfpd.channeltypevo.entityfield.handlername.name", type = ApiParamType.STRING)
    private String handlerName;

    public ChannelTypeVo() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHandlerName() {
        if (StringUtils.isBlank(handlerName) && StringUtils.isNotBlank(handler)) {
            IProcessTaskSerialNumberPolicyHandler policyHandler = ProcessTaskSerialNumberPolicyHandlerFactory.getHandler(handler);
            if (policyHandler != null) {
                handlerName = policyHandler.getName();
            }
        }
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    @Override

    public ChannelTypeVo clone() throws CloneNotSupportedException {
        return (ChannelTypeVo) super.clone();
    }

}
