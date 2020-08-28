package codedriver.framework.process.operationauth.core;

public enum OperationAuthHandlerType implements IOperationAuthHandlerType {
	TASK("工单"), STEP("步骤"), OMNIPOTENT("普通组件"), AUTOMATIC("自动组件");

    private OperationAuthHandlerType(String text) {
        this.text = text;
    }
    private String text;
    @Override
    public String getText() {
        return text;
    }

}
