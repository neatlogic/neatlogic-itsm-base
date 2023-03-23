package neatlogic.framework.process.operationauth.core;

import neatlogic.framework.util.I18nUtils;

public enum OperationAuthHandlerType implements IOperationAuthHandlerType {
	TASK("task", "enum.process.operationauthhandlertype.task"),
    STEP("step", "enum.process.operationauthhandlertype.step"),
    OMNIPOTENT("omnipotent", "enum.process.operationauthhandlertype.omnipotent"),
    AUTOMATIC("automatic", "enum.process.operationauthhandlertype.automatic"),
    TIMER("timer", "enum.process.operationauthhandlertype.timer");

    private OperationAuthHandlerType(String value, String text) {
        this.value = value;
        this.text = text;
    }
    private String value;
    private String text;
    @Override
    public String getText() {
        return I18nUtils.getMessage(text);
    }
    @Override
    public String getValue() {
        return value;
    }

}
