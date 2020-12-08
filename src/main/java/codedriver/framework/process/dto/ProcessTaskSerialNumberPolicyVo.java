package codedriver.framework.process.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ProcessTaskSerialNumberPolicyVo {

    private String channelTypeUuid;
    private String handler;
    private String name;
    private JSONArray formAttributeList;
    private JSONObject config;
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

    public JSONObject getConfig() {
        if (config == null) {
            config = new JSONObject();
        }
        return config;
    }

    public void setConfig(String config) {
        this.config = JSONObject.parseObject(config);
    }

    public String getConfigStr() {
        if (config != null) {
            return config.toJSONString();
        }
        return null;
    }

    public Long getSerialNumberSeed() {
        return serialNumberSeed;
    }

    public void setSerialNumberSeed(Long serialNumberSeed) {
        this.serialNumberSeed = serialNumberSeed;
    }
}
