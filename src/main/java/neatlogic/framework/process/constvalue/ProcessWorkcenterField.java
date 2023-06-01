package neatlogic.framework.process.constvalue;

import neatlogic.framework.dashboard.constvalue.IDashboardGroupField;
import neatlogic.framework.util.I18n;

public enum ProcessWorkcenterField implements IDashboardGroupField {
    ID("id", new I18n("工单ID")),
    SERIAL_NUMBER("serialnumber", new I18n("工单号")),
    TITLE("title", new I18n("标题")),
    CHANNELTYPE("channeltype", new I18n("服务类型")),
    CHANNEL("channel", new I18n("enum.process.processworkcenterfield.channel)")),
    PROCESS("process", new I18n("流程")),
    CONFIGHASH("confighash", new I18n("工单配置hash")),
    CATALOG("catalog", new I18n("服务目录")),
    CONTENT("content", new I18n("上报内容")),
    CONTENT_INCLUDE_HTML("contentincludehtml", new I18n("上报内容")),
    ENDTIME("endtime", new I18n("结束时间"), "endTime"),
    STARTTIME("starttime", new I18n("开始时间"), "startTime"),
    ACTIVETIME("activetime", new I18n("激活时间"), "activeTime"),
    EXPIRED_TIME("expiretime", new I18n("剩余时间")),
    OWNER("owner", new I18n("上报人")),
    REPORTER("reporter", new I18n("代报人")),
    PRIORITY("priority", new I18n("优先级")),
    STATUS("status", new I18n("工单状态")),
    STEP("step", new I18n("步骤")),
    STEP_STATUS("stepstatus", new I18n("步骤状态")),
    STEP_USER("stepuser", new I18n("步骤处理人")),
    STEP_NAME("stepname", new I18n("步骤名")),
    STEP_TEAM("stepteam", new I18n("步骤处理组")),
    CURRENT_STEP("currentstep", new I18n("当前步骤")),
    WOKRTIME("worktime", new I18n("时间窗口")),
    TRANSFER_FROM_USER("transferfromuser", new I18n("转交人")),
    ABOUTME("aboutme", new I18n("与我相关")),
    ACTION("action", new I18n("操作栏")),
    IS_SHOW("isshow", new I18n("是否显示，0隐藏，1显示")),
    FOCUS_USERS("focususers", new I18n("关注此工单的用户"));
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
