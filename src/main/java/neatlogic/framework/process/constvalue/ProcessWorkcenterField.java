package neatlogic.framework.process.constvalue;

import neatlogic.framework.dashboard.constvalue.IDashboardGroupField;
import neatlogic.framework.util.I18n;

public enum ProcessWorkcenterField implements IDashboardGroupField {
    ID("id", new I18n("enum.process.processworkcenterfield.id")),
    SERIAL_NUMBER("serialnumber", new I18n("enum.process.processworkcenterfield.serial_number")),
    TITLE("title", new I18n("enum.process.processworkcenterfield.title")),
    CHANNELTYPE("channeltype", new I18n("enum.process.processworkcenterfield.channeltype")),
    CHANNEL("channel", new I18n("enum.process.processworkcenterfield.channel)")),
    PROCESS("process", new I18n("enum.process.processworkcenterfield.process")),
    CONFIGHASH("confighash", new I18n("enum.process.processworkcenterfield.confighash")),
    CATALOG("catalog", new I18n("enum.process.processworkcenterfield.catalog")),
    CONTENT("content", new I18n("enum.process.processworkcenterfield.content.a")),
    CONTENT_INCLUDE_HTML("contentincludehtml", new I18n("enum.process.processworkcenterfield.content_include_html")),
    ENDTIME("endtime", new I18n("enum.process.processworkcenterfield.endtime"), "endTime"),
    STARTTIME("starttime", new I18n("enum.process.processworkcenterfield.starttime"), "startTime"),
    ACTIVETIME("activetime", new I18n("enum.process.processworkcenterfield.activetime"), "activeTime"),
    EXPIRED_TIME("expiretime", new I18n("enum.process.processworkcenterfield.expired_time")),
    OWNER("owner", new I18n("enum.process.processworkcenterfield.owner")),
    REPORTER("reporter", new I18n("enum.process.processworkcenterfield.reporter")),
    PRIORITY("priority", new I18n("enum.process.processworkcenterfield.priority")),
    STATUS("status", new I18n("enum.process.processworkcenterfield.status")),
    STEP("step", new I18n("enum.process.processworkcenterfield.step.a")),
    STEP_STATUS("stepstatus", new I18n("enum.process.processworkcenterfield.step_status")),
    STEP_USER("stepuser", new I18n("enum.process.processworkcenterfield.step_user")),
    STEP_NAME("stepname", new I18n("enum.process.processworkcenterfield.step_name")),
    STEP_TEAM("stepteam", new I18n("enum.process.processworkcenterfield.step_team")),
    CURRENT_STEP("currentstep", new I18n("enum.process.processworkcenterfield.current_step")),
    WOKRTIME("worktime", new I18n("enum.process.processworkcenterfield.wokrtime")),
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
