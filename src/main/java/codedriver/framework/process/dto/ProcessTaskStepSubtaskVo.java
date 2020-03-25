package codedriver.framework.process.dto;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.process.constvalue.ProcessTaskStatus;

public class ProcessTaskStepSubtaskVo {

	private Long processTaskId;
	private Long processTaskStepId;
	private Long id;
	private String owner;
	private String status;
	private String userId;
	private String userName;
	private Date targetTime;
	private Date createTime;
	private Date startTime;
	private Date endTime;
	private String cancelUser;
	private Date cancelTime;
	private String content;
	
	private Integer isEditable;
	private Integer isAbortable;
	private Integer isRedoable;
	private Integer isCompletable;
	private JSONObject paramObj;
	
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getTargetTime() {
		return targetTime;
	}
	public void setTargetTime(Date targetTime) {
		this.targetTime = targetTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public String getCancelUser() {
		return cancelUser;
	}
	public void setCancelUser(String cancelUser) {
		this.cancelUser = cancelUser;
	}
	public Date getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	public JSONObject getParamObj() {
		return paramObj;
	}
	public void setParamObj(JSONObject paramObj) {
		this.paramObj = paramObj;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getIsEditable() {
		if(isEditable == null) {
			String currentUser = UserContext.get().getUserId();
			if(currentUser != null && currentUser.equals(this.owner) && ProcessTaskStatus.RUNNING.getValue().equals(this.status)) {
				isEditable = 1;
			}else {
				isEditable = 0;
			}
		}
		return isEditable;
	}
	public void setIsEditable(Integer isEditable) {
		this.isEditable = isEditable;
	}
	public Integer getIsAbortable() {
		if(isAbortable == null) {
			String currentUser = UserContext.get().getUserId();
			if(currentUser != null && currentUser.equals(this.owner) && ProcessTaskStatus.RUNNING.getValue().equals(this.status)) {
				isAbortable = 1;
			}else {
				isAbortable = 0;
			}
		}
		return isAbortable;
	}
	public void setIsAbortable(Integer isAbortable) {
		this.isAbortable = isAbortable;
	}
	public Integer getIsRedoable() {
		if(isRedoable == null) {
			String currentUser = UserContext.get().getUserId();
			if(currentUser != null && currentUser.equals(this.owner) && ProcessTaskStatus.SUCCEED.getValue().equals(this.status)) {
				isRedoable = 1;
			}else {
				isRedoable = 0;
			}
		}
		return isRedoable;
	}
	public void setIsRedoable(Integer isRedoable) {
		this.isRedoable = isRedoable;
	}
	public Integer getIsCompletable() {
		if(isCompletable == null) {
			String currentUser = UserContext.get().getUserId();
			if(currentUser != null && currentUser.equals(this.userId) && ProcessTaskStatus.RUNNING.getValue().equals(this.status)) {
				isCompletable = 1;
			}else {
				isCompletable = 0;
			}
		}
		return isCompletable;
	}
	public void setIsCompletable(Integer isCompletable) {
		this.isCompletable = isCompletable;
	}
	
}
