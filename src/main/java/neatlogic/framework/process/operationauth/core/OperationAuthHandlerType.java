package neatlogic.framework.process.operationauth.core;

import neatlogic.framework.util.$;

public enum OperationAuthHandlerType implements IOperationAuthHandlerType {
	TASK("task", "nfpoc.operationauthhandlertype.text.task"),
    STEP("step", "nfpoc.operationauthhandlertype.text.step"),
    ;

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
