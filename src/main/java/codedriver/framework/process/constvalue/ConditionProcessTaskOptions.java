package codedriver.framework.process.constvalue;

public enum ConditionProcessTaskOptions {
    TITLE("title", "标题"),
    CHANNELTYPE("channeltype", "服务类型"),
    CONTENT("content", "上报内容"),
    STARTTIME("starttime", "开始时间"),
    OWNER("owner", "上报人"),
    PRIORITY("priority", "优先级"),
    OWNERCOMPANY("ownercompany", "上报人公司"),
    OWNERDEPARTMENT("ownerdepartment", "上报人部门"),
    OWNERLEVEL("ownerlevel", "上报人级别")
    ;
    private String value;
    private String text;
    private ConditionProcessTaskOptions(String value, String text) {
        this.value = value;
        this.text = text;
    }
    public String getValue() {
        return value;
    }
    public String getText() {
        return text;
    }
    public static ConditionProcessTaskOptions getConditionProcessTaskOprion(String _value) {
        for(ConditionProcessTaskOptions e : values()) {
            if(e.value.equals(_value)) {
                return e;
            }
        }
        return null;
    }
}
