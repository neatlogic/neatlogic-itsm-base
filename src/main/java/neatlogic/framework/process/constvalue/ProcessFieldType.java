package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ProcessFieldType {
    COMMON("common", "enum.process.processfieldtype.common"),
    FORM("form", "enum.process.processfieldtype.form"),
    CUSTOM("custom", "enum.process.processfieldtype.custom"),
    CONSTANT("constant", "enum.process.processfieldtype.constant");
    private String value;
    private String name;

    private ProcessFieldType(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return I18nUtils.getMessage(name);
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
