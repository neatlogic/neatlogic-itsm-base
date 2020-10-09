package codedriver.framework.process.dto;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessTaskStepAuditVo {
	@EntityField(name = "活动id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "步骤id", type = ApiParamType.LONG)
	private Long processTaskStepId;
	@EntityField(name = "步骤名称", type = ApiParamType.STRING)
	private String processTaskStepName;
	@EntityField(name = "用户userUuid", type = ApiParamType.STRING)
	private String userUuid;
	@EntityField(name = "用户名", type = ApiParamType.STRING)
	private String userName;
	@EntityField(name = "用户其他属性", type = ApiParamType.STRING)
	private String userInfo;
	@EntityField(name = "用户头像", type = ApiParamType.STRING)
	private String avatar;
	@EntityField(name = "创建时间", type = ApiParamType.LONG)
	private Date actionTime;
	@EntityField(name = "活动类型，startprocess(上报)、complete(完成)、retreat(撤回)、abort(终止)、recover(恢复)、transfer(转交)、updateTitle(更新标题)、updatePriority(更新优先级)、updateContent(更新上报描述内容)、comment(评论)", type = ApiParamType.STRING)
	private String action;
	//@EntityField(name = "活动详情列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepAuditDetailVo> auditDetailList;
	
	@EntityField(name = "目标步骤id", type = ApiParamType.LONG)
	private Long nextStepId;
	@EntityField(name = "目标步骤名称", type = ApiParamType.STRING)
	private String nextStepName;
	@EntityField(name = "步骤状态", type = ApiParamType.STRING)
	private String stepStatus;
	@EntityField(name = "步骤状态信息", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStatusVo stepStatusVo;
	@EntityField(name = "描述", type = ApiParamType.STRING)
	private String description;
    @EntityField(name = "原始处理人", type = ApiParamType.STRING)
    private String originalUser;
    @EntityField(name = "原始处理人名", type = ApiParamType.STRING)
    private String originalUserName;
	
	public ProcessTaskStepAuditVo() { 
	}
	
	public ProcessTaskStepAuditVo(Long _processTaskId,String _action) { 
		this.processTaskId = _processTaskId;
		this.action = _action;
	}

	public ProcessTaskStepAuditVo(Long processTaskId, Long processTaskStepId, String userUuid, String action) {
		this.processTaskId = processTaskId;
		this.processTaskStepId = processTaskStepId;
		this.userUuid = userUuid;
		this.action = action;
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

	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}

	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}

	public String getProcessTaskStepName() {
		return processTaskStepName;
	}

	public void setProcessTaskStepName(String processTaskStepName) {
		this.processTaskStepName = processTaskStepName;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUserName() {
		if(StringUtils.isBlank(userName) && StringUtils.isNotBlank(userUuid)) {
			userName = SystemUser.getUserName(userUuid);
			if(StringUtils.isBlank(userName)) {
				userName = userUuid;
			}
		}
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<ProcessTaskStepAuditDetailVo> getAuditDetailList() {
		return auditDetailList;
	}

	public void setAuditDetailList(List<ProcessTaskStepAuditDetailVo> auditDetailList) {
		this.auditDetailList = auditDetailList;
	}

	public Long getNextStepId() {
		return nextStepId;
	}

	public void setNextStepId(Long nextStepId) {
		this.nextStepId = nextStepId;
	}

	public String getNextStepName() {
		return nextStepName;
	}

	public void setNextStepName(String nextStepName) {
		this.nextStepName = nextStepName;
	}

	public String getStepStatus() {
		return stepStatus;
	}

	public void setStepStatus(String stepStatus) {
		this.stepStatus = stepStatus;
	}

	public ProcessTaskStatusVo getStepStatusVo() {
		if(stepStatusVo == null && StringUtils.isNotBlank(stepStatus)) {
			stepStatusVo = new ProcessTaskStatusVo(stepStatus);
		}
		return stepStatusVo;
	}

	public void setStepStatusVo(ProcessTaskStatusVo stepStatusVo) {
		this.stepStatusVo = stepStatusVo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getAvatar(){
		if (StringUtils.isBlank(avatar) && StringUtils.isNotBlank(userInfo)) {
			JSONObject jsonObject = JSONObject.parseObject(userInfo);
			avatar = jsonObject.getString("avatar");
		}
		return avatar;
	}

    public String getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(String originalUser) {
        this.originalUser = originalUser;
    }

    public String getOriginalUserName() {
        return originalUserName;
    }

    public void setOriginalUserName(String originalUserName) {
        this.originalUserName = originalUserName;
    }
}
