package neatlogic.framework.process.dto;

import com.alibaba.fastjson.annotation.JSONField;

import neatlogic.framework.util.SnowflakeUtil;

public class ProcessTaskTransferReportVo {

    private Long id;
    private Long channelTypeRelationId;
    private Long fromProcessTaskId;
    private Long toProcessTaskId;
    @JSONField(serialize=false)
    private Boolean isAutoGenerateId = true;
    
    public ProcessTaskTransferReportVo() {}
    public ProcessTaskTransferReportVo(Long channelTypeRelationId, Long fromProcessTaskId, Long toProcessTaskId) {
        this.channelTypeRelationId = channelTypeRelationId;
        this.fromProcessTaskId = fromProcessTaskId;
        this.toProcessTaskId = toProcessTaskId;
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
    public Long getFromProcessTaskId() {
        return fromProcessTaskId;
    }
    public void setFromProcessTaskId(Long fromProcessTaskId) {
        this.fromProcessTaskId = fromProcessTaskId;
    }
    public Long getToProcessTaskId() {
        return toProcessTaskId;
    }
    public void setToProcessTaskId(Long toProcessTaskId) {
        this.toProcessTaskId = toProcessTaskId;
    }
}
