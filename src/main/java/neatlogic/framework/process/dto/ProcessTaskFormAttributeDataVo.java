package neatlogic.framework.process.dto;

import neatlogic.framework.form.dto.AttributeDataVo;

public class ProcessTaskFormAttributeDataVo extends AttributeDataVo
    implements Comparable<ProcessTaskFormAttributeDataVo> {
//    @ESKey(type = ESKeyType.PKEY, name = "processTaskId")
    private Long processTaskId;
    private Integer sort;

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
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
