package codedriver.framework.process.constvalue;

public enum FormAttributeAuthType {
	COMPONENT("component", "组件"),
	ROW("row", "行")
	;
	private String value;
	private String text;
	public String getValue() {
		return value;
	}
	public String getText() {
		return text;
	}
	private FormAttributeAuthType(String value, String text) {
		this.value = value;
		this.text = text;
	}
	
}
