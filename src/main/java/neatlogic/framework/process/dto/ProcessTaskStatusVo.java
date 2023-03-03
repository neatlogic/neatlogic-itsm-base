package neatlogic.framework.process.dto;

import neatlogic.framework.process.constvalue.ProcessTaskStatus;

public class ProcessTaskStatusVo {

	private String status;
	private String text;
	private String color;
	public ProcessTaskStatusVo() {
	}
	public ProcessTaskStatusVo(String status) {
		this.status = status;
		this.text = ProcessTaskStatus.getText(status);
		this.color = ProcessTaskStatus.getColor(status);
	}
	public ProcessTaskStatusVo(String status, String text) {
		this.status = status;
		this.text = text;
		this.color = ProcessTaskStatus.getColor(status);
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
