package codedriver.framework.process.constvalue;

public enum ProcessTaskStepDataType implements IProcessTaskStepDataType{
	STEPDRAFTSAVE("stepdraftsave", "步骤草稿暂存"),
	AUTOMATIC("automatic", "auto节点数据")
	;
	private String value;
	private String text;
	
	private ProcessTaskStepDataType(String value, String text) {
		this.value = value;
		this.text = text;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String getText() {
		return this.text;
	}

}
