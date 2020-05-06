package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessTaskStepVo extends BasePageVo {

	private final static Logger logger = LoggerFactory.getLogger(ProcessTaskStepVo.class);
	
	@EntityField(name = "工单步骤id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	private Long fromProcessTaskStepId;
	private Long startProcessTaskStepId;
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
	private Integer isCheck;
	@EntityField(name = "开始时间", type = ApiParamType.LONG)
	private Date startTime;
	@EntityField(name = "结束时间", type = ApiParamType.LONG)
	private Date endTime;
	@EntityField(name = "超时时间点", type = ApiParamType.LONG)
	private Date expireTime;
	@EntityField(name = "步骤配置信息", type = ApiParamType.LONG)
	private transient String config;
	private Long expireTimeLong;
	private String error;
	private String result;
	private String configHash;
	private transient JSONObject configObj;
	private Boolean isAllDone = false;
	private Boolean isCurrentUserDone = false;
	private Boolean isWorkerPolicyListSorted = false;
	//private Boolean isAttributeListSorted = false;
	private Boolean isTimeoutPolicyListSorted = false;
	//@EntityField(name = "处理人列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepUserVo> userList = new ArrayList<>();
	//@EntityField(name = "处理组列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepTeamVo> teamList = new ArrayList<>();
	private List<ProcessTaskStepRelVo> relList = new ArrayList<>();
	@EntityField(name = "有权限处理人列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepWorkerVo> workerList = new ArrayList<>();
	private List<ProcessTaskStepWorkerPolicyVo> workerPolicyList = new ArrayList<>();
	private List<ProcessTaskStepTimeoutPolicyVo> timeoutPolicyList = new ArrayList<>();
	private List<ProcessTaskStepFormAttributeVo> formAttributeList = new ArrayList<>();
	private JSONObject paramObj;
	@EntityField(name = "表单属性显示控制", type = ApiParamType.JSONOBJECT)
	private Map<String, String> formAttributeActionMap;
	@EntityField(name = "处理人", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStepUserVo majorUser;
//	@EntityField(name = "处理人列表", type = ApiParamType.JSONARRAY)
//	private List<ProcessTaskStepUserVo> majorUserList = new ArrayList<>();
	@EntityField(name = "子任务处理人列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepUserVo> minorUserList = new ArrayList<>();
	@EntityField(name = "代办人列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepUserVo> agentUserList = new ArrayList<>();
	@EntityField(name = "暂存评论附件或上报描述附件", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStepCommentVo comment;
	@EntityField(name = "评论附件列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepAuditVo> processTaskStepAuditList = new ArrayList<>();
	@EntityField(name = "回复是否必填", type = ApiParamType.INTEGER)
	private Integer isRequired;
	@EntityField(name = "流转方向", type = ApiParamType.STRING)
	private String flowDirection = "";
	@EntityField(name = "子任务列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepSubtaskVo> processTaskStepSubtaskList = new ArrayList<>();
	@EntityField(name = "当前用户是否有权限看到该步骤内容", type = ApiParamType.INTEGER)
	private Integer isView;
	@EntityField(name = "可分配处理人的步骤列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepVo> assignableWorkerStepList = new ArrayList<>();
	
	private transient String aliasName;
	
	public ProcessTaskStepVo() {

	}

	public ProcessTaskStepVo(ProcessStepVo processStepVo) {
		this.setProcessUuid(processStepVo.getProcessUuid());
		this.setProcessStepUuid(processStepVo.getUuid());
		this.setName(processStepVo.getName());
		this.setHandler(processStepVo.getHandler());
		this.setType(processStepVo.getType());
		this.setConfig(processStepVo.getConfig());
		this.setFormUuid(processStepVo.getFormUuid());
		if (processStepVo.getUserList() != null && processStepVo.getUserList().size() > 0) {
			List<ProcessTaskStepUserVo> userList = new ArrayList<>();
			for (ProcessStepUserVo userVo : processStepVo.getUserList()) {
				ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(userVo);
				userList.add(processTaskStepUserVo);
			}
			this.setUserList(userList);
		}
		if (processStepVo.getTeamList() != null && processStepVo.getTeamList().size() > 0) {
			List<ProcessTaskStepTeamVo> teamList = new ArrayList<>();
			for (ProcessStepTeamVo teamVo : processStepVo.getTeamList()) {
				ProcessTaskStepTeamVo processTaskStepTeamVo = new ProcessTaskStepTeamVo(teamVo);
				teamList.add(processTaskStepTeamVo);
			}
			this.setTeamList(teamList);
		}
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
		if (processStepVo.getTimeoutPolicyList() != null && processStepVo.getTimeoutPolicyList().size() > 0) {
			List<ProcessTaskStepTimeoutPolicyVo> timeoutList = new ArrayList<>();
			for (ProcessStepTimeoutPolicyVo policyVo : processStepVo.getTimeoutPolicyList()) {
				policyVo.setProcessStepUuid(processStepVo.getUuid());
				ProcessTaskStepTimeoutPolicyVo processTaskStepTimeoutPolicyVo = new ProcessTaskStepTimeoutPolicyVo(policyVo);
				timeoutList.add(processTaskStepTimeoutPolicyVo);
			}
			this.setTimeoutPolicyList(timeoutList);
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
			result += getId().hashCode() * 7;
		}
		if (getProcessStepUuid() != null) {
			result += getProcessStepUuid().hashCode() * 11;
		}
		return result;
	}

	public Long getId() {
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
		if(statusVo == null && StringUtils.isNotBlank(status)) {
			if (MapUtils.isNotEmpty(getConfigObj())) {
				JSONArray customStatusList = getConfigObj().getJSONArray("customStatusList");
				if(CollectionUtils.isNotEmpty(customStatusList)) {
					for(int i = 0; i < customStatusList.size(); i++) {
						JSONObject customStatus = customStatusList.getJSONObject(i);							
						if(status.equals(customStatus.getString("name"))) {
							String value = customStatus.getString("value");
							if(StringUtils.isNotBlank(value)) {
								statusVo = new ProcessTaskStatusVo(status, value);
								return statusVo;
							}
						}
					}
				}
			}
			statusVo = new ProcessTaskStatusVo(status);
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

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public JSONObject getConfigObj() {
		if(configObj == null && StringUtils.isNotBlank(config)) {
			try {
				configObj = JSONObject.parseObject(config);
			} catch (Exception ex) {
				if(StringUtils.isNotBlank(configHash)) {
					logger.error("hash为" + configHash + "的processtask_step_config内容不是合法的JSON格式", ex);					
				}
			}
		}
		return configObj;
	}

	public void setConfigObj(JSONObject configObj) {
		this.configObj = configObj;
	}

	public List<ProcessTaskStepUserVo> getUserList() {
		return userList;
	}

	public void setUserList(List<ProcessTaskStepUserVo> userList) {
		this.userList = userList;
	}

	public List<ProcessTaskStepTeamVo> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<ProcessTaskStepTeamVo> teamList) {
		this.teamList = teamList;
	}

	public Integer getIsRequired() {
		if(isRequired == null && MapUtils.isNotEmpty(getConfigObj())) {
			JSONObject workerPolicyConfig = getConfigObj().getJSONObject("workerPolicyConfig");
			if (MapUtils.isNotEmpty(workerPolicyConfig)) {
				isRequired = workerPolicyConfig.getInteger("isRequired");
			}
		}
		return isRequired;
	}

	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
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

	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
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

	public List<ProcessTaskStepTimeoutPolicyVo> getTimeoutPolicyList() {
		if (!isTimeoutPolicyListSorted && timeoutPolicyList != null && timeoutPolicyList.size() > 0) {
			Collections.sort(timeoutPolicyList);
			isTimeoutPolicyListSorted = true;
		}
		return timeoutPolicyList;
	}

	public void setTimeoutPolicyList(List<ProcessTaskStepTimeoutPolicyVo> timeoutPolicyList) {
		this.timeoutPolicyList = timeoutPolicyList;
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

//	public List<ProcessTaskStepUserVo> getMajorUserList() {
//		return majorUserList;
//	}
//
//	public void setMajorUserList(List<ProcessTaskStepUserVo> majorUserList) {
//		this.majorUserList = majorUserList;
//	}

	public List<ProcessTaskStepUserVo> getMinorUserList() {
		return minorUserList;
	}

	public void setMinorUserList(List<ProcessTaskStepUserVo> minorUserList) {
		this.minorUserList = minorUserList;
	}

	public List<ProcessTaskStepUserVo> getAgentUserList() {
		return agentUserList;
	}

	public void setAgentUserList(List<ProcessTaskStepUserVo> agentUserList) {
		this.agentUserList = agentUserList;
	}

	public ProcessTaskStepCommentVo getComment() {
		return comment;
	}

	public void setComment(ProcessTaskStepCommentVo comment) {
		this.comment = comment;
	}

	public List<ProcessTaskStepAuditVo> getProcessTaskStepAuditList() {
		return processTaskStepAuditList;
	}

	public void setProcessTaskStepAuditList(List<ProcessTaskStepAuditVo> processTaskStepAuditList) {
		this.processTaskStepAuditList = processTaskStepAuditList;
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

}
