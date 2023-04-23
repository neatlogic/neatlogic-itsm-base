package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;

public enum ProcessTaskStepTaskNotifyTriggerType implements INotifyTriggerType {
    CREATETASK("createtask", "enum.process.processtasksteptasknotifytriggertype.createtask", "enum.process.processtasksteptasknotifytriggertype.createtask.1"),
    EDITTASK("edittask", "enum.process.processtasksteptasknotifytriggertype.edittask", "enum.process.processtasksteptasknotifytriggertype.edittask.1"),
    DELETETASK("deletetask", "enum.process.processtasksteptasknotifytriggertype.deletetask", "enum.process.processtasksteptasknotifytriggertype.deletetask.1"),
    COMPLETETASK("completetask", "enum.process.processtasksteptasknotifytriggertype.completetask", "enum.process.processtasksteptasknotifytriggertype.completetask.1"),
    COMPLETEALLTASK("completealltask", "enum.process.processtasksteptasknotifytriggertype.completealltask", "enum.process.processtasksteptasknotifytriggertype.completealltask.1");

    private String trigger;
    private String text;
    private String description;

    private ProcessTaskStepTaskNotifyTriggerType(String _trigger, String _text, String _description) {
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
        for (ProcessTaskStepTaskNotifyTriggerType n : values()) {
            if (n.getTrigger().equals(trigger)) {
                return n.getText();
            }
        }
        return "";
    }
}
