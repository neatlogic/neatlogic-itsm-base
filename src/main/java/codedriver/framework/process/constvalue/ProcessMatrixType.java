package codedriver.framework.process.constvalue;

public enum ProcessMatrixType {
    CUSTOM("custom", "自定义数据源"), OUTSIDE("outside", "外部数据源");

    private String value;
    private String name;

    private ProcessMatrixType(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getValue(String _value) {
        for (ProcessMatrixType s : ProcessMatrixType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (ProcessMatrixType s : ProcessMatrixType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }
}
