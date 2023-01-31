package neatlogic.framework.process.dto;

import neatlogic.framework.form.dto.AttributeDataVo;

public class ProcessTaskFormAttributeDataVo extends AttributeDataVo
    implements Comparable<ProcessTaskFormAttributeDataVo> {
//    @ESKey(type = ESKeyType.PKEY, name = "processTaskId")
    private Long processTaskId;
    private String type;
    private Integer sort;
    /**
     * 这个字段已废弃，只有旧邮件模板用到，父类AttributeDataVo中attribureLabel与该字段作用相同
     * @param label
     */
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

    /**
     * 这个方法已废弃，只有旧邮件模板用到，父类AttributeDataVo中getAttribureLabel()与该方法作用相同
     * @return
     */
    @Deprecated
    public String getLabel() {
        return super.getAttributeLabel();
    }

    /**
     * 这个方法已废弃，只有旧邮件模板用到，父类AttributeDataVo中setAttribureLabel()与该方法作用相同
     * @param label
     */
    @Deprecated
    public void setLabel(String label) {
        super.setAttributeLabel(label);
    }

    @Override
    public int compareTo(ProcessTaskFormAttributeDataVo attributeData) {
        return this.sort.compareTo(attributeData.getSort());
    }

}
