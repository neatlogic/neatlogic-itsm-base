package neatlogic.framework.process.dto;

public class ProcessTaskStepAgentVo {

    private Long processTaskId;
    private Long processTaskStepId;
    private String userUuid;
    private String agentUuid;
    public ProcessTaskStepAgentVo(Long processTaskId, Long processTaskStepId, String userUuid, String agentUuid) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.userUuid = userUuid;
        this.agentUuid = agentUuid;
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
    public String getUserUuid() {
        return userUuid;
    }
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }
    public String getAgentUuid() {
        return agentUuid;
    }
    public void setAgentUuid(String agentUuid) {
        this.agentUuid = agentUuid;
    }
}
