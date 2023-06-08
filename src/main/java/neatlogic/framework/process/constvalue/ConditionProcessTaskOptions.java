package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ConditionProcessTaskOptions {
    TASKID("id","工单ID"),
    STEPID("stepid","步骤ID"),
    TITLE("title", "标题"),
    CHANNELTYPE("channeltype", "服务类型"),
    CONTENT("content", "上报内容"),
    STARTTIME("starttime", "开始时间"),
    OWNER("owner", "上报人"),
    PRIORITY("priority", "优先级"),
    OWNERCOMPANY("ownercompany", "上报人公司"),
    OWNERDEPARTMENT("ownerdepartment", "上报人部门"),
    OWNERROLE("ownerrole", "上报人角色"),
    OWNERLEVEL("ownerlevel", "上报人级别")
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
        return $.t(text);
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
