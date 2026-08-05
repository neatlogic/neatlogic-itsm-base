package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessTaskParams {
    TASKID("id", "nfpc.processtaskparams.text.taskid"),
    STEPID("stepid", "nfpc.processtaskparams.text.stepid"),
    STEPNAME("stepname", "nfpc.processtaskparams.text.stepname"),
    STEPUSER("stepuser", "nfpc.processtaskparams.text.stepuser"),
    STEPUSERID("stepuserid", "nfpc.processtaskparams.text.stepuserid"),
    TITLE("title", "nfpc.processtaskparams.text.title"),
    CHANNELTYPE("channeltype", "nfpc.processtaskparams.text.channeltype"),
    CONTENT("content", "nfpc.processtaskparams.text.content"),
    STARTTIME("starttime", "nfpc.processtaskparams.text.starttime"),
    OWNER("owner", "nfpc.processtaskparams.text.owner"),
    OWNERUSERID("owneruserid", "nfpc.processtaskparams.text.owneruserid"),
    PRIORITY("priority", "nfpc.processtaskparams.text.priority"),
    OWNERGROUP("ownergroup", "nfpc.processtaskparams.text.ownergroup"),
    OWNERCOMPANY("ownercompany", "nfpc.processtaskparams.text.ownercompany"),
    OWNERCENTER("ownercenter", "nfpc.processtaskparams.text.ownercenter"),
    OWNERDEPARTMENT("ownerdepartment", "nfpc.processtaskparams.text.ownerdepartment"),
    OWNERDEPARTMENTPATH("ownerdepartmentpath", "nfpc.processtaskparams.text.ownerdepartmentpath"),
    OWNERTEAM("ownerteam", "nfpc.processtaskparams.text.ownerteam"),
    OWNERROLE("ownerrole", "nfpc.processtaskparams.text.ownerrole"),
    OWNERLEVEL("ownerlevel", "nfpc.processtaskparams.text.ownerlevel"),
    STEPTASK("steptask", "nfpc.processtaskparams.text.steptask"),
    ACTIONTRIGGERUSER("actiontriggeruser", "nfpc.processtaskparams.text.actiontriggeruser"),
    ACTIONTRIGGERUSERID("actiontriggeruserid", "nfpc.processtaskparams.text.actiontriggeruserid"),
    STEPTASKID("steptaskid", "nfpc.processtaskparams.text.steptaskid");

    private String value;
    private String text;

    ProcessTaskParams(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return $.t(text);
    }

}
