package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18n;

public enum ProcessTaskSource implements IProcessTaskSource {

    PC("pc", new I18n("enum.process.processtasksource.pc")),
    MOBILE("mobile", new I18n("enum.process.processtasksource.mobile")),
    IMPORT("import", new I18n("enum.process.processtasksource.import")),
    SYSTEM("system", new I18n("enum.process.processtasksource.system"));

    private String value;
    private I18n text;

    ProcessTaskSource(String value, I18n text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text.toString();
    }
}
