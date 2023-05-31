package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18n;

public enum ProcessTaskParams {
    TASKID("id", new I18n("enum.process.processtaskparams.taskid")),
    STEPID("stepid", new I18n("enum.process.processtaskparams.stepid")),
    TITLE("title", new I18n("enum.process.processtaskparams.title")),
    CHANNELTYPE("channeltype", new I18n("enum.process.processtaskparams.channeltype")),
    CONTENT("content", new I18n("enum.process.processtaskparams.content")),
    STARTTIME("starttime", new I18n("enum.process.processtaskparams.starttime")),
    OWNER("owner", new I18n("enum.process.processtaskparams.owner.a")),
    PRIORITY("priority", new I18n("enum.process.processtaskparams.priority")),
    OWNERCOMPANY("ownercompany", new I18n("enum.process.processtaskparams.ownercompany")),
    OWNERDEPARTMENT("ownerdepartment", new I18n("enum.process.processtaskparams.ownerdepartment")),
    OWNERROLE("ownerrole", new I18n("enum.process.processtaskparams.ownerrole")),
    OWNERLEVEL("ownerlevel", new I18n("enum.process.processtaskparams.ownerlevel")),
    STEPTASK("steptask", new I18n("enum.process.processtaskparams.steptask.a")),
    ACTIONTRIGGERUSER("actiontriggeruser", new I18n("enum.process.processtaskparams.actiontriggeruser")),
    STEPTASKID("steptaskid", new I18n("enum.process.processtaskparams.steptaskid"));

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
