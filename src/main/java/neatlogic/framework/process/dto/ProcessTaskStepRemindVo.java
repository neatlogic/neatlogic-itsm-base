package neatlogic.framework.process.dto;

import neatlogic.framework.common.dto.BaseEditorVo;


public class ProcessTaskStepRemindVo extends BaseEditorVo {

    private Long processTaskId;
    private Long processTaskStepId;
    private String action;
    private String actionName;
    private String title;
    private String content;
    private String contentHash;
    private String detail;
    public ProcessTaskStepRemindVo() {}
    public ProcessTaskStepRemindVo(Long processTaskStepId) {
        this.processTaskStepId = processTaskStepId;
    }
    public ProcessTaskStepRemindVo(Long processTaskStepId, String action) {
        this.processTaskStepId = processTaskStepId;
        this.action = action;
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
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getActionName() {
        return actionName;
    }
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContentHash() {
        return contentHash;
    }
    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
