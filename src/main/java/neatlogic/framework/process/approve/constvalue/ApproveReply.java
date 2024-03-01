package neatlogic.framework.process.approve.constvalue;

public enum ApproveReply {
    ACCEPT("accept", "接受"), NEUTRAL("neutral", "中立"), DENY("deny", "拒绝");
    private String value;
    private String text;

    ApproveReply(String value, String text) {
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
