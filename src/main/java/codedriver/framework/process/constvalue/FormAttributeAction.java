package codedriver.framework.process.constvalue;

public enum FormAttributeAction {
	HIDE("hide", "隐藏"),
	READ("read", "只读");
	private String value;
	private String text;
	public String getValue() {
		return value;
	}
	public String getText() {
		return text;
	}
	private FormAttributeAction(String value, String text) {
		this.value = value;
		this.text = text;
	}
	
}
