package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;

public enum ProcessTaskNotifyTriggerType implements INotifyTriggerType {

    STARTPROCESS("startprocess", "enum.process.processtasknotifytriggertype.startprocess", "enum.process.processtasknotifytriggertype.startprocess.1"),
    URGE("urge", "enum.process.processtasknotifytriggertype.urge", "enum.process.processtasknotifytriggertype.urge.1"),
    ABORTPROCESSTASK("abortprocesstask", "enum.process.processtasknotifytriggertype.abortprocesstask", "enum.process.processtasknotifytriggertype.abortprocesstask"),
    RECOVERPROCESSTASK("recoverprocesstask", "enum.process.processtasknotifytriggertype.recoverprocesstask", "enum.process.processtasknotifytriggertype.recoverprocesstask.1"),
    COMPLETEPROCESSTASK("completeprocesstask", "enum.process.processtasknotifytriggertype.completeprocesstask", "enum.process.processtasknotifytriggertype.completeprocesstask.1"),
    WAITINGSCOREPROCESSTASK("waitingscoreprocesstask", "enum.process.processtasknotifytriggertype.waitingscoreprocesstask", "enum.process.processtasknotifytriggertype.waitingscoreprocesstask.1"),
    SCOREPROCESSTASK("scoreprocesstask", "enum.process.processtasknotifytriggertype.scoreprocesstask", "enum.process.processtasknotifytriggertype.scoreprocesstask"),
    REOPENPROCESSTASK("reopenprocesstask", "enum.process.processtasknotifytriggertype.reopenprocesstask", "enum.process.processtasknotifytriggertype.reopenprocesstask.1"),
    MARKREPEATPROCESSTASK("markrepeatprocesstask", "enum.process.processtasknotifytriggertype.markrepeatprocesstask", "enum.process.processtasknotifytriggertype.markrepeatprocesstask"),
    DELETEPROCESSTASK("deleteprocesstask", "enum.process.processtasknotifytriggertype.deleteprocesstask", "enum.process.processtasknotifytriggertype.deleteprocesstask"),
    ;

    private String trigger;
    private String text;
    private String description;

    ProcessTaskNotifyTriggerType(String _trigger, String _text, String _description) {
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
        return text;
    }

    @Override
    public String getDescription() {
        return description;
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
