package codedriver.framework.process.dto;

public class ProcessTaskFormAttributeDataVo extends AttributeDataVo implements Comparable<ProcessTaskFormAttributeDataVo>{
	private Long processTaskId;
	private String type;
	private Integer sort;
	
	public Long getProcessTaskId() {
		return processTaskId;
	}

	public void setProcessTaskId(Long processTaskId) {
		this.processTaskId = processTaskId;
	}

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
