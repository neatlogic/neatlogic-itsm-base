package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18n;

public enum ProcessTaskParams {
    TASKID("id", new I18n("工单ID")),
    STEPID("stepid", new I18n("步骤ID")),
    TITLE("title", new I18n("标题")),
    CHANNELTYPE("channeltype", new I18n("服务类型")),
    CONTENT("content", new I18n("上报内容")),
    STARTTIME("starttime", new I18n("开始时间")),
    OWNER("owner", new I18n("上报人")),
    PRIORITY("priority", new I18n("优先级")),
    OWNERCOMPANY("ownercompany", new I18n("上报人公司")),
    OWNERDEPARTMENT("ownerdepartment", new I18n("上报人部门")),
    OWNERROLE("ownerrole", new I18n("上报人角色")),
    OWNERLEVEL("ownerlevel", new I18n("上报人级别")),
    STEPTASK("steptask", new I18n("子任务")),
    ACTIONTRIGGERUSER("actiontriggeruser", new I18n("动作触发人")),
    STEPTASKID("steptaskid", new I18n("子任务ID"));

    private String value;
    private I18n text;
//    private ParamType paramType;
//    private String freemarkerTemplate;

    ProcessTaskParams(String value, I18n text) {
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
        return text.toString();
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
