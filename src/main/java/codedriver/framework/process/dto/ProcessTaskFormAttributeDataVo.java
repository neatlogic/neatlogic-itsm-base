package codedriver.framework.process.dto;

import codedriver.framework.form.dto.AttributeDataVo;

public class ProcessTaskFormAttributeDataVo extends AttributeDataVo
    implements Comparable<ProcessTaskFormAttributeDataVo> {
//    @ESKey(type = ESKeyType.PKEY, name = "processTaskId")
    private Long processTaskId;
    private String type;
    private Integer sort;
    private String label;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int compareTo(ProcessTaskFormAttributeDataVo attributeData) {
        return this.sort.compareTo(attributeData.getSort());
    }

}
