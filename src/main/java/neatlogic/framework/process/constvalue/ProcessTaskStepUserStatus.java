package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18n;

public enum ProcessTaskStepUserStatus {
    DOING("doing", new I18n("处理中")), DONE("done", new I18n("处理完毕"));

    private String status;
    private I18n text;

    private ProcessTaskStepUserStatus(String _status, I18n _text) {
        this.status = _status;
        this.text = _text;
    }

    public String getValue() {
        return status;
    }

    public String getText() {
        return text.toString();
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
