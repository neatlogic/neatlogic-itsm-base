package codedriver.framework.process.dto;

import codedriver.framework.dto.UserVo;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepUserStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class ProcessTaskStepUserVo {
//    @ESKey(type = ESKeyType.PKEY, name ="processTaskId")
	private Long processTaskId;
	private Long processTaskStepId;
	private UserVo userVo;
	private String userUuid;
	private String userName;
	private List<String> userUuidList;
	private String userType;
	private String status = ProcessTaskStepUserStatus.DOING.getValue();
	private String statusName;
	private Date startTime;
	private Date endTime;
	private Date activeTime;
	private String action;
//	private Long timeCost;
//	private String timeCostStr;

	private String newUserUuid;
	
	public ProcessTaskStepUserVo() {

	}

	public ProcessTaskStepUserVo(Long _processTaskId, Long _processTaskStepId, String _userUuid) {
		this.setProcessTaskId(_processTaskId);
		this.setProcessTaskStepId(_processTaskStepId);
		this.userUuid = _userUuid;
	}

	public ProcessTaskStepUserVo(Long _processTaskId, Long _processTaskStepId, String _userUuid, String userType) {
        this.setProcessTaskId(_processTaskId);
        this.setProcessTaskStepId(_processTaskStepId);
		this.userUuid = _userUuid;
        this.setUserType(userType);
    }

	public ProcessTaskStepUserVo(String _status,Date _startTime,Date _endTime, Long _processTaskId, Long _processTaskStepId, String _userUuid, String _userName, String userType) {
		this.status = _status;
		this.startTime = _startTime;
		this.activeTime = _startTime;
		this.endTime = _endTime;
		this.setProcessTaskId(_processTaskId);
		this.setProcessTaskStepId(_processTaskStepId);
		this.userUuid = _userUuid;
		this.userName = _userName;
		this.setUserType(userType);
	}

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

	/**
	 * 这个方法是为了返回userVo属性给前端用的，后端代码不要主动调用这个方法
	 * @return
	 */
	@Deprecated
	public UserVo getUserVo() {
		if (userVo == null && StringUtils.isNotBlank(userUuid)) {
			userVo = new UserVo(userUuid);
			userVo.setUserName(userName);
		}
		return userVo;
	}

	/**
	 * 这个方法是mybatis用的，有些旧sql语句存在，但后端代码不要主动调用这个方法
	 * @param userVo
	 * @return
	 */
	@Deprecated
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
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

	public List<String> getUserUuidList() {
		return userUuidList;
	}

	public void setUserUuidList(List<String> userUuidList) {
		this.userUuidList = userUuidList;
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

//	public Long getTimeCost() {
//		return timeCost;
//	}
//
//	public void setTimeCost(Long timeCost) {
//		this.timeCost = timeCost;
//	}
//
//	public String getTimeCostStr() {
//		return timeCostStr;
//	}
//
//	public void setTimeCostStr(String timeCostStr) {
//		this.timeCostStr = timeCostStr;
//	}

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

    public String getNewUserUuid() {
        return newUserUuid;
    }

    public void setNewUserUuid(String newUserUuid) {
        this.newUserUuid = newUserUuid;
    }

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}
}
