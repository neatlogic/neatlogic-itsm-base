package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ConditionConfigType {
  WORKCENTER("workcenter", "enum.process.conditionconfigtype.workcenter"),
  DEFAULT("default","common.default");

  private final String value;
  private final String name;

  private ConditionConfigType(String _value, String _name) {
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
    for (ConditionConfigType s : ConditionConfigType.values()) {
      if (s.getValue().equals(_value)) {
        return s.getValue();
      }
    }
    return null;
  }

  public static String getName(String _value) {
    for (ConditionConfigType s : ConditionConfigType.values()) {
      if (s.getValue().equals(_value)) {
        return s.getName();
      }
    }
    return "";
  }
}
