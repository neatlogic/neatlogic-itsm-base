package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskNotifyTriggerType implements INotifyTriggerType {

    STARTPROCESS("startprocess", new I18n("enum.process.processtasknotifytriggertype.startprocess.a"), new I18n("enum.process.processtasknotifytriggertype.startprocess.b")),
    ABORTPROCESSTASK("abortprocesstask", new I18n("enum.process.processtasknotifytriggertype.abortprocesstask.a"), new I18n("enum.process.processtasknotifytriggertype.abortprocesstask.a")),
    RECOVERPROCESSTASK("recoverprocesstask", new I18n("enum.process.processtasknotifytriggertype.recoverprocesstask.a"), new I18n("enum.process.processtasknotifytriggertype.recoverprocesstask.b")),
    COMPLETEPROCESSTASK("completeprocesstask", new I18n("enum.process.processtasknotifytriggertype.completeprocesstask.a"), new I18n("enum.process.processtasknotifytriggertype.completeprocesstask.b")),
    WAITINGSCOREPROCESSTASK("waitingscoreprocesstask", new I18n("enum.process.processtasknotifytriggertype.waitingscoreprocesstask.a"), new I18n("enum.process.processtasknotifytriggertype.waitingscoreprocesstask.b")),
    SCOREPROCESSTASK("scoreprocesstask", new I18n("enum.process.processtasknotifytriggertype.scoreprocesstask.a"), new I18n("enum.process.processtasknotifytriggertype.scoreprocesstask.a")),
    REOPENPROCESSTASK("reopenprocesstask", new I18n("enum.process.processtasknotifytriggertype.reopenprocesstask.a"), new I18n("enum.process.processtasknotifytriggertype.reopenprocesstask.b")),
    MARKREPEATPROCESSTASK("markrepeatprocesstask", new I18n("enum.process.processtasknotifytriggertype.markrepeatprocesstask"), new I18n("enum.process.processtasknotifytriggertype.markrepeatprocesstask")),
    DELETEPROCESSTASK("deleteprocesstask", new I18n("enum.process.processtasknotifytriggertype.deleteprocesstask"), new I18n("enum.process.processtasknotifytriggertype.deleteprocesstask")),
    ;

    private String trigger;
    private I18n text;
    private I18n description;

    ProcessTaskNotifyTriggerType(String _trigger, I18n _text, I18n _description) {
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
        for (ProcessTaskNotifyTriggerType n : values()) {
            if (n.getTrigger().equals(trigger)) {
                return n.getText();
            }
        }
        return "";
    }
}
