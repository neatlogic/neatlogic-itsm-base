package codedriver.framework.process.dto;

import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.util.SnowflakeUtil;

public class ProcessTaskRelationVo extends BasePageVo {
    private Long id;
    private Long channelTypeRelationId;
    private Long source;
    private Long target;

    private Long channelTypeRelationName;
    private Long processTaskId;
    private Long tilte;
    private ProcessTaskStatusVo statusVo;
    private ChannelTypeVo channelTypeVo;
    
    private transient Boolean isAutoGenerateId = true;
    public ProcessTaskRelationVo() {}
    public ProcessTaskRelationVo(Long channelTypeRelationId, Long source, Long target) {
        this.channelTypeRelationId = channelTypeRelationId;
        this.source = source;
        this.target = target;
    }
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
    public Long getChannelTypeRelationName() {
        return channelTypeRelationName;
    }
    public void setChannelTypeRelationName(Long channelTypeRelationName) {
        this.channelTypeRelationName = channelTypeRelationName;
    }
    public Long getProcessTaskId() {
        return processTaskId;
    }
    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }
    public Long getTilte() {
        return tilte;
    }
    public void setTilte(Long tilte) {
        this.tilte = tilte;
    }
    public ProcessTaskStatusVo getStatusVo() {
        return statusVo;
    }
    public void setStatusVo(ProcessTaskStatusVo statusVo) {
        this.statusVo = statusVo;
    }
    public ChannelTypeVo getChannelTypeVo() {
        return channelTypeVo;
    }
    public void setChannelTypeVo(ChannelTypeVo channelTypeVo) {
        this.channelTypeVo = channelTypeVo;
    }
}
