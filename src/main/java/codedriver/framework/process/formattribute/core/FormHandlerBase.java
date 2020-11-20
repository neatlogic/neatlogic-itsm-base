package codedriver.framework.process.formattribute.core;

public abstract class FormHandlerBase implements IFormAttributeHandler {
    @Override
    public final String getType() {
        return "form";
    }
}
