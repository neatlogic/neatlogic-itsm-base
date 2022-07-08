package codedriver.framework.process.constvalue;

public enum ProcessTaskSource implements IProcessTaskChannel {

    PC("pc", "PC端"),
    MOBILE("mobile", "移动端"),
    SYSTEM("system", "系统");

    private String value;
    private String text;

    ProcessTaskSource(String value, String text) {
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
