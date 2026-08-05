package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessFieldType {
    COMMON("common", "nfpc.processfieldtype.text.common"),
    FORM("form", "nfpc.processfieldtype.text.form"),
    CUSTOM("custom", "nfpc.processfieldtype.text.custom"),
    CONSTANT("constant", "nfpc.processfieldtype.text.constant");
    private final String value;
    private final String name;

    ProcessFieldType(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return $.t(name);
    }

    public static String getValue(String _value) {
        for (ProcessFieldType s : ProcessFieldType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (ProcessFieldType s : ProcessFieldType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }

}
