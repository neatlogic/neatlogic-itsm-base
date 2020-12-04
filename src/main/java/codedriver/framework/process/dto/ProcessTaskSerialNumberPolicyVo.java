package codedriver.framework.process.dto;

import com.alibaba.fastjson.JSONArray;

public class ProcessTaskSerialNumberPolicyVo {

    private String channelTypeUuid;
    private String handler;
    private String name;
    private JSONArray formAttributeList;
    private String config;
    private Long serialNumberSeed;

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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public JSONArray getFormAttributeList() {
        return formAttributeList;
    }
    public void setFormAttributeList(JSONArray formAttributeList) {
        this.formAttributeList = formAttributeList;
    }
    public String getConfig() {
        return config;
    }
    public void setConfig(String config) {
        this.config = config;
    }
    public Long getSerialNumberSeed() {
        return serialNumberSeed;
    }
    public void setSerialNumberSeed(Long serialNumberSeed) {
        this.serialNumberSeed = serialNumberSeed;
    }
}
