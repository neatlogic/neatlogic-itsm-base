package codedriver.framework.process.constvalue;

public enum ProcessTaskNumberGenerateRule {
    AUTO_INCREMENT("autoincrement", "自增序列"), DATETIME_AUTO_INCREMENT("datetimeautoincrement", "日期+自增序列");
    private String value;
    private String text;
    private ProcessTaskNumberGenerateRule(String value, String text) {
        this.value = value;
        this.text = text;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

}
