package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskStepTaskNotifyTriggerType implements INotifyTriggerType {
    CREATETASK("createtask", new I18n("enum.process.processtasksteptasknotifytriggertype.createtask.a"), new I18n("enum.process.processtasksteptasknotifytriggertype.createtask.b")),
    EDITTASK("edittask", new I18n("enum.process.processtasksteptasknotifytriggertype.edittask.a"), new I18n("enum.process.processtasksteptasknotifytriggertype.edittask.b")),
    DELETETASK("deletetask", new I18n("enum.process.processtasksteptasknotifytriggertype.deletetask.a"), new I18n("enum.process.processtasksteptasknotifytriggertype.deletetask.b")),
    COMPLETETASK("completetask", new I18n("enum.process.processtasksteptasknotifytriggertype.completetask.a"), new I18n("enum.process.processtasksteptasknotifytriggertype.completetask.b")),
    COMPLETEALLTASK("completealltask", new I18n("enum.process.processtasksteptasknotifytriggertype.completealltask.a"), new I18n("enum.process.processtasksteptasknotifytriggertype.completealltask.b"));

    private String trigger;
    private I18n text;
    private I18n description;

    ProcessTaskStepTaskNotifyTriggerType(String _trigger, I18n _text, I18n _description) {
        this.trigger = _trigger;
        this.text = _text;
        this.description = _description;
    }

    @Override
    public String getTrigger() {
        return trigger;
    }

    @Override
    public String getText() {
        return text.toString();
    }

    @Override
    public String getDescription() {
        return description.toString();
    }

    public static String getText(String trigger) {
        for (ProcessTaskStepTaskNotifyTriggerType n : values()) {
            if (n.getTrigger().equals(trigger)) {
                return n.getText();
            }
        }
        return "";
    }
}
