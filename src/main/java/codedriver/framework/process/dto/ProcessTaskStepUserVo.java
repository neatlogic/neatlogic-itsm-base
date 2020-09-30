package codedriver.framework.process.dto;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import codedriver.framework.elasticsearch.annotation.ESKey;
import codedriver.framework.elasticsearch.constvalue.ESKeyType;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepUserStatus;

public class ProcessTaskStepUserVo {
    @ESKey(type = ESKeyType.PKEY, id ="processTaskId")
	private Long processTaskId;
	private Long processTaskStepId;
	private String userUuid;
	private String userName;
	private String userType;
	private String userInfo;
	private String userAvatar;
	private String status = ProcessTaskStepUserStatus.DOING.getValue();
	private String statusName;
	private Date startTime;
	private Date endTime;
	private String action;
	private Long timeCost;
	private String timeCostStr;

	private String newUserUuid;
	
	public ProcessTaskStepUserVo() {

	}

    /*public ProcessTaskStepUserVo(Long _processTaskStepId, String _userUuid) {
    	this.setProcessTaskStepId(_processTaskStepId);
    	this.setUserUuid(_userUuid);
    }*/

	public ProcessTaskStepUserVo(Long _processTaskId, Long _processTaskStepId, String _userUuid) {
		this.setProcessTaskId(_processTaskId);
		this.setProcessTaskStepId(_processTaskStepId);
		this.setUserUuid(_userUuid);
	}

	public ProcessTaskStepUserVo(Long _processTaskId, Long _processTaskStepId, String _userUuid, String userType) {
        this.setProcessTaskId(_processTaskId);
        this.setProcessTaskStepId(_processTaskStepId);
        this.setUserUuid(_userUuid);
        this.setUserType(userType);
    }
	
//	public ProcessTaskStepUserVo(ProcessStepUserVo processStepUserVo) {
//		this.setUserUuid(processStepUserVo.getUserUuid());
//		this.setUserName(processStepUserVo.getUserName());
//	}

	public ProcessTaskStepUserVo(Long processTaskId, Long processTaskStepId, String userUuid, String userType,
        String newUserUuid) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.userUuid = userUuid;
        this.userType = userType;
        this.newUserUuid = newUserUuid;
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

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getUserAvatar() {
		if (StringUtils.isBlank(userAvatar) && StringUtils.isNotBlank(userInfo)) {
			JSONObject jsonObject = JSONObject.parseObject(userInfo);
			userAvatar = jsonObject.getString("avatar");
		}
		return userAvatar;
	}

    public String getNewUserUuid() {
        return newUserUuid;
    }

    public void setNewUserUuid(String newUserUuid) {
        this.newUserUuid = newUserUuid;
    }

}
