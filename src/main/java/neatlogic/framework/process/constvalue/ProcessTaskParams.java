package neatlogic.framework.process.constvalue;

public enum ProcessTaskParams {
    TASKID("id", "工单ID"),
    STEPID("stepid", "步骤ID"),
    STEPNAME("stepname", "步骤名称"),
    TITLE("title", "标题"),
    CHANNELTYPE("channeltype", "服务类型"),
    CONTENT("content", "上报内容"),
    STARTTIME("starttime", "开始时间"),
    OWNER("owner", "上报人"),
    OWNERUSERID("owneruserid", "上报人用户ID"),
    PRIORITY("priority", "优先级"),
    OWNERGROUP("ownergroup", "上报人集团"),
    OWNERCOMPANY("ownercompany", "上报人公司"),
    OWNERCENTER("ownercenter", "上报人中心"),
    OWNERDEPARTMENT("ownerdepartment", "上报人部门"),
    OWNERDEPARTMENTPATH("ownerdepartmentpath", "上报人部门(完整)"),
    OWNERTEAM("ownerteam", "上报人组"),
    OWNERROLE("ownerrole", "上报人角色"),
    OWNERLEVEL("ownerlevel", "上报人级别"),
    STEPTASK("steptask", "子任务"),
    ACTIONTRIGGERUSER("actiontriggeruser", "动作触发人"),
    ACTIONTRIGGERUSERID("actiontriggeruserid", "动作触发人用户ID"),
    STEPTASKID("steptaskid", "子任务ID");

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
        return text;
    }

}
