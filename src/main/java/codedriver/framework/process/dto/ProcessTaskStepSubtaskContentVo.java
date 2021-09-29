package codedriver.framework.process.dto;

import codedriver.framework.common.dto.BaseEditorVo;

@Deprecated
public class ProcessTaskStepSubtaskContentVo extends BaseEditorVo {
	private Long id;
	private Long processTaskStepSubtaskId;
	private String contentHash;
	private String content;
	private String action;
//	private Date fcd;
//	private UserVo fcuVo;
//	private String fcuName;
//	private String fcuInfo;
//	private String fcuAvatar;
//	private Integer fcuVipLevel;
//	private Date lcd;
//	private UserVo lcuVo;
//	private String lcuInfo;
//	private String lcuAvatar;
//	private Integer lcuVipLevel;

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

//	public Date getFcd() {
//		return fcd;
//	}
//
//	public void setFcd(Date fcd) {
//		this.fcd = fcd;
//	}
//
//	public Date getLcd() {
//		return lcd;
//	}
//
//	public void setLcd(Date lcd) {
//		this.lcd = lcd;
//	}
//
//	public UserVo getFcuVo() {
//		return fcuVo;
//	}
//
//	public void setFcuVo(UserVo fcuVo) {
//		this.fcuVo = fcuVo;
//	}
//
//	public UserVo getLcuVo() {
//		return lcuVo;
//	}
//
//	public void setLcuVo(UserVo lcuVo) {
//		this.lcuVo = lcuVo;
//	}
}
