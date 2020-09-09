package codedriver.framework.process.dto;

import codedriver.framework.util.SnowflakeUtil;

public class ProcessTaskRelationVo {
    private Long id;
    private Long channelTypeRelationId;
    private Long source;
    private Long target;
    
    private transient Boolean isAutoGenerateId = true;
    
    public synchronized Long getId() {
        if(id == null && isAutoGenerateId) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getChannelTypeRelationId() {
        return channelTypeRelationId;
    }
    public void setChannelTypeRelationId(Long channelTypeRelationId) {
        this.channelTypeRelationId = channelTypeRelationId;
    }
    public Long getSource() {
        return source;
    }
    public void setSource(Long source) {
        this.source = source;
    }
    public Long getTarget() {
        return target;
    }
    public void setTarget(Long target) {
        this.target = target;
    }
    public Boolean getIsAutoGenerateId() {
        return isAutoGenerateId;
    }
    public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
        this.isAutoGenerateId = isAutoGenerateId;
    }
}
