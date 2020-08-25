package codedriver.framework.process.constvalue;

public enum ProcessWorkcenterField {
	ID("id", "工单号"),
	TITLE("title", "标题"),
	CHANNELTYPE("channeltype", "服务类型"),
	CHANNEL("channel", "服务"),
	CATALOG("catalog", "服务目录"),
	CONTENT("content", "上报内容","contentincludehtml"),
	ENDTIME("endtime", "结束时间"),
	STARTTIME("starttime", "开始时间"),
	EXPIRED_TIME("expiretime", "剩余时间"),
	OWNER("owner", "上报人"),
	REPORTER("reporter", "代报人"),
	PRIORITY("priority", "优先级"),
	STATUS("status", "工单状态"),
	STEP("step","步骤"),
	STEP_STATUS("stepstatus","步骤状态","step.status"),
	STEP_USER("stepuser","步骤处理人","step.usertypelist.userlist"),
	STEP_TEAM("stepteam","步骤处理组"),
	CURRENT_STEP("currentstep","当前步骤"),
	WOKRTIME("worktime","时间窗口"),
	TRANSFER_FROM_USER("transferfromuser","转交人","transferfromuser"),
	ABOUTME("aboutme","与我相关"),
	ACTION("action", "操作栏");
	private String value;
	private String name;
	private String conditionValue;

	private ProcessWorkcenterField(String _value, String _name,String _conditionValue) {
		this.value = _value;
		this.conditionValue = _conditionValue;
		this.name = _name;
	}
	private ProcessWorkcenterField(String _value, String _name) {
		this.value = _value;
		this.conditionValue = _value;
		this.name = _name;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
	public String getConditionValue() {
		return conditionValue;
	}
	
	public static String getValue(String _value) {
		for (ProcessWorkcenterField s : ProcessWorkcenterField.values()) {
			if (s.getValue().equals(_value)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getName(String _value) {
		for (ProcessWorkcenterField s : ProcessWorkcenterField.values()) {
			if (s.getValue().equals(_value)) {
				return s.getName();
			}
		}
		return "";
	}

	public static String getConditionValue(String _value) {
		for (ProcessWorkcenterField s : ProcessWorkcenterField.values()) {
			if (s.getValue().equals(_value)) {
				return String.format("common.%s", s.getConditionValue());
			}
		}
		return null;
	}
}
