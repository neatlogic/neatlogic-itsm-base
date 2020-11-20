package codedriver.framework.process.formattribute.core;

public abstract class ControlHandlerBase implements IFormAttributeHandler {
    @Override
    public final String getType() {
        return "control";
    }
}
