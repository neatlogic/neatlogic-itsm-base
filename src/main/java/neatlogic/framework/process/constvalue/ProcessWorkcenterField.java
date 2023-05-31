package neatlogic.framework.process.constvalue;

import neatlogic.framework.dashboard.constvalue.IDashboardGroupField;
import neatlogic.framework.util.I18n;

public enum ProcessWorkcenterField implements IDashboardGroupField {
    ID("id", new I18n("common.workorderid")),
    SERIAL_NUMBER("serialnumber", new I18n("common.tasknumber")),
    TITLE("title", new I18n("common.title")),
    CHANNELTYPE("channeltype", new I18n("common.channeltype")),
    CHANNEL("channel", new I18n("enum.process.processworkcenterfield.channel)")),
    PROCESS("process", new I18n("enum.process.processworkcenterfield.process")),
    CONFIGHASH("confighash", new I18n("enum.process.processworkcenterfield.confighash")),
    CATALOG("catalog", new I18n("common.servicecatalog")),
    CONTENT("content", new I18n("common.contend")),
    CONTENT_INCLUDE_HTML("contentincludehtml", new I18n("common.contend")),
    ENDTIME("endtime", new I18n("common.endtime"), "endTime"),
    STARTTIME("starttime", new I18n("common.starttime"), "startTime"),
    ACTIVETIME("activetime", new I18n("common.activetime"), "activeTime"),
    EXPIRED_TIME("expiretime", new I18n("common.remainingtime")),
    OWNER("owner", new I18n("common.owner")),
    REPORTER("reporter", new I18n("common.reporter")),
    PRIORITY("priority", new I18n("common.priority")),
    STATUS("status", new I18n("common.itsm.processtaskstatus")),
    STEP("step", new I18n("common.step")),
    STEP_STATUS("stepstatus", new I18n("common.stepstatus")),
    STEP_USER("stepuser", new I18n("common.stepuser")),
    STEP_NAME("stepname", new I18n("common.stepname")),
    STEP_TEAM("stepteam", new I18n("enum.process.processworkcenterfield.step_team")),
    CURRENT_STEP("currentstep", new I18n("common.currentstep")),
    WOKRTIME("worktime", new I18n("common.window")),
    TRANSFER_FROM_USER("transferfromuser", new I18n("enum.process.processworkcenterfield.transfer_from_user")),
    ABOUTME("aboutme", new I18n("enum.process.processworkcenterfield.aboutme")),
    ACTION("action", new I18n("enum.process.processworkcenterfield.action")),
    IS_SHOW("isshow", new I18n("enum.process.processworkcenterfield.is_show")),
    FOCUS_USERS("focususers", new I18n("enum.process.processworkcenterfield.focus_users"));
    private final String value;
    private final String valuePro;
    private final I18n text;


    private ProcessWorkcenterField(String _value, I18n _text) {
        this.value = _value;
        this.text = _text;
        this.valuePro = _value;
    }

    private ProcessWorkcenterField(String _value, I18n _text, String _vaulePro) {
        this.value = _value;
        this.valuePro = _vaulePro;
        this.text = _text;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text.toString();
    }

    public String getValuePro() {
        return valuePro;
    }

    public static String getValue(String _value) {
        for (ProcessWorkcenterField s : ProcessWorkcenterField.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _value) {
        for (ProcessWorkcenterField s : ProcessWorkcenterField.values()) {
            if (s.getValue().equals(_value)) {
                return s.getText();
            }
        }
        return "";
    }

    public static String getConditionValue(String _value) {
        for (ProcessWorkcenterField s : ProcessWorkcenterField.values()) {
            if (s.getValue().equals(_value)) {
                return String.format("common.%s", s.getValue());
            }
        }
        return null;
    }
}
