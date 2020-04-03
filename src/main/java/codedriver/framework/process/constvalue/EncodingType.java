package codedriver.framework.process.constvalue;

public enum EncodingType {
    UTF_8("utf-8", "UTF-8"), GBK("gbk", "GBK");

    private String value;
    private String name;

    private EncodingType(String _value, String _name) {
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
        for (EncodingType s : EncodingType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (EncodingType s : EncodingType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }
}
