package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ConditionProcessTaskOptions {
    TASKID("id", "nfpc.conditionprocesstaskoptions.text.taskid"),
    STEPID("stepid", "nfpc.conditionprocesstaskoptions.text.stepid"),
    TITLE("title", "nfpc.conditionprocesstaskoptions.text.title"),
    CHANNELTYPE("channeltype", "nfpc.conditionprocesstaskoptions.text.channeltype"),
    CONTENT("content", "nfpc.conditionprocesstaskoptions.text.content"),
    STARTTIME("starttime", "nfpc.conditionprocesstaskoptions.text.starttime"),
    OWNER("owner", "nfpc.conditionprocesstaskoptions.text.owner"),
    OWNERUSERID("owneruserid", "nfpc.conditionprocesstaskoptions.text.owneruserid"),
    PRIORITY("priority", "nfpc.conditionprocesstaskoptions.text.priority"),
    OWNERGROUP("ownergroup", "nfpc.conditionprocesstaskoptions.text.ownergroup"),
    OWNERCOMPANY("ownercompany", "nfpc.conditionprocesstaskoptions.text.ownercompany"),
    OWNERCENTER("ownercenter", "nfpc.conditionprocesstaskoptions.text.ownercenter"),
    OWNERDEPARTMENT("ownerdepartment", "nfpc.conditionprocesstaskoptions.text.ownerdepartment"),
    OWNERTEAM("ownerteam", "nfpc.conditionprocesstaskoptions.text.ownerteam"),
    OWNERROLE("ownerrole", "nfpc.conditionprocesstaskoptions.text.ownerrole"),
    OWNERLEVEL("ownerlevel", "nfpc.conditionprocesstaskoptions.text.ownerlevel"),
    REGION("region", "nfpc.conditionprocesstaskoptions.text.region");

    private final String value;
    private final String text;

    ConditionProcessTaskOptions(String value, String text) {
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
