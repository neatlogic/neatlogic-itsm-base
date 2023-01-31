package neatlogic.framework.process.constvalue;

public enum ProcessTaskParams {
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
    OWNERLEVEL("ownerlevel", "上报人级别"),
    STEPTASK("steptask", "子任务"),
    ACTIONTRIGGERUSER("actiontriggeruser", "动作触发人"),
    STEPTASKID("steptaskid", "子任务ID")
    ;

    private String value;
    private String text;
//    private ParamType paramType;
//    private String freemarkerTemplate;

    ProcessTaskParams(String value, String text) {
        this.value = value;
        this.text = text;
    }

//    ProcessTaskParams(String value, String text, ParamType paramType) {
//        this.value = value;
//        this.text = text;
//        this.paramType = paramType;
//    }

//    private ProcessTaskParams(String value, String text, ParamType paramType, String freemarkerTemplate) {
//        this.value = value;
//        this.text = text;
//        this.paramType = paramType;
//        this.freemarkerTemplate = freemarkerTemplate;
//    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

//    public ParamType getParamType() {
//        return paramType;
//    }

//    public String getFreemarkerTemplate() {
//        if (freemarkerTemplate == null && paramType != null) {
//            freemarkerTemplate = paramType.getFreemarkerTemplate(value);
//        }
//        return freemarkerTemplate;
//    }

}
