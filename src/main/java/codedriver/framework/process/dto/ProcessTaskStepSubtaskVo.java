package codedriver.framework.process.dto;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessTaskStepSubtaskVo {
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "步骤id", type = ApiParamType.LONG)
	private Long processTaskStepId;
	@EntityField(name = "子任务id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "创建人", type = ApiParamType.STRING)
	private String owner;
	@EntityField(name = "状态", type = ApiParamType.STRING)
	private String status;
	@EntityField(name = "状态名", type = ApiParamType.STRING)
	private String statusText;
	@EntityField(name = "处理人", type = ApiParamType.STRING)
	private String userId;
	@EntityField(name = "处理人名称", type = ApiParamType.STRING)
	private String userName;
	@EntityField(name = "期望完成时间", type = ApiParamType.LONG)
	private Date targetTime;
	@EntityField(name = "创建时间", type = ApiParamType.LONG)
	private Date createTime;
	@EntityField(name = "开始时间", type = ApiParamType.LONG)
	private Date startTime;
	@EntityField(name = "结束时间", type = ApiParamType.LONG)
	private Date endTime;
	@EntityField(name = "取消人", type = ApiParamType.STRING)
	private String cancelUser;
	private Date cancelTime;
	@EntityField(name = "描述", type = ApiParamType.STRING)
	private String content;
	
	@EntityField(name = "是否可编辑", type = ApiParamType.INTEGER)
	private Integer isEditable;
	@EntityField(name = "是否可取消", type = ApiParamType.INTEGER)
	private Integer isAbortable;
	@EntityField(name = "是否可打回重做", type = ApiParamType.INTEGER)
	private Integer isRedoable;
	@EntityField(name = "是否可完成", type = ApiParamType.INTEGER)
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
	public String getStatusText() {
		if(StringUtils.isBlank(statusText) && StringUtils.isNotBlank(status)) {
			statusText = ProcessTaskStatus.getText(status);
		}
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
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
