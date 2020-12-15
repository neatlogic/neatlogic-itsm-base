package codedriver.framework.process.dto;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class ProcessTaskSerialNumberPolicyVo {
    @JSONField(serialize = false)
    private transient String channelTypeUuid;
    private String handler;
    private String name;
    private JSONArray formAttributeList;
    @JSONField(serialize = false)
    private transient JSONObject config;
    @JSONField(serialize = false)
    private transient Long serialNumberSeed;

    private Date startTime;
    private Date endTime;
    
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

    @JSONField(serialize = false)
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
