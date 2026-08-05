package neatlogic.framework.process.constvalue;

import neatlogic.framework.dashboard.constvalue.IDashboardGroupField;
import neatlogic.framework.util.I18n;

public enum ProcessWorkcenterField implements IDashboardGroupField {
    ID("id", new I18n("nfpc.processworkcenterfield.text.id")),
    SERIAL_NUMBER("serialnumber", new I18n("nfpc.processworkcenterfield.text.serial_number")),
    TITLE("title", new I18n("nfpc.processworkcenterfield.text.title")),
    CHANNELTYPE("channeltype", new I18n("nfpc.processworkcenterfield.text.channeltype")),
    CHANNEL("channel", new I18n("enum.process.processworkcenterfield.channel)")),
    PROCESS("process", new I18n("nfpc.processworkcenterfield.text.process")),
    CONFIGHASH("confighash", new I18n("nfpc.processworkcenterfield.text.confighash")),
    CATALOG("catalog", new I18n("nfpc.processworkcenterfield.text.catalog")),
    CONTENT("content", new I18n("nfpc.processworkcenterfield.text.content")),
    CONTENT_INCLUDE_HTML("contentincludehtml", new I18n("nfpc.processworkcenterfield.text.content_include_html")),
    ENDTIME("endtime", new I18n("nfpc.processworkcenterfield.text.endtime"), "endTime"),
    STARTTIME("starttime", new I18n("nfpc.processworkcenterfield.text.starttime"), "startTime"),
    ACTIVETIME("activetime", new I18n("nfpc.processworkcenterfield.text.activetime"), "activeTime"),
    EXPIRED_TIME("expiretime", new I18n("nfpc.processworkcenterfield.text.expired_time")),
    OWNER("owner", new I18n("nfpc.processworkcenterfield.text.owner")),
    REPORTER("reporter", new I18n("nfpc.processworkcenterfield.text.reporter")),
    PRIORITY("priority", new I18n("nfpc.processworkcenterfield.text.priority")),
    STATUS("status", new I18n("nfpc.processworkcenterfield.text.status")),
    STEP("step", new I18n("nfpc.processworkcenterfield.text.step")),
    STEP_STATUS("stepstatus", new I18n("nfpc.processworkcenterfield.text.step_status")),
    STEP_USER("stepuser", new I18n("nfpc.processworkcenterfield.text.step_user")),
    STEP_ONLY_USER("steponlyuser", new I18n("nfpc.processworkcenterfield.text.step_only_user")),
    STEP_ONLY_TEAM("steponlyteam", new I18n("nfpc.processworkcenterfield.text.step_only_team")),
    STEP_NAME("stepname", new I18n("nfpc.processworkcenterfield.text.step_name")),
    STEP_TEAM("stepteam", new I18n("nfpc.processworkcenterfield.text.step_team")),
    CURRENT_STEP("currentstep", new I18n("nfpc.processworkcenterfield.text.current_step")),
    WOKRTIME("worktime", new I18n("nfpc.processworkcenterfield.text.wokrtime")),
    TRANSFER_FROM_USER("transferfromuser", new I18n("nfpc.processworkcenterfield.text.transfer_from_user")),
    ABOUTME("aboutme", new I18n("nfpc.processworkcenterfield.text.aboutme")),
    ACTION("action", new I18n("nfpc.processworkcenterfield.text.action")),
    IS_SHOW("isshow", new I18n("nfpc.processworkcenterfield.text.is_show")),
    FOCUS_USERS("focususers", new I18n("nfpc.processworkcenterfield.text.focus_users")),
    REGION("region", new I18n("common.region")),
    INVOKE("invoke", new I18n("common.source")),
    TAG("tag", new I18n("common.tag")),
    ;
    private final String value;
    private final String valuePro;
    private final I18n text;

    ProcessWorkcenterField(String _value, I18n _text) {
        this.value = _value;
        this.text = _text;
        this.valuePro = _value;
    }

    ProcessWorkcenterField(String _value, I18n _text, String _vaulePro) {
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
