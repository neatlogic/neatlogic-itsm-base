/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;
import codedriver.framework.process.processtaskserialnumberpolicy.core.ProcessTaskSerialNumberPolicyHandlerFactory;
import codedriver.framework.restful.annotation.EntityField;
import com.alibaba.fastjson.annotation.JSONField;
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

    @JSONField(serialize = false)
    private String keyword;

    public ChannelTypeVo() {
    }

    //	public ChannelTypeVo(ChannelTypeVo channelTypeVo) {
//	    if(channelTypeVo != null) {
//	        this.uuid = channelTypeVo.getUuid();
//	        this.name = channelTypeVo.getName();
//	        this.isActive = channelTypeVo.getIsActive();
//	        this.icon = channelTypeVo.getIcon();
//	        this.color = channelTypeVo.getColor();
//	        this.description = channelTypeVo.getDescription();
//	        this.sort = channelTypeVo.getSort();
//	        this.prefix = channelTypeVo.getPrefix();
//	    }
//    }
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
