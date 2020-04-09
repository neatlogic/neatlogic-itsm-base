package codedriver.framework.process.constvalue;

public enum ProcessField {
	ID("id", "工单号"),
	TITLE("title", "标题"),
	CHANNELTYPE("channeltype", "服务类型"),
	CONTENT("content", "上报内容"),
	ENDTIME("endtime", "结束时间"),
	STARTTIME("starttime", "开始时间"),
	EXPIRED_TIME("expiredtime", "剩余时间"),
	OWNER("owner", "上报人"),
	REPORTER("reporter", "代报人"),
	PRIORITY("priority", "优先级"),
	STATUS("status", "工单状态");
	private String value;
	private String name;
	private String conditionValue;

	private ProcessField(String _value, String _name,String _conditionValue) {
		this.value = _value;
		this.conditionValue = _conditionValue;
		this.name = _name;
	}
	private ProcessField(String _value, String _name) {
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
		for (ProcessField s : ProcessField.values()) {
			if (s.getValue().equals(_value)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getName(String _value) {
		for (ProcessField s : ProcessField.values()) {
			if (s.getValue().equals(_value)) {
				return s.getName();
			}
		}
		return "";
	}

}