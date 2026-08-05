package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessTaskConditionType {

    WORKCENTER("workcenter", "nfpc.processtaskconditiontype.text.workcenter"),
    PROCESS("process", "nfpc.processtaskconditiontype.text.process");;

    private final String value;
    private final String text;

    ProcessTaskConditionType(String value, String text) {
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
