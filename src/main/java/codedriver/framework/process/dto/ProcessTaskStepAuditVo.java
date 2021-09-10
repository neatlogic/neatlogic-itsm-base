package codedriver.framework.process.dto;

import java.util.Date;
import java.util.List;

import codedriver.framework.dto.UserVo;
import codedriver.framework.dto.WorkAssignmentUnitVo;
import codedriver.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessTaskStepAuditVo {
	@EntityField(name = "活动id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "步骤id", type = ApiParamType.LONG)
	private Long processTaskStepId;
	@EntityField(name = "用户")
	private WorkAssignmentUnitVo userVo;
	@EntityField(name = "用户userUuid", type = ApiParamType.STRING)
	private String userUuid;
	@EntityField(name = "创建时间", type = ApiParamType.LONG)
	private Date actionTime;
	@EntityField(name = "活动类型", type = ApiParamType.STRING)
	private String action;
	//@EntityField(name = "活动详情列表", type = ApiParamType.JSONARRAY)
	private List<ProcessTaskStepAuditDetailVo> auditDetailList;
	@EntityField(name = "步骤状态", type = ApiParamType.STRING)
	private String stepStatus;
	@EntityField(name = "步骤状态信息", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStatusVo stepStatusVo;
	@EntityField(name = "描述", type = ApiParamType.STRING)
	private String description;
	@EntityField(name = "原始处理人Vo")
	private WorkAssignmentUnitVo originalUserVo;
    @EntityField(name = "原始处理人uuid", type = ApiParamType.STRING)
    private String originalUser;
	@JSONField(serialize = false)
	private String descriptionHash;

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
		if(id == null){
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

	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}

	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}

	public WorkAssignmentUnitVo getUserVo() {
		return userVo;
	}

	public void setUserVo(WorkAssignmentUnitVo userVo) {
		this.userVo = userVo;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
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

	public WorkAssignmentUnitVo getOriginalUserVo() {
		return originalUserVo;
	}

	public void setOriginalUserVo(WorkAssignmentUnitVo originalUserVo) {
		this.originalUserVo = originalUserVo;
	}


    public String getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(String originalUser) {
        this.originalUser = originalUser;
    }

	public String getDescriptionHash() {
		return descriptionHash;
	}

	public void setDescriptionHash(String descriptionHash) {
		this.descriptionHash = descriptionHash;
	}
}
