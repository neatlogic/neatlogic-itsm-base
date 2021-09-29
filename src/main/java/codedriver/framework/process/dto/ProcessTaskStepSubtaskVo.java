package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import codedriver.framework.dto.UserVo;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.restful.annotation.EntityField;

@Deprecated
public class ProcessTaskStepSubtaskVo {
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "步骤id", type = ApiParamType.LONG)
	private Long processTaskStepId;
	@EntityField(name = "子任务id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "创建人")
	private UserVo ownerVo;
//	@EntityField(name = "创建人", type = ApiParamType.STRING)
//	private String owner;
//	@EntityField(name = "创建人名称", type = ApiParamType.STRING)
//	private String ownerName;
//	@EntityField(name = "创建人额外信息", type = ApiParamType.STRING)
//	private String ownerInfo;
//	@EntityField(name = "创建人名称", type = ApiParamType.STRING)
//	private String ownerAvatar;
//	@EntityField(name = "创建人VIP等级", type = ApiParamType.INTEGER)
//	private Integer ownerVipLevel;
	@EntityField(name = "状态", type = ApiParamType.STRING)
	private String status;
	@EntityField(name = "状态信息", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStatusVo statusVo;
	@EntityField(name = "处理人uuid", type = ApiParamType.STRING)
	private String userUuid;
	@EntityField(name = "处理人名称", type = ApiParamType.STRING)
	private String userName;
	@EntityField(name = "处理人Vo")
	private UserVo userVo;
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
	@EntityField(name = "描述hash值", type = ApiParamType.STRING)
	private String contentHash;
	@EntityField(name = "回复内容列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepSubtaskContentVo> contentList;
	@EntityField(name = "步骤主处理人", type = ApiParamType.STRING)
	private String majorUser;
	
	@EntityField(name = "是否可编辑", type = ApiParamType.INTEGER)
	private Integer isEditable;
	@EntityField(name = "是否可取消", type = ApiParamType.INTEGER)
	private Integer isAbortable;
	@EntityField(name = "是否可打回重做", type = ApiParamType.INTEGER)
	private Integer isRedoable;
	@EntityField(name = "是否可完成", type = ApiParamType.INTEGER)
	private Integer isCompletable;
	@EntityField(name = "是否可回复", type = ApiParamType.INTEGER)
	private Integer isCommentable;
	
	@EntityField(name = "超时时长", type = ApiParamType.LONG)
	private Long timeout;
	@EntityField(name = "超时时长描述", type = ApiParamType.STRING)
	private String timeoutDesc;
	
	@EntityField(name = "权限操作按钮列表", type = ApiParamType.JSONARRAY)
	private List<ValueTextVo> actionList = new ArrayList<>();
	
	@EntityField(name = "处理人", type = ApiParamType.STRING)
	private String workerList;
	@JSONField(serialize=false)
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

	public UserVo getOwnerVo() {
		return ownerVo;
	}

	public void setOwnerVo(UserVo ownerVo) {
		this.ownerVo = ownerVo;
	}

	//	public String getOwner() {
//		return owner;
//	}
//	public void setOwner(String owner) {
//		this.owner = owner;
//	}
//	public String getOwnerName() {
//		return ownerName;
//	}
//	public void setOwnerName(String ownerName) {
//		this.ownerName = ownerName;
//	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ProcessTaskStatusVo getStatusVo() {
		if(statusVo == null && StringUtils.isNotBlank(status)) {
			statusVo = new ProcessTaskStatusVo(status);
		}
		return statusVo;
	}
	public void setStatusVo(ProcessTaskStatusVo statusVo) {
		this.statusVo = statusVo;
	}
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
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
	public String getContentHash() {
		return contentHash;
	}
	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}
	public List<ProcessTaskStepSubtaskContentVo> getContentList() {
		return contentList;
	}
	public void setContentList(List<ProcessTaskStepSubtaskContentVo> contentList) {
		this.contentList = contentList;
	}
	public String getMajorUser() {
		return majorUser;
	}
	public void setMajorUser(String majorUser) {
		this.majorUser = majorUser;
	}
	public Integer getIsEditable() {
		if(isEditable == null) {
			String currentUser = UserContext.get().getUserUuid();
			if(currentUser != null && currentUser.equals(this.majorUser) && ProcessTaskStatus.RUNNING.getValue().equals(this.status)) {
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
			String currentUser = UserContext.get().getUserUuid();
			if(currentUser != null && currentUser.equals(this.majorUser) && ProcessTaskStatus.RUNNING.getValue().equals(this.status)) {
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
			String currentUser = UserContext.get().getUserUuid();
			if(currentUser != null && currentUser.equals(this.majorUser) && ProcessTaskStatus.SUCCEED.getValue().equals(this.status)) {
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
			String currentUser = UserContext.get().getUserUuid();
			if(currentUser != null && currentUser.equals(this.userUuid) && ProcessTaskStatus.RUNNING.getValue().equals(this.status)) {
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
	public Integer getIsCommentable() {
		if(isCommentable == null) {
			String currentUser = UserContext.get().getUserUuid();
			if(currentUser != null && (currentUser.equals(this.userUuid) || currentUser.equals(this.majorUser)) && ProcessTaskStatus.RUNNING.getValue().equals(this.status)) {
				isCommentable = 1;
			}else {
				isCommentable = 0;
			}
		}
		return isCommentable;
	}
	public void setIsCommentable(Integer isCommentable) {
		this.isCommentable = isCommentable;
	}
	
	public Long getTimeout() {
		if(timeout == null && targetTime != null && endTime != null) {
			timeout = endTime.getTime() - targetTime.getTime();
		}
		return timeout;
	}
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
	
	public String getTimeoutDesc() {
		if(StringUtils.isBlank(timeoutDesc) && getTimeout() != null) {
			StringBuilder stringBuilder = new StringBuilder();
			long timeoutLong = getTimeout();
			if(timeoutLong < 0) {
				stringBuilder.append("提前");
			}else if(timeoutLong > 0){
				stringBuilder.append("超时");
			}else {
				stringBuilder.append("准时");
				timeoutDesc = stringBuilder.toString();
				return timeoutDesc;
			}
			timeoutLong = Math.abs(timeoutLong);
			if(timeoutLong >= (60 * 60 * 1000)) {
				long hours = timeoutLong / (60 * 60 * 1000);
				stringBuilder.append(hours);
				stringBuilder.append("h");
				timeoutLong = timeoutLong % (60 * 60 * 1000);
			}
			if(timeoutLong >= (60 * 1000)) {
				long minutes = timeoutLong / (60 * 1000);
				stringBuilder.append(minutes);
				stringBuilder.append("m");
				timeoutLong = timeoutLong % (60 * 1000);
			}
			if(timeoutLong >= 1000) {
				long seconds = timeoutLong / 1000;
				stringBuilder.append(seconds);
				stringBuilder.append("s");
			}
			timeoutDesc = stringBuilder.toString();
		}
		return timeoutDesc;
	}
	public void setTimeoutDesc(String timeoutDesc) {
		this.timeoutDesc = timeoutDesc;
	}
	
	public List<ValueTextVo> getActionList() {
		return actionList;
	}
	public void setActionList(List<ValueTextVo> actionList) {
		this.actionList = actionList;
	}
	
	public String getWorkerList() {
		if(StringUtils.isBlank(workerList) && StringUtils.isNotBlank(userUuid)) {
			workerList = GroupSearch.USER.getValuePlugin() + userUuid;
		}
		return workerList;
	}
	public void setWorkerList(String workerList) {
		this.workerList = workerList;
	}

//	public String getOwnerInfo() {
//		return ownerInfo;
//	}
//
//	public void setOwnerInfo(String ownerInfo) {
//		this.ownerInfo = ownerInfo;
//	}
//
//	public String getOwnerAvatar() {
//		if (StringUtils.isBlank(ownerAvatar) && StringUtils.isNotBlank(ownerInfo)) {
//			JSONObject jsonObject = JSONObject.parseObject(ownerInfo);
//			ownerAvatar = jsonObject.getString("avatar");
//		}
//		return ownerAvatar;
//	}
//
//	public Integer getOwnerVipLevel() {
//		return ownerVipLevel;
//	}
//
//	public void setOwnerVipLevel(Integer ownerVipLevel) {
//		this.ownerVipLevel = ownerVipLevel;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentHash == null) ? 0 : contentHash.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((targetTime == null) ? 0 : targetTime.hashCode());
		result = prime * result + ((userUuid == null) ? 0 : userUuid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessTaskStepSubtaskVo other = (ProcessTaskStepSubtaskVo) obj;
		if (contentHash == null) {
			if (other.contentHash != null)
				return false;
		} else if (!contentHash.equals(other.contentHash))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (targetTime == null) {
			if (other.targetTime != null)
				return false;
		} else if (!targetTime.equals(other.targetTime))
			return false;
		if (userUuid == null) {
			if (other.userUuid != null)
				return false;
		} else if (!userUuid.equals(other.userUuid))
			return false;
		return true;
	}
	
}
