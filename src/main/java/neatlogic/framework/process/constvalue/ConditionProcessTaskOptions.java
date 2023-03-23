package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ConditionProcessTaskOptions {
    TASKID("id","enum.process.conditionprocesstaskoptions.taskid"),
    STEPID("stepid","enum.process.conditionprocesstaskoptions.stepid"),
    TITLE("title", "enum.process.conditionprocesstaskoptions.title"),
    CHANNELTYPE("channeltype", "enum.process.conditionprocesstaskoptions.channeltype"),
    CONTENT("content", "enum.process.conditionprocesstaskoptions.content"),
    STARTTIME("starttime", "enum.process.conditionprocesstaskoptions.starttime"),
    OWNER("owner", "enum.process.conditionprocesstaskoptions.owner"),
    PRIORITY("priority", "enum.process.conditionprocesstaskoptions.priority"),
    OWNERCOMPANY("ownercompany", "enum.process.conditionprocesstaskoptions.ownercompany"),
    OWNERDEPARTMENT("ownerdepartment", "enum.process.conditionprocesstaskoptions.ownerdepartment"),
    OWNERROLE("ownerrole", "enum.process.conditionprocesstaskoptions.ownerrole"),
    OWNERLEVEL("ownerlevel", "enum.process.conditionprocesstaskoptions.ownerlevel")
    ;
    private String value;
    private String text;
    ConditionProcessTaskOptions(String value, String text) {
        this.value = value;
        this.text = text;
    }
    public String getValue() {
        return value;
    }
    public String getText() {
        return I18nUtils.getMessage(text);
    }
//    public static ConditionProcessTaskOptions getConditionProcessTaskOprion(String _value) {
//        for(ConditionProcessTaskOptions e : values()) {
//            if(e.value.equals(_value)) {
//                return e;
//            }
//        }
//        return null;
//    }
}
