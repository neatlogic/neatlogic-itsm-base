package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ConditionProcessTaskOptions {
    TASKID("id","common.workorderid"),
    STEPID("stepid","common.stepid"),
    TITLE("title", "common.title"),
    CHANNELTYPE("channeltype", "common.channeltype"),
    CONTENT("content", "common.contend"),
    STARTTIME("starttime", "common.starttime"),
    OWNER("owner", "common.owner"),
    PRIORITY("priority", "common.priority"),
    OWNERCOMPANY("ownercompany", "common.ownercompany"),
    OWNERDEPARTMENT("ownerdepartment", "common.reporterdepartment"),
    OWNERROLE("ownerrole", "common.reporterrole"),
    OWNERLEVEL("ownerlevel", "common.reporterlevel")
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
