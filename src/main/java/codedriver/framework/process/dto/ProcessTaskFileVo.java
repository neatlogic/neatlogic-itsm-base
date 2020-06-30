package codedriver.framework.process.dto;

public class ProcessTaskFileVo {

	private Long processTaskId;
	private Long processTaskStepId;
	private Long fileId;

	public ProcessTaskFileVo() {
	}

	public ProcessTaskFileVo(Long processTaskId, Long processTaskStepId, Long fileId) {
		this.processTaskId = processTaskId;
		this.processTaskStepId = processTaskStepId;
		this.fileId = fileId;
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

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
}
