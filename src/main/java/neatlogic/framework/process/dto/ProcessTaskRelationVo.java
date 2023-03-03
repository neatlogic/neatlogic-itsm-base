package neatlogic.framework.process.dto;

import com.alibaba.fastjson.annotation.JSONField;

import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.process.constvalue.ProcessTaskOperationType;
import neatlogic.framework.util.SnowflakeUtil;

public class ProcessTaskRelationVo extends BasePageVo {
    private Long id;
    private Long channelTypeRelationId;
    private Long source;
    private Long target;
    private String action;
    private Integer isDeletable;
    
    private String channelTypeRelationName;
    private Long processTaskId;
    private String title;
    private String serialNumber;
    private ProcessTaskStatusVo statusVo;
    private ChannelTypeVo channelTypeVo;
    @JSONField(serialize=false)
    private Boolean isAutoGenerateId = true;
    public ProcessTaskRelationVo() {}
    public ProcessTaskRelationVo(Long processTaskId) {
        this.processTaskId = processTaskId;
    }
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
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public Boolean getIsAutoGenerateId() {
        return isAutoGenerateId;
    }
    public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
        this.isAutoGenerateId = isAutoGenerateId;
    }
    public String getChannelTypeRelationName() {
        return channelTypeRelationName;
    }
    public void setChannelTypeRelationName(String channelTypeRelationName) {
        this.channelTypeRelationName = channelTypeRelationName;
    }
    public Long getProcessTaskId() {
        return processTaskId;
    }
    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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
    public Integer getIsDeletable() {
        if(isDeletable == null) {
            if(ProcessTaskOperationType.PROCESSTASK_TRANSFERREPORT.getValue().equals(action)) {
                isDeletable = 0;
            }else {
                isDeletable = 1;
            }
        }
        return isDeletable;
    }
    public void setIsDeletable(Integer isDeletable) {
        this.isDeletable = isDeletable;
    }
}
