package codedriver.framework.process.constvalue;

public enum RestfulType {
    POST("POST", "POST"), GET("GET", "GET"), PUT("PUT", "PUT"), DELETE("DELETE", "DELETE");

    private String value;
    private String name;

    private RestfulType(String _value, String _name) {
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
        for (RestfulType s : RestfulType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (RestfulType s : RestfulType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }
}
