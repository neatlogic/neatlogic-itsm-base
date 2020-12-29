package codedriver.framework.process.operationauth.core;

public enum OperationAuthHandlerType implements IOperationAuthHandlerType {
	TASK("task", "工单"), STEP("step", "步骤"), OMNIPOTENT("omnipotent", "普通组件"), AUTOMATIC("automatic", "自动组件");

    private OperationAuthHandlerType(String value, String text) {
        this.value = value;
        this.text = text;
    }
    private String value;
    private String text;
    @Override
    public String getText() {
        return text;
    }
    @Override
    public String getValue() {
        return value;
    }

}
