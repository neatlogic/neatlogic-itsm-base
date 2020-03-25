package codedriver.framework.process.constvalue;

public enum ProcessTaskAuditDetailType {
	TITLE("title", "标题", "title", "oldTitle"),
	PRIORITY("priority", "优先级", "priorityUuid", "oldPriorityUuid"),
	CONTENT("content", "内容", "contentHash", "oldContentHash"),
	//FORM("form", "表单"),
	WORKER("worker", "处理人", "workerList", "oldWorkerList"),
	DATE("date", "期望时间", "targetTime", "oldTargetTime"),
	FILE("file", "上传文件", "fileUuidList", "oldFileUuidList");
	
	private String status;
	private String text;
	private String paramName;
	private String oldDataParamName;
	
	private ProcessTaskAuditDetailType(String _status, String _text, String _paramName, String _oldDataParamName) {
		this.status = _status;
		this.text = _text;
		this.paramName = _paramName;
		this.oldDataParamName = _oldDataParamName;
	}

	public String getValue() {
		return status;
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

	public static String getValue(String _status) {
		for (ProcessTaskAuditDetailType s : ProcessTaskAuditDetailType.values()) {
			if (s.getValue().equals(_status)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getText(String _status) {
		for (ProcessTaskAuditDetailType s : ProcessTaskAuditDetailType.values()) {
			if (s.getValue().equals(_status)) {
				return s.getText();
			}
		}
		return "";
	}
}
