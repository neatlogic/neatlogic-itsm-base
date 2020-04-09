package codedriver.framework.process.constvalue;

public enum ProcessTaskAuditDetailType {
	TITLE("title", "标题", "title", "oldTitle"),
	PRIORITY("priority", "优先级", "priorityUuid", "oldPriorityUuid"),
	CONTENT("content", "内容", "contentHash", "oldContentHash"),
	FORM("form", "表单", "processTaskFormAttributeDataList", "oldProcessTaskFormAttributeDataList"),
	WORKER("worker", "处理人", "workerList", "oldWorkerList"),
	DATE("date", "期望时间", "targetTime", "oldTargetTime"),
	FILE("file", "上传文件", "fileUuidList", "oldFileUuidList");
	
	private String value;
	private String text;
	private String paramName;
	private String oldDataParamName;
	
	private ProcessTaskAuditDetailType(String _value, String _text, String _paramName, String _oldDataParamName) {
		this.value = _value;
		this.text = _text;
		this.paramName = _paramName;
		this.oldDataParamName = _oldDataParamName;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public String getParamName() {
		return paramName;
	}

	public String getOldDataParamName() {
		return oldDataParamName;
	}

	public static String getValue(String _value) {
		for (ProcessTaskAuditDetailType s : ProcessTaskAuditDetailType.values()) {
			if (s.getValue().equals(_value)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getText(String _value) {
		for (ProcessTaskAuditDetailType s : ProcessTaskAuditDetailType.values()) {
			if (s.getValue().equals(_value)) {
				return s.getText();
			}
		}
		return "";
	}
	
	public static String getParamName(String _value) {
		for (ProcessTaskAuditDetailType s : ProcessTaskAuditDetailType.values()) {
			if (s.getValue().equals(_value)) {
				return s.getParamName();
			}
		}
		return "";
	}
	
	public static String getOldDataParamName(String _value) {
		for (ProcessTaskAuditDetailType s : ProcessTaskAuditDetailType.values()) {
			if (s.getValue().equals(_value)) {
				return s.getOldDataParamName();
			}
		}
		return "";
	}
}
