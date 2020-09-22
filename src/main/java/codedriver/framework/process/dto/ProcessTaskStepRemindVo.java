package codedriver.framework.process.dto;

import java.util.Date;

public class ProcessTaskStepRemindVo {

    private Long processTaskId;
    private Long processTaskStepId;
    private String action;
    private String actionName;
    private String title;
    private String content;
    private String contentHash;
    private String detail;
    private String fcu;
    private String fcuName;
    private Date fcd;
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
    public String getDetailHash() {
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
    public String getFcu() {
        return fcu;
    }
    public void setFcu(String fcu) {
        this.fcu = fcu;
    }
    public String getFcuName() {
        return fcuName;
    }
    public void setFcuName(String fcuName) {
        this.fcuName = fcuName;
    }
    public Date getFcd() {
        return fcd;
    }
    public void setFcd(Date fcd) {
        this.fcd = fcd;
    }
}
