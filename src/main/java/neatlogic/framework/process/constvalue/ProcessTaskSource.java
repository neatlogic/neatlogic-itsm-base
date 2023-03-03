package neatlogic.framework.process.constvalue;

public enum ProcessTaskSource implements IProcessTaskSource {

    PC("pc", "PC端"),
    MOBILE("mobile", "移动端"),
    IMPORT("import", "导入"),
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
