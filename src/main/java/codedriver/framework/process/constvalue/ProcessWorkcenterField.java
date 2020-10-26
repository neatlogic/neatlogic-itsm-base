package codedriver.framework.process.constvalue;

public enum ProcessWorkcenterField {
	ID("id", "工单号"),
	TITLE("title", "标题"),
	CHANNELTYPE("channeltype", "服务类型"),
	CHANNEL("channel", "服务"),
	PROCESS("process","流程"),
	CONFIGHASH("confighash","工单配置hash"),
	CATALOG("catalog", "服务目录"),
	CONTENT("content", "上报内容"),
	ENDTIME("endtime", "结束时间"),
	STARTTIME("starttime", "开始时间"),
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
	private String value;
	private String name;


	private ProcessWorkcenterField(String _value, String _name) {
		this.value = _value;
		this.name = _name;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
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
                return String.format("common.%s", s.getValue());
            }
        }
        return null;
    }
}
