package neatlogic.framework.process.constvalue;

public enum ProcessTaskFinalStatus {

    SUCCEED("succeed", "成功"),
    FAILED("failed", "失败");

    private String value;
    private String text;

    ProcessTaskFinalStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
