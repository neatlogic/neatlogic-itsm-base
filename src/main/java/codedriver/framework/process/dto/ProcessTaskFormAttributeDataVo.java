package codedriver.framework.process.dto;

public class ProcessTaskFormAttributeDataVo extends AttributeDataVo implements Comparable<ProcessTaskFormAttributeDataVo>{
	private Long processTaskId;
	//private Long processTaskStepId;
	private String type;
	//private String formUuid;
	private Integer sort;
	
	public Long getProcessTaskId() {
		return processTaskId;
	}

	public void setProcessTaskId(Long processTaskId) {
		this.processTaskId = processTaskId;
	}

//	public Long getProcessTaskStepId() {
//		return processTaskStepId;
//	}
//
//	public void setProcessTaskStepId(Long processTaskStepId) {
//		this.processTaskStepId = processTaskStepId;
//	}

//	public String getFormUuid() {
//		return formUuid;
//	}
//
//	public void setFormUuid(String formUuid) {
//		this.formUuid = formUuid;
//	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public int compareTo(ProcessTaskFormAttributeDataVo attributeData) {
		return this.sort.compareTo(attributeData.getSort());
	}

}
