package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessTaskSourceType {
    ITSM("itsm", "term.itsm.groupname");

    private final String value;
    private final String text;

    private ProcessTaskSourceType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return $.t(text);
    }
}
