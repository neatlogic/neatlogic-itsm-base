package codedriver.framework.process.dto;

import org.apache.commons.lang3.StringUtils;

import codedriver.framework.asynchronization.threadlocal.UserContext;

public class ProcessTaskStepSubtaskContentVo {
	private Long processTaskStepSubtaskId;
	private String contentHash;
	private String fcd;
	private String fcu;
	private String lcd;
	private String lcu;

	public ProcessTaskStepSubtaskContentVo() {

	}

	public ProcessTaskStepSubtaskContentVo(Long _processTaskStepSubtaskId, String _contentHash) {
		processTaskStepSubtaskId = _processTaskStepSubtaskId;
		contentHash = _contentHash;
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
