package codedriver.framework.process.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import codedriver.framework.asynchronization.threadlocal.UserContext;

public class ProcessTaskAssignWorkerVo {

	private Long processTaskId;
	private Long processTaskStepId;
	private Long fromProcessTaskStepId;
	private String fromProcessStepUuid;
	private String type;
	private String uuid;
	private Date fcd;
	private String fcu;

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
	public Long getFromProcessTaskStepId() {
		return fromProcessTaskStepId;
	}
	public void setFromProcessTaskStepId(Long fromProcessTaskStepId) {
		this.fromProcessTaskStepId = fromProcessTaskStepId;
	}
	public String getFromProcessStepUuid() {
		return fromProcessStepUuid;
	}
	public void setFromProcessStepUuid(String fromProcessStepUuid) {
		this.fromProcessStepUuid = fromProcessStepUuid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getFcd() {
		return fcd;
	}
	public void setFcd(Date fcd) {
		this.fcd = fcd;
	}
	public String getFcu() {
		if(StringUtils.isBlank(fcu)) {
			fcu = UserContext.get().getUserId();
		}
		return fcu;
	}
	public void setFcu(String fcu) {
		this.fcu = fcu;
	}
}
