package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18n;

public enum WorkerPolicy {
    ASSIGN("assign", new I18n("自定义")),
    //MANUAL("manual", "处理人抢单"),
    AUTOMATIC("automatic", new I18n("分派器")),
    //FROMER("fromer", "前置步骤指定"),
    COPY("copy", new I18n("复制前置步骤处理人")),
    //ATTRIBUTE("attribute", "属性值"),
    FORM("form", new I18n("表单值")),
    PRESTEPASSIGN("prestepassign", new I18n("由前置步骤处理人指定"));
    private String policy;
    private I18n text;

    private WorkerPolicy(String _policy, I18n _text) {
        this.policy = _policy;
        this.text = _text;
    }

    public String getValue() {
        return policy;
    }

    public String getText() {
        return text.toString();
    }

    public static String getValue(String _policy) {
        for (WorkerPolicy s : WorkerPolicy.values()) {
            if (s.getValue().equals(_policy)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _policy) {
        for (WorkerPolicy s : WorkerPolicy.values()) {
            if (s.getValue().equals(_policy)) {
                return s.getText();
            }
        }
        return "";
    }

    public static WorkerPolicy getWorkerPolicy(String _policy) {
        for (WorkerPolicy s : WorkerPolicy.values()) {
            if (s.getValue().equals(_policy)) {
                return s;
            }
        }
        return null;
    }
}
