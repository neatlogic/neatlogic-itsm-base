package codedriver.framework.process.constvalue;

public enum ProcessTaskChannel implements IProcessTaskChannel {

    PC("pc", "PC端"),
    MOBILE("mobile", "移动端");

    private String value;
    private String text;

    ProcessTaskChannel(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }
}
