package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessFlowDirection {
	FORWARD("forward", "nfpc.processflowdirection.text.forward"),
	BACKWARD("backward", "nfpc.processflowdirection.text.backward");
	
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
		return $.t(text);
	}

}
