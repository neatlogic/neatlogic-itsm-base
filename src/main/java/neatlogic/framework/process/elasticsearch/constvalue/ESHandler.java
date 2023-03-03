package neatlogic.framework.process.elasticsearch.constvalue;

public enum ESHandler {
	PROCESSTASK("processtask","工单");

	private String value;
	private String text;

	private ESHandler(String _value, String _text) {
		this.value = _value;
		this.text = _text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static String getValue(String _status) {
		for (ESHandler s : ESHandler.values()) {
			if (s.getValue().equals(_status)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getText(String _status) {
		for (ESHandler s : ESHandler.values()) {
			if (s.getValue().equals(_status)) {
				return s.getText();
			}
		}
		return "";
	}

}
