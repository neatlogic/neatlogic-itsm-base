package neatlogic.framework.process.dto;

import neatlogic.framework.process.constvalue.ProcessTaskStepStatus;

public class ProcessTaskStepStatusVo {

	private String status;
	private String text;
	private String color;
	public ProcessTaskStepStatusVo() {
	}
	public ProcessTaskStepStatusVo(String status) {
		this.status = status;
		this.text = ProcessTaskStepStatus.getText(status);
		this.color = ProcessTaskStepStatus.getColor(status);
	}
	public ProcessTaskStepStatusVo(String status, String text) {
		this.status = status;
		this.text = text;
		this.color = ProcessTaskStepStatus.getColor(status);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
