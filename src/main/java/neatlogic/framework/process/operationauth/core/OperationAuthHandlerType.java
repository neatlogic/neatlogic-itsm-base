package neatlogic.framework.process.operationauth.core;

import neatlogic.framework.util.$;

public enum OperationAuthHandlerType implements IOperationAuthHandlerType {
	TASK("task", "工单"),
    STEP("step", "步骤"),
    OMNIPOTENT("omnipotent", "普通组件"),
    AUTOMATIC("automatic", "自动组件"),
    TIMER("timer", "定时组件");

    private OperationAuthHandlerType(String value, String text) {
        this.value = value;
        this.text = text;
    }
    private String value;
    private String text;
    @Override
    public String getText() {
        return $.t(text);
    }
    @Override
    public String getValue() {
        return value;
    }

}
