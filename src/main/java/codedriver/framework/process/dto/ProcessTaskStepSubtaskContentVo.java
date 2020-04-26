package codedriver.framework.process.dto;

import org.apache.commons.lang3.StringUtils;

import codedriver.framework.asynchronization.threadlocal.UserContext;

public class ProcessTaskStepSubtaskContentVo {
	private Long id;
	private Long processTaskStepSubtaskId;
	private String contentHash;
	private String content;
	private String action;
	private String fcd;
	private String fcu;
	private String fcuName;
	private String lcd;
	private String lcu;

	public ProcessTaskStepSubtaskContentVo() {

	}

	public ProcessTaskStepSubtaskContentVo(Long id, String contentHash) {
		this.id = id;
		this.contentHash = contentHash;
	}

	public ProcessTaskStepSubtaskContentVo(Long _processTaskStepSubtaskId, String _action, String _contentHash) {
		processTaskStepSubtaskId = _processTaskStepSubtaskId;
		contentHash = _contentHash;
		action = _action;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcessTaskStepSubtaskId() {
		return processTaskStepSubtaskId;
	}

	public void setProcessTaskStepSubtaskId(Long processTaskStepSubtaskId) {
		this.processTaskStepSubtaskId = processTaskStepSubtaskId;
	}

	public String getContentHash() {
		return contentHash;
	}

	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFcd() {
		return fcd;
	}

	public void setFcd(String fcd) {
		this.fcd = fcd;
	}

	public String getFcu() {
		if (StringUtils.isBlank(fcu)) {
			fcu = UserContext.get().getUserId();
		}
		return fcu;
	}

	public void setFcu(String fcu) {
		this.fcu = fcu;
	}

	public String getFcuName() {
		return fcuName;
	}

	public void setFcuName(String fcuName) {
		this.fcuName = fcuName;
	}

	public String getLcd() {
		return lcd;
	}

	public void setLcd(String lcd) {
		this.lcd = lcd;
	}

	public String getLcu() {
		if (StringUtils.isBlank(lcu)) {
			lcu = UserContext.get().getUserId();
		}
		return lcu;
	}

	public void setLcu(String lcu) {
		this.lcu = lcu;
	}

}
