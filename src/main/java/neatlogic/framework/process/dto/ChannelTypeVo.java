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
    @EntityField(name = "服务类型uuid", type = ApiParamType.STRING)
    private String uuid;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "状态", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "图标", type = ApiParamType.STRING)
    private String icon;
    @EntityField(name = "颜色", type = ApiParamType.STRING)
    private String color;
    @EntityField(name = "描述", type = ApiParamType.STRING)
    private String description;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private Integer sort;
    @EntityField(name = "工单号前缀", type = ApiParamType.STRING)
    private String prefix;
    @EntityField(name = "工单号策略", type = ApiParamType.STRING)
    private String handler;
    @EntityField(name = "工单号策略名", type = ApiParamType.STRING)
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
