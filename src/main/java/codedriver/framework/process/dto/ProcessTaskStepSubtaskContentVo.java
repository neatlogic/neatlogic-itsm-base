package codedriver.framework.process.dto;

import codedriver.framework.common.dto.BaseEditorVo;

@Deprecated
public class ProcessTaskStepSubtaskContentVo extends BaseEditorVo {
	private Long id;
	private Long processTaskStepSubtaskId;
	private String contentHash;
	private String content;
	private String action;

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
}
