package codedriver.framework.process.constvalue;

public enum ProcessWorkcenterField {
	ID("id", "工单号"),
	TITLE("title", "标题"),
	CHANNELTYPE("channeltype", "服务类型"),
	CHANNEL("channel", "服务"),
	CATALOG("catalog", "服务目录"),
	CONTENT("content", "上报内容"),
	ENDTIME("endtime", "结束时间"),
	STARTTIME("starttime", "开始时间"),
	EXPIRED_TIME("expiredtime", "剩余时间"),
	OWNER("owner", "上报人"),
	REPORTER("reporter", "代报人"),
	PRIORITY("priority", "优先级"),
	STATUS("status", "工单状态"),
	STEP("step","步骤"),
	CURRENT_STEP("currentstep","当前步骤"),
	CURRENT_STEP_USER("currentstepuser","当前步骤处理人","currentstep.usertypelist.userlist"),
	CURRENT_STEP_STATUS("stepstatus","当前步骤状态","currentstep.status"),
	WOKRTIME("worktime","时间窗口"),
	TRANSFER_FROM_USER("transferfromuser","转交人","transferfromuser"),
	USER_WILL_DO("userwilldo","用户待处理的","userwilldo"),
	USER_DO("userdo","用户参与的","userdo"),
	USER_DONE("userdone","用户已处理的","userdone"),
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
