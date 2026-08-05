package neatlogic.framework.process.constvalue;

import neatlogic.framework.dependency.core.IFromType;

import neatlogic.framework.util.$;
public enum ProcessFromType implements IFromType {

    EOATEMPLATE("eoaTemplate", "nfpc.processfromtype.text.eoatemplate"),
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
        return $.t(text);
    }
}
