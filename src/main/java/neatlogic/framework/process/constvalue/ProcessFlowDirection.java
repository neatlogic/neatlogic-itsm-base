package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ProcessFlowDirection {
	FORWARD("forward", "enum.process.processflowdirection.forward"),
	BACKWARD("backward", "enum.process.processflowdirection.backward");
	
	private String value;
	private String text;
	
	private ProcessFlowDirection(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return I18nUtils.getMessage(text);
	}

}
