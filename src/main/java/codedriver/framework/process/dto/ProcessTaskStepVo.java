package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import codedriver.framework.dto.UserVo;
import codedriver.framework.form.dto.FormAttributeVo;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.process.stephandler.core.ProcessStepInternalHandlerFactory;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;

public class ProcessTaskStepVo extends BasePageVo {

	@EntityField(name = "工单步骤id", type = ApiParamType.LONG)
	private Long id;
//	@ESKey(type = ESKeyType.PKEY, name ="processTaskId")
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	private Long fromProcessTaskStepId;
	@JSONField(serialize=false)
	private transient Long startProcessTaskStepId;
	private String processUuid;
	private String processStepUuid;
	@EntityField(name = "步骤名称", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "状态", type = ApiParamType.STRING)
	private String status;
	@EntityField(name = "状态信息", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStatusVo statusVo;
	@EntityField(name = "步骤处理器", type = ApiParamType.STRING)
	private String handler;
	@EntityField(name = "步骤类型", type = ApiParamType.STRING)
	private String type;
	private String formUuid;
	private Integer isActive = 0;
    @EntityField(name = "激活时间", type = ApiParamType.LONG)
    private Date activeTime;
	@EntityField(name = "开始时间", type = ApiParamType.LONG)
	private Date startTime;
	@EntityField(name = "结束时间", type = ApiParamType.LONG)
	private Date endTime;
	@EntityField(name = "超时时间点", type = ApiParamType.LONG)
	private Date expireTime;
//	@JSONField(serialize=false)
//	private transient String config;
	private Long expireTimeLong;
	private String error;
	private String result;
	private String configHash;
	private Boolean isAllDone = false;
	private Boolean isCurrentUserDone = false;
	private Boolean isWorkerPolicyListSorted = false;
	//@EntityField(name = "处理人列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepUserVo> userList = new ArrayList<>();
	@JSONField(serialize=false)
	private transient List<ProcessTaskStepRelVo> relList = new ArrayList<>();
	@EntityField(name = "有权限处理人列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepWorkerVo> workerList = new ArrayList<>();
	private List<ProcessTaskStepWorkerPolicyVo> workerPolicyList = new ArrayList<>();
	private List<ProcessTaskStepFormAttributeVo> formAttributeList = new ArrayList<>();
	private List<FormAttributeVo> formAttributeVoList = new ArrayList<>();
	
	@JSONField(serialize=false)
	private transient JSONObject paramObj;
	@EntityField(name = "表单属性显示控制", type = ApiParamType.JSONOBJECT)
	private Map<String, String> formAttributeActionMap;
	@EntityField(name = "处理人", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStepUserVo majorUser;
	@EntityField(name = "子任务处理人列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepUserVo> minorUserList = new ArrayList<>();
	@EntityField(name = "暂存评论附件或上报描述附件", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStepReplyVo comment;
	@EntityField(name = "评论附件列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepReplyVo> commentList = new ArrayList<>();
	@EntityField(name = "是否需要回复框", type = ApiParamType.INTEGER)
	private Integer isNeedContent;
	@EntityField(name = "回复是否必填", type = ApiParamType.INTEGER)
	private Integer isRequired;
	@EntityField(name = "流转方向", type = ApiParamType.STRING)
	private String flowDirection;
	@EntityField(name = "子任务列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepSubtaskVo> processTaskStepSubtaskList = new ArrayList<>();
	@EntityField(name = "当前用户是否有权限看到该步骤内容", type = ApiParamType.INTEGER)
	private Integer isView;
	@EntityField(name = "可分配处理人的步骤列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepVo> assignableWorkerStepList = new ArrayList<>();
	@EntityField(name = "时效列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskSlaTimeVo> slaTimeList = new ArrayList<>();
	@JSONField(serialize=false)
	private transient String aliasName;
	@JSONField(serialize=false)
	private transient Boolean isAutoGenerateId = false;
	
	@EntityField(name = "步骤数据", type = ApiParamType.JSONOBJECT)
	private JSONObject processTaskStepData;
	//@EntityField(name = "当前子任务Id", type = ApiParamType.LONG)
	@JSONField(serialize=false)
	private transient Long currentSubtaskId;
    //@EntityField(name = "当前子任务", type = ApiParamType.JSONOBJECT)
	@JSONField(serialize=false)
    private transient ProcessTaskStepSubtaskVo currentSubtaskVo;
	@EntityField(name = "处理器特有的步骤信息", type = ApiParamType.JSONOBJECT)
	private Object handlerStepInfo;
	@EntityField(name = "向前步骤列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepVo> forwardNextStepList = new ArrayList<>();
	@EntityField(name = "向后步骤列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepVo> backwardNextStepList = new ArrayList<>();
	@JSONField(serialize=false)
	private transient Map<String, Object> formAttributeDataMap;
    
    @EntityField(name = "步骤表单属性隐藏数据", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepFormAttributeVo> stepFormConfig = new ArrayList<>();
    
    @EntityField(name = "提醒列表", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepRemindVo> processTaskStepRemindList = new ArrayList<>();
    @EntityField(name = "原始处理人uuid", type = ApiParamType.STRING)
    private String originalUser;
	@EntityField(name = "原始处理人")
	private UserVo originalUserVo;
//    @EntityField(name = "原始处理人名", type = ApiParamType.STRING)
//    private String originalUserName;
    @EntityField(name = "回复模版", type = ApiParamType.JSONOBJECT)
    private ProcessCommentTemplateVo commentTemplate;
    private transient int updateActiveTime;
    private transient int updateStartTime;
    private transient int updateEndTime;
	public ProcessTaskStepVo() {

	}

	public ProcessTaskStepVo(ProcessStepVo processStepVo) {
		this.isAutoGenerateId = true;
		this.setProcessUuid(processStepVo.getProcessUuid());
		this.setProcessStepUuid(processStepVo.getUuid());
		this.setName(processStepVo.getName());
		this.setHandler(processStepVo.getHandler());
		this.setType(processStepVo.getType());
		this.setFormUuid(processStepVo.getFormUuid());

		if (processStepVo.getFormAttributeList() != null && processStepVo.getFormAttributeList().size() > 0) {
			List<ProcessTaskStepFormAttributeVo> attributeList = new ArrayList<>();
			for (ProcessStepFormAttributeVo attributeVo : processStepVo.getFormAttributeList()) {
				attributeVo.setProcessStepUuid(processStepVo.getUuid());
				ProcessTaskStepFormAttributeVo processTaskStepAttributeVo = new ProcessTaskStepFormAttributeVo(attributeVo);
				attributeList.add(processTaskStepAttributeVo);
			}
			this.setFormAttributeList(attributeList);
		}
		if (processStepVo.getWorkerPolicyList() != null && processStepVo.getWorkerPolicyList().size() > 0) {
			List<ProcessTaskStepWorkerPolicyVo> policyList = new ArrayList<>();
			for (ProcessStepWorkerPolicyVo policyVo : processStepVo.getWorkerPolicyList()) {
				policyVo.setProcessStepUuid(processStepVo.getUuid());
				ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo = new ProcessTaskStepWorkerPolicyVo(policyVo);
				policyList.add(processTaskStepWorkerPolicyVo);
			}
			this.setWorkerPolicyList(policyList);
		}

	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof ProcessTaskStepVo))
			return false;

		final ProcessTaskStepVo step = (ProcessTaskStepVo) other;
		try {
			if (getId() != null && getId().equals(step.getId())) {
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 0;
		if (getId() != null) {
			result += getId().hashCode() * 37;
		}
		return result;
	}

	public synchronized Long getId() {
		if(id == null && isAutoGenerateId) {
			id = SnowflakeUtil.uniqueLong();
		}
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcessTaskId() {
		return processTaskId;
	}

	public void setProcessTaskId(Long processTaskId) {
		this.processTaskId = processTaskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ProcessTaskStatusVo getStatusVo() {
		if(statusVo == null && StringUtils.isNotBlank(status) && StringUtils.isNotBlank(configHash) && StringUtils.isNotBlank(handler)) {
			String statusText = ProcessStepInternalHandlerFactory.getHandler().getStatusTextByConfigHashAndHandler(configHash, handler, status);
			if (StringUtils.isNotBlank(statusText)) {
				statusVo = new ProcessTaskStatusVo(status, statusText);
			} else {
				statusVo = new ProcessTaskStatusVo(status);
			}
		}
		return statusVo;
	}

	public void setStatusVo(ProcessTaskStatusVo statusVo) {
		this.statusVo = statusVo;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActived) {
		this.isActive = isActived;
	}

	public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
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

//	public String getConfig() {
//		return config;
//	}
//
//	public void setConfig(String config) {
//		this.config = config;
//	}

	public List<ProcessTaskStepUserVo> getUserList() {
		return userList;
	}

	public void setUserList(List<ProcessTaskStepUserVo> userList) {
		this.userList = userList;
	}

	public Integer getIsRequired() {
		if(isRequired == null && StringUtils.isNotBlank(configHash)) {
			isRequired = ProcessStepInternalHandlerFactory.getHandler().getIsRequiredByConfigHash(configHash);
		}
		return isRequired;
	}

	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}

	public Integer getIsNeedContent() {
		if(isNeedContent == null && StringUtils.isNotBlank(configHash)) {
			isNeedContent = ProcessStepInternalHandlerFactory.getHandler().getIsNeedContentByConfigHash(configHash);
		}
		return isNeedContent;
	}

	public void setIsNeedContent(Integer isNeedContent) {
		this.isNeedContent = isNeedContent;
	}

	public String getFlowDirection() {
		return flowDirection;
	}

	public void setFlowDirection(String flowDirection) {
		this.flowDirection = flowDirection;
	}

	public List<ProcessTaskStepRelVo> getRelList() {
		return relList;
	}

	public void setRelList(List<ProcessTaskStepRelVo> relList) {
		this.relList = relList;
	}

	public List<ProcessTaskStepWorkerVo> getWorkerList() {
		return workerList;
	}

	public void setWorkerList(List<ProcessTaskStepWorkerVo> workerList) {
		this.workerList = workerList;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getProcessStepUuid() {
		return processStepUuid;
	}

	public void setProcessStepUuid(String processStepUuid) {
		this.processStepUuid = processStepUuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProcessUuid() {
		return processUuid;
	}

	public void setProcessUuid(String processUuid) {
		this.processUuid = processUuid;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void appendError(String error) {
		if (this.error != null) {
			this.error += "\n";
			this.error += error;
		} else {
			this.error = error;
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Long getFromProcessTaskStepId() {
		return fromProcessTaskStepId;
	}

	public void setFromProcessTaskStepId(Long fromProcessTaskStepId) {
		this.fromProcessTaskStepId = fromProcessTaskStepId;
	}

	public List<ProcessTaskStepWorkerPolicyVo> getWorkerPolicyList() {
		if (!isWorkerPolicyListSorted && workerPolicyList != null && workerPolicyList.size() > 0) {
			Collections.sort(workerPolicyList);
			isWorkerPolicyListSorted = true;
		}
		return workerPolicyList;
	}

	public void setWorkerPolicyList(List<ProcessTaskStepWorkerPolicyVo> workerPolicyList) {
		this.workerPolicyList = workerPolicyList;
	}

	public JSONObject getParamObj() {
		if(paramObj == null) {
			paramObj = new JSONObject();
		}
		return paramObj;
	}

	public void setParamObj(JSONObject paramObj) {
		this.paramObj = paramObj;
	}

	public Boolean getIsAllDone() {
		return isAllDone;
	}

	public void setIsAllDone(Boolean isAllDone) {
		if (isAllDone) {
			this.isCurrentUserDone = isAllDone;
		}
		this.isAllDone = isAllDone;
	}

	public Boolean getIsCurrentUserDone() {
		return isCurrentUserDone;
	}

	public void setIsCurrentUserDone(Boolean isCurrentUserDone) {
		this.isCurrentUserDone = isCurrentUserDone;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Long getExpireTimeLong() {
		return expireTimeLong;
	}

	public void setExpireTimeLong(Long expireTimeLong) {
		this.expireTimeLong = expireTimeLong;
	}

	public String getFormUuid() {
		return formUuid;
	}

	public void setFormUuid(String formUuid) {
		this.formUuid = formUuid;
	}

	public List<ProcessTaskStepFormAttributeVo> getFormAttributeList() {
		return formAttributeList;
	}

	public void setFormAttributeList(List<ProcessTaskStepFormAttributeVo> formAttributeList) {
		this.formAttributeList = formAttributeList;
	}

	public Long getStartProcessTaskStepId() {
		return startProcessTaskStepId;
	}

	public void setStartProcessTaskStepId(Long startProcessTaskStepId) {
		this.startProcessTaskStepId = startProcessTaskStepId;
	}

	public String getConfigHash() {
		return configHash;
	}

	public void setConfigHash(String configHash) {
		this.configHash = configHash;
	}

	public Map<String, String> getFormAttributeActionMap() {
		return formAttributeActionMap;
	}

	public void setFormAttributeActionMap(Map<String, String> formAttributeActionMap) {
		this.formAttributeActionMap = formAttributeActionMap;
	}

	public List<ProcessTaskStepUserVo> getMinorUserList() {
		return minorUserList;
	}

	public void setMinorUserList(List<ProcessTaskStepUserVo> minorUserList) {
		this.minorUserList = minorUserList;
	}

	public ProcessTaskStepReplyVo getComment() {
		return comment;
	}

	public void setComment(ProcessTaskStepReplyVo comment) {
		this.comment = comment;
	}

	public List<ProcessTaskStepReplyVo> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<ProcessTaskStepReplyVo> commentList) {
		this.commentList = commentList;
	}

	public List<ProcessTaskStepSubtaskVo> getProcessTaskStepSubtaskList() {
		return processTaskStepSubtaskList;
	}

	public void setProcessTaskStepSubtaskList(List<ProcessTaskStepSubtaskVo> processTaskStepSubtaskList) {
		this.processTaskStepSubtaskList = processTaskStepSubtaskList;
	}

	public Integer getIsView() {
		return isView;
	}

	public void setIsView(Integer isView) {
		this.isView = isView;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public List<ProcessTaskStepVo> getAssignableWorkerStepList() {
		return assignableWorkerStepList;
	}

	public void setAssignableWorkerStepList(List<ProcessTaskStepVo> assignableWorkerStepList) {
		this.assignableWorkerStepList = assignableWorkerStepList;
	}

	public ProcessTaskStepUserVo getMajorUser() {
		return majorUser;
	}

	public void setMajorUser(ProcessTaskStepUserVo majorUser) {
		this.majorUser = majorUser;
	}

	public List<ProcessTaskSlaTimeVo> getSlaTimeList() {
		return slaTimeList;
	}

	public void setSlaTimeList(List<ProcessTaskSlaTimeVo> slaTimeList) {
		this.slaTimeList = slaTimeList;
	}

	public Boolean getIsAutoGenerateId() {
		return isAutoGenerateId;
	}

	public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
		this.isAutoGenerateId = isAutoGenerateId;
	}

	public JSONObject getProcessTaskStepData() {
		return processTaskStepData;
	}

	public void setProcessTaskStepData(JSONObject processTaskStepData) {
		this.processTaskStepData = processTaskStepData;
	}

	public Long getCurrentSubtaskId() {
		return currentSubtaskId;
	}

	public void setCurrentSubtaskId(Long currentSubtaskId) {
		this.currentSubtaskId = currentSubtaskId;
	}

	public ProcessTaskStepSubtaskVo getCurrentSubtaskVo() {
        return currentSubtaskVo;
    }

    public void setCurrentSubtaskVo(ProcessTaskStepSubtaskVo currentSubtaskVo) {
        this.currentSubtaskVo = currentSubtaskVo;
    }

    public Object getHandlerStepInfo() {
		return handlerStepInfo;
	}

	public void setHandlerStepInfo(Object handlerStepInfo) {
		this.handlerStepInfo = handlerStepInfo;
	}

	public List<ProcessTaskStepVo> getForwardNextStepList() {
		return forwardNextStepList;
	}

	public void setForwardNextStepList(List<ProcessTaskStepVo> forwardNextStepList) {
		this.forwardNextStepList = forwardNextStepList;
	}

	public List<ProcessTaskStepVo> getBackwardNextStepList() {
		return backwardNextStepList;
	}

	public void setBackwardNextStepList(List<ProcessTaskStepVo> backwardNextStepList) {
		this.backwardNextStepList = backwardNextStepList;
	}

    public Map<String, Object> getFormAttributeDataMap() {
        return formAttributeDataMap;
    }

    public void setFormAttributeDataMap(Map<String, Object> formAttributeDataMap) {
        this.formAttributeDataMap = formAttributeDataMap;
    }

    public List<ProcessTaskStepFormAttributeVo> getStepFormConfig() {
        return stepFormConfig;
    }

    public void setStepFormConfig(List<ProcessTaskStepFormAttributeVo> stepFormConfig) {
        this.stepFormConfig = stepFormConfig;
    }

    public List<ProcessTaskStepRemindVo> getProcessTaskStepRemindList() {
        return processTaskStepRemindList;
    }

    public void setProcessTaskStepRemindList(List<ProcessTaskStepRemindVo> processTaskStepRemindList) {
        this.processTaskStepRemindList = processTaskStepRemindList;
    }
    
    public List<FormAttributeVo> getFormAttributeVoList() {
        return formAttributeVoList;
    }

    public void setFormAttributeVoList(List<FormAttributeVo> formAttributeVoList) {
        this.formAttributeVoList = formAttributeVoList;
    }

    public String getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(String originalUser) {
        this.originalUser = originalUser;
    }

	public UserVo getOriginalUserVo() {
		return originalUserVo;
	}

	public void setOriginalUserVo(UserVo originalUserVo) {
		this.originalUserVo = originalUserVo;
	}

	//    public String getOriginalUserName() {
//        return originalUserName;
//    }
//
//    public void setOriginalUserName(String originalUserName) {
//        this.originalUserName = originalUserName;
//    }

    public int getUpdateActiveTime() {
        return updateActiveTime;
    }

    public void setUpdateActiveTime(int updateActiveTime) {
        this.updateActiveTime = updateActiveTime;
    }

    public int getUpdateStartTime() {
        return updateStartTime;
    }

    public void setUpdateStartTime(int updateStartTime) {
        this.updateStartTime = updateStartTime;
    }

    public int getUpdateEndTime() {
        return updateEndTime;
    }

    public void setUpdateEndTime(int updateEndTime) {
        this.updateEndTime = updateEndTime;
    }

	public ProcessCommentTemplateVo getCommentTemplate() {
		return commentTemplate;
	}

	public void setCommentTemplate(ProcessCommentTemplateVo commentTemplate) {
		this.commentTemplate = commentTemplate;
	}
}
