package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessTaskSource implements IProcessTaskSource {

    PC("pc", "nfpc.processtasksource.text.pc"),
    MOBILE("mobile", "nfpc.processtasksource.text.mobile"),
    IMPORT("import", "nfpc.processtasksource.text.import"),
    SYSTEM("system", "nfpc.processtasksource.text.system"),
    PROCESSTASK_TRANSFER_REPORT("processtasktransferreport", "nfpc.processtasksource.text.processtask_transfer_report"),
    ;

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
        return $.t(text);
    }
}
