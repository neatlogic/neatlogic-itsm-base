package codedriver.framework.process.dto;

public class ProcessTaskTagVo {
    private Long processTaskId;
    private Long tagId;
    
    
    
    public ProcessTaskTagVo() {
        super();
    }
    public ProcessTaskTagVo(Long processTaskId, Long tagId) {
        this.processTaskId = processTaskId;
        this.tagId = tagId;
    }
    public Long getProcessTaskId() {
        return processTaskId;
    }
    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }
    public Long getTagId() {
        return tagId;
    }
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
    
}
