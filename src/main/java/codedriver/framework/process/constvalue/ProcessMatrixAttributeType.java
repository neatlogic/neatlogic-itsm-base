package codedriver.framework.process.constvalue;

public enum ProcessMatrixAttributeType {
	INPUT("input","文本框"),
	SELECT("select","下拉框"),
	DATE("date","日期"),
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
