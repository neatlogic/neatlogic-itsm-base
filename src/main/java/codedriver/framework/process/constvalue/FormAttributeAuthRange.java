package codedriver.framework.process.constvalue;

public enum FormAttributeAuthRange {
	ALL("all", "所有")
	;
	private String value;
	private String text;
	public String getValue() {
		return value;
	}
	public String getText() {
		return text;
	}
	private FormAttributeAuthRange(String value, String text) {
		this.value = value;
		this.text = text;
	}
	
}
