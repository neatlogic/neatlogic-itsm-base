package codedriver.framework.process.constvalue;

public enum ProcessMatrixAttributeType {
	FORMINPUT("forminput","文本框"),
	FORMSELECT("formselect","下拉框"),
	FORMDATE("formdate","日期"),
	USER("user","用户"),
	TEAM("team","用户组"),
	ROLE("role","角色");
	
	private String value;
	private String text;
	
	private ProcessMatrixAttributeType(String value, String text) {
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
