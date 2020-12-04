package codedriver.framework.process.dto;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessTaskSerialNumberPolicyVo {

    private String channelTypeUuid;
    private String handler;
    private String config;
    @EntityField(name = "工单号当前值", type = ApiParamType.STRING)
    private Long autoIncrement;

//    @EntityField(name = "工单号起始值", type = ApiParamType.STRING)
//    private String startValue;
//    @EntityField(name = "工单号位数", type = ApiParamType.STRING)
//    private String digits;
    public String getChannelTypeUuid() {
        return channelTypeUuid;
    }
    public void setChannelTypeUuid(String channelTypeUuid) {
        this.channelTypeUuid = channelTypeUuid;
    }
    public String getHandler() {
        return handler;
    }
    public void setHandler(String handler) {
        this.handler = handler;
    }
    public String getConfig() {
        return config;
    }
    public void setConfig(String config) {
        this.config = config;
    }
    public Long getAutoIncrement() {
        return autoIncrement;
    }
    public void setAutoIncrement(Long autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
