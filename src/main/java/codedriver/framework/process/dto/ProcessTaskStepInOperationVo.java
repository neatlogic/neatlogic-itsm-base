package codedriver.framework.process.dto;

public class ProcessTaskStepInOperationVo {

    private Long processTaskId;
    private Long processTaskStepId;
    private String operationType;
    public ProcessTaskStepInOperationVo(Long processTaskId, Long processTaskStepId, String operationType) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.operationType = operationType;
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
}
