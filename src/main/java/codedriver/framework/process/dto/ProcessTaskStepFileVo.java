package codedriver.framework.process.dto;

public class ProcessTaskStepFileVo {

	private Long processTaskId;
	private Long processTaskStepId;
	private Long fileId;
	private Long contentId;

	public ProcessTaskStepFileVo() {
	}

	public ProcessTaskStepFileVo(Long processTaskId, Long processTaskStepId, Long fileId, Long contentId) {
		this.processTaskId = processTaskId;
		this.processTaskStepId = processTaskStepId;
		this.fileId = fileId;
		this.contentId = contentId;
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

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
}
