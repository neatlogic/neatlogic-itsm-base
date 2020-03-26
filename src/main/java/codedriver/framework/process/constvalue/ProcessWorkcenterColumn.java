package codedriver.framework.process.constvalue;

public enum ProcessWorkcenterColumn {
	ID("id", "工单号"),
	TITLE("title", "标题"),
	CHANNELTYPE("channeltype", "服务类型","channelType"),
	CHANNEL("channel", "服务"),
	CATALOG("catalog", "服务目录"),
	CONTENT("content", "上报内容"),
	ENDTIME("endtime", "结束时间","endTime"),
	STARTTIME("starttime", "开始时间","startTime"),
	EXPIRED_TIME("expiredtime", "剩余时间","expiredTime"),
	OWNER("owner", "上报人"),
	REPORTER("reporter", "代报人"),
	PRIORITY("priority", "优先级"),
	STATUS("status", "工单状态"),
	CURRENT_STEP("currentstep","当前步骤","currentStep"),
	CURRENT_STEP_USER("currentstepuser","当前步骤用户","currentStepUser"),
	CURRENT_STEP_STATUS("currentstepstatus","当前步骤状态","currentStepStatus"),
	WOKRTIME("worktime","时间窗口"),
	STEPUSER("stepuser", "当前处理人","currentStepUser"),
	TRANSFER_FROM_USER("transferfromuser","转交人","transferFromUser"),
	ACTION("action", "操作栏");
	private String value;
	private String name;
	private String valueEs;

	private ProcessWorkcenterColumn(String _value, String _name,String _valueEs) {
		this.value = _value;
		this.valueEs = _valueEs;
		this.name = _name;
	}
	private ProcessWorkcenterColumn(String _value, String _name) {
		this.value = _value;
		this.valueEs = _value;
		this.name = _name;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
	public String getValueEs() {
		return valueEs;
	}
	
	public static String getValue(String _value) {
		for (ProcessWorkcenterColumn s : ProcessWorkcenterColumn.values()) {
			if (s.getValue().equals(_value)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getName(String _value) {
		for (ProcessWorkcenterColumn s : ProcessWorkcenterColumn.values()) {
			if (s.getValue().equals(_value)) {
				return s.getName();
			}
		}
		return "";
	}

}
