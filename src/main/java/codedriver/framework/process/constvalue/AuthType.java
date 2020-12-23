package codedriver.framework.process.constvalue;

public enum AuthType {
  BASIC("basic", "basic"), HMAC_SHA1("hmac-sha1", "HMAC_SHA1");

  private String value;
  private String name;

  private AuthType(String _value, String _name) {
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
    for (AuthType s : AuthType.values()) {
      if (s.getValue().equals(_value)) {
        return s.getValue();
      }
    }
    return null;
  }

  public static String getName(String _value) {
    for (AuthType s : AuthType.values()) {
      if (s.getValue().equals(_value)) {
        return s.getName();
      }
    }
    return "";
  }
}
