package codedriver.framework.process.dto;

public class ProcessTaskStepFormAttributeVo {
//    @ESKey(type = ESKeyType.PKEY, name ="processTaskId")
	private Long processTaskId;
	private Long processTaskStepId;
	private String action;
	private String processStepUuid;
	private String attributeUuid;
	private String type;

	public ProcessTaskStepFormAttributeVo() {

	}

	public ProcessTaskStepFormAttributeVo(Long _processTaskStepId, String _attributeUuid) {
		this.processTaskStepId = _processTaskStepId;
		this.attributeUuid = _attributeUuid;
	}

	public ProcessTaskStepFormAttributeVo(Long _processTaskStepId) {
		this.processTaskStepId = _processTaskStepId;
	}

	public ProcessTaskStepFormAttributeVo(ProcessStepFormAttributeVo processStepFormAttributeVo) {
		this.setProcessStepUuid(processStepFormAttributeVo.getProcessStepUuid());
		this.setAttributeUuid(processStepFormAttributeVo.getAttributeUuid());
		this.setType(processStepFormAttributeVo.getType());
		this.setAction(processStepFormAttributeVo.getAction());
	}

	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}

	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}

	public String getProcessStepUuid() {
		return processStepUuid;
	}

	public void setProcessStepUuid(String processStepUuid) {
		this.processStepUuid = processStepUuid;
	}

	public String getAttributeUuid() {
		return attributeUuid;
	}

	public void setAttributeUuid(String attributeUuid) {
		this.attributeUuid = attributeUuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}
