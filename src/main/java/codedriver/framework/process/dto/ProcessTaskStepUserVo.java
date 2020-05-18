package codedriver.framework.process.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepUserStatus;

public class ProcessTaskStepUserVo {
	private Long processTaskId;
	private Long processTaskStepId;
	private String userUuid;
	private String userName;
	private String userType = "major";
	private String status = ProcessTaskStepUserStatus.DOING.getValue();
	private String statusName;
	private Date startTime;
	private Date endTime;
	private String action;
	private Long timeCost;
	private String timeCostStr;

	public ProcessTaskStepUserVo() {

	}

	public ProcessTaskStepUserVo(Long _processTaskStepId, String _userUuid) {
		this.setProcessTaskStepId(_processTaskStepId);
		this.setUserUuid(_userUuid);
	}

	public ProcessTaskStepUserVo(Long _processTaskId, Long _processTaskStepId, String _userUuid) {
		this.setProcessTaskId(_processTaskId);
		this.setProcessTaskStepId(_processTaskStepId);
		this.setUserUuid(_userUuid);
	}

	public ProcessTaskStepUserVo(ProcessStepUserVo processStepUserVo) {
		this.setUserUuid(processStepUserVo.getUserUuid());
		this.setUserName(processStepUserVo.getUserName());
	}

	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}

	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		if (StringUtils.isNotBlank(status) && StringUtils.isBlank(statusName)) {
			statusName = ProcessTaskStatus.getText(status);
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getTimeCost() {
		return timeCost;
	}

	public void setTimeCost(Long timeCost) {
		this.timeCost = timeCost;
	}

	public String getTimeCostStr() {
		return timeCostStr;
	}

	public void setTimeCostStr(String timeCostStr) {
		this.timeCostStr = timeCostStr;
	}

	public Long getProcessTaskId() {
		return processTaskId;
	}

	public void setProcessTaskId(Long processTaskId) {
		this.processTaskId = processTaskId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
