package codedriver.framework.process.dto;

public class ActionVo {

	private Long processTaskStepId;
	private String processTaskStepName;
	private String trigger;
	private String triggerText;
	private String integrationUuid;
	private String integrationName;
	private boolean isSucceed;
	private String statusText;
	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}
	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}
	public String getProcessTaskStepName() {
		return processTaskStepName;
	}
	public void setProcessTaskStepName(String processTaskStepName) {
		this.processTaskStepName = processTaskStepName;
	}
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public String getTriggerText() {
		return triggerText;
	}
	public void setTriggerText(String triggerText) {
		this.triggerText = triggerText;
	}
	public String getIntegrationUuid() {
		return integrationUuid;
	}
	public void setIntegrationUuid(String integrationUuid) {
		this.integrationUuid = integrationUuid;
	}
	public String getIntegrationName() {
		return integrationName;
	}
	public void setIntegrationName(String integrationName) {
		this.integrationName = integrationName;
	}
	public boolean isSucceed() {
		return isSucceed;
	}
	public void setSucceed(boolean isSucceed) {
		this.isSucceed = isSucceed;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
}
