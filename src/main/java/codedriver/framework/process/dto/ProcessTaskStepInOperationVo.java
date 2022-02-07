package codedriver.framework.process.dto;

import java.util.Date;

public class ProcessTaskStepInOperationVo {

    private Long id;
    private Long processTaskId;
    private Long processTaskStepId;
    private String operationType;
    private Date expireTime;
    public ProcessTaskStepInOperationVo() {

    }
    public ProcessTaskStepInOperationVo(Long processTaskId, Long processTaskStepId, String operationType) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.operationType = operationType;
    }
    public ProcessTaskStepInOperationVo(Long processTaskId, Long processTaskStepId, String operationType, Date expireTime) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.operationType = operationType;
        this.expireTime = expireTime;
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
}
