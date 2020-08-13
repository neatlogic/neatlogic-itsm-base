package codedriver.framework.process.constvalue;

public enum ProcessUserType {
	MAJOR("major", "处理人"), MINOR("minor", "协助处理人"),AGENT("agent","代办人"),OWNER("owner","上报人"),REPORTER("reporter","代报人"),WORKER("worker", "待处理人");

	private String status;
	private String text;

	private ProcessUserType(String _status, String _text) {
		this.status = _status;
		this.text = _text;
	}

	public String getValue() {
		return status;
	}

	public String getText() {
		return text;
	}

	public static String getValue(String _status) {
		for (ProcessUserType s : ProcessUserType.values()) {
			if (s.getValue().equals(_status)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getText(String _status) {
		for (ProcessUserType s : ProcessUserType.values()) {
			if (s.getValue().equals(_status)) {
				return s.getText();
			}
		}
		return "";
	}

}
