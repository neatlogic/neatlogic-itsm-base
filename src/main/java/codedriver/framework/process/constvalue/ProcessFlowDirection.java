package codedriver.framework.process.constvalue;

public enum ProcessFlowDirection {
	FORWARD("forward", "流转至："),
	BACKWARD("backward", "回退至：");
	
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
		return text;
	}

}
