package neatlogic.framework.process.elasticsearch.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ESHandler {
	PROCESSTASK("processtask","enum.process.eshandler.processtask");

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
		return I18nUtils.getMessage(text);
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
