package codedriver.framework.process.constvalue;

import codedriver.framework.dashboard.constvalue.IDashboardGroupField;

public enum ProcessWorkcenterField  implements IDashboardGroupField {
	ID("id", "工单id"),
	SERIAL_NUMBER("serialnumber","工单号"),
	TITLE("title", "标题"),
	CHANNELTYPE("channeltype", "服务类型"),
	CHANNEL("channel", "服务"),
	PROCESS("process","流程"),
	CONFIGHASH("confighash","工单配置hash"),
	CATALOG("catalog", "服务目录"),
	CONTENT("content", "上报内容"),
	CONTENT_INCLUDE_HTML("contentincludehtml", "上报内容"),
	ENDTIME("endtime", "结束时间","endTime"),
	STARTTIME("starttime", "开始时间","startTime"),
	ACTIVETIME("activetime", "激活时间","activeTime"),
	EXPIRED_TIME("expiretime", "剩余时间"),
	OWNER("owner", "上报人"),
	REPORTER("reporter", "代报人"),
	PRIORITY("priority", "优先级"),
	STATUS("status", "工单状态"),
	STEP("step","步骤"),
	STEP_STATUS("stepstatus","步骤状态"),
	STEP_USER("stepuser","步骤处理人"),
	STEP_TEAM("stepteam","步骤处理组"),
	CURRENT_STEP("currentstep","当前步骤"),
	WOKRTIME("worktime","时间窗口"),
	TRANSFER_FROM_USER("transferfromuser","转交人"),
	ABOUTME("aboutme","与我相关"),
	ACTION("action", "操作栏"),
	IS_SHOW("isshow","是否显示，0隐藏，1显示"),
	FOCUS_USERS("focususers", "关注此工单的用户");
	private final String value;
	private final String valuePro;
	private final String text;


	private ProcessWorkcenterField(String _value, String _text) {
		this.value = _value;
		this.text = _text;
		this.valuePro = _value;
	}

	private ProcessWorkcenterField(String _value, String _text,String _vaulePro) {
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
		return text;
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
