package codedriver.framework.process.dto;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import codedriver.framework.asynchronization.threadlocal.UserContext;

public class ProcessTaskStepSubtaskContentVo {
	private Long id;
	private Long processTaskStepSubtaskId;
	private String contentHash;
	private String content;
	private String action;
	private Date fcd;
	private String fcu;
	private String fcuName;
	private String fcuInfo;
	private String fcuAvatar;
	private Date lcd;
	private String lcu;
	private String lcuInfo;
	private String lcuAvatar;

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

	public Date getFcd() {
		return fcd;
	}

	public void setFcd(Date fcd) {
		this.fcd = fcd;
	}

	public String getFcu() {
		if (StringUtils.isBlank(fcu)) {
			fcu = UserContext.get().getUserUuid();
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

	public Date getLcd() {
		return lcd;
	}

	public void setLcd(Date lcd) {
		this.lcd = lcd;
	}

	public String getLcu() {
		if (StringUtils.isBlank(lcu)) {
			lcu = UserContext.get().getUserUuid();
		}
		return lcu;
	}

	public void setLcu(String lcu) {
		this.lcu = lcu;
	}

	public String getFcuInfo() {
		return fcuInfo;
	}

	public void setFcuInfo(String fcuInfo) {
		this.fcuInfo = fcuInfo;
	}

	public String getFcuAvatar() {
		if (StringUtils.isBlank(fcuAvatar) && StringUtils.isNotBlank(fcuInfo)) {
			JSONObject jsonObject = JSONObject.parseObject(fcuInfo);
			fcuAvatar = jsonObject.getString("avatar");
		}
		return fcuAvatar;
	}

	public String getLcuInfo() {
		return lcuInfo;
	}

	public void setLcuInfo(String lcuInfo) {
		this.lcuInfo = lcuInfo;
	}

	public String getLcuAvatar() {
		if (StringUtils.isBlank(lcuAvatar) && StringUtils.isNotBlank(lcuInfo)) {
			JSONObject jsonObject = JSONObject.parseObject(lcuInfo);
			lcuAvatar = jsonObject.getString("avatar");
		}
		return lcuAvatar;
	}

}
