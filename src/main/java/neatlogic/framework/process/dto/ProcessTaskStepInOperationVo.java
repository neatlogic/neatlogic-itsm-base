package neatlogic.framework.process.dto;

import neatlogic.framework.common.config.Config;
import neatlogic.framework.util.SnowflakeUtil;

import java.util.Date;

public class ProcessTaskStepInOperationVo {

    private Long id;
    private Long processTaskId;
    private Long processTaskStepId;
    private String operationType;
    private Date expireTime;
    private Integer serverId;
    public ProcessTaskStepInOperationVo() {

    }
    public ProcessTaskStepInOperationVo(Long processTaskId, Long processTaskStepId, String operationType) {
        this.id = SnowflakeUtil.uniqueLong();
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.operationType = operationType;
        //设置数据默认有效时间为6秒
        this.expireTime = new Date(System.currentTimeMillis() + 6000);
        this.serverId = Config.SCHEDULE_SERVER_ID;
    }
    public ProcessTaskStepInOperationVo(Long processTaskId, Long processTaskStepId, String operationType, Date expireTime) {
        this.id = SnowflakeUtil.uniqueLong();
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.operationType = operationType;
        this.expireTime = expireTime;
        this.serverId = Config.SCHEDULE_SERVER_ID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }
    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }
    public Long getProcessTaskStepId() {
        return processTaskStepId;
    }
    public void setProcessTaskStepId(Long processTaskStepId) {
        this.processTaskStepId = processTaskStepId;
    }
    public String getOperationType() {
        return operationType;
    }
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
}
