package codedriver.framework.process.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import codedriver.framework.asynchronization.threadlocal.UserContext;

public class ProcessTaskStepContentVo {
	private Long id;
	private Long processTaskId;
	private Long processTaskStepId;
	private String contentHash;
	private Date fcd;
	private String fcu;
	private Date lcd;
	private String lcu;

	public ProcessTaskStepContentVo() {

	}

	public ProcessTaskStepContentVo(Long _processTaskId, Long _processTaskStepId, String _contentHash) {
		processTaskId = _processTaskId;
		processTaskStepId = _processTaskStepId;
		contentHash = _contentHash;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}

	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}

	public String getContentHash() {
		return contentHash;
	}

	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}

	public Date getFcd() {
		return fcd;
	}

	public void setFcd(Date fcd) {
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

	public Date getLcd() {
		return lcd;
	}

	public void setLcd(Date lcd) {
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

	public Long getProcessTaskId() {
		return processTaskId;
	}

	public void setProcessTaskId(Long processTaskId) {
		this.processTaskId = processTaskId;
	}

}
