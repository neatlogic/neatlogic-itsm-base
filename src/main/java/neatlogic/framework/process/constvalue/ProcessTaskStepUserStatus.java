package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessTaskStepUserStatus {
    DOING("doing", "nfpc.processtaskstepuserstatus.text.doing"),
    DONE("done", "nfpc.processtaskstepuserstatus.text.done"),
    TRANSFERRED("transferred", "nfpc.processtaskstepuserstatus.text.transferred"),
    SOMEONE_TRANSFERRED("someonetransferred", "nfpc.processtaskstepuserstatus.text.someone_transferred")
    ;

    private String status;
    private String text;

    ProcessTaskStepUserStatus(String _status, String _text) {
        this.status = _status;
        this.text = _text;
    }

    public String getValue() {
        return status;
    }

    public String getText() {
        return $.t(text);
    }

    public static String getValue(String _status) {
        for (ProcessTaskStepUserStatus s : ProcessTaskStepUserStatus.values()) {
            if (s.getValue().equals(_status)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _status) {
        for (ProcessTaskStepUserStatus s : ProcessTaskStepUserStatus.values()) {
            if (s.getValue().equals(_status)) {
                return s.getText();
            }
        }
        return "";
    }

}
