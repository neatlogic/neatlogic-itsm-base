package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.List;


public class ChannelRelationVo {
    private String source;
    private Long channelTypeRelationId;
    private String target;
    private List<String> targetList = new ArrayList<>();
    private List<String> authorityList = new ArrayList<>();

    private transient String type;
    private transient String uuid;
    
    public Long getChannelTypeRelationId() {
        return channelTypeRelationId;
    }
    public void setChannelTypeRelationId(Long channelTypeRelationId) {
        this.channelTypeRelationId = channelTypeRelationId;
    }   
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public List<String> getTargetList() {
        return targetList;
    }
    public void setTargetList(List<String> targetList) {
        this.targetList = targetList;
    }

    public List<String> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<String> authorityList) {
        this.authorityList = authorityList;
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
