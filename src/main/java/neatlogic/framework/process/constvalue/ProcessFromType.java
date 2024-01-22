package neatlogic.framework.process.constvalue;

import neatlogic.framework.dependency.core.IFromType;

public enum ProcessFromType implements IFromType {

    EOATEMPLATE("eoaTemplate", "EOA模板"),
    ;

    private String value;
    private String text;

    ProcessFromType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }
}
