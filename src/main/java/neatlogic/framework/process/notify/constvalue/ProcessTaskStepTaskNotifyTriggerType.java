package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskStepTaskNotifyTriggerType implements INotifyTriggerType {
    CREATETASK("createtask", new I18n("子任务创建"), new I18n("步骤处理人为当前步骤创建子任务时触发通知")),
    EDITTASK("edittask", new I18n("子任务编辑"), new I18n("步骤处理人编辑子任务内容时触发通知")),
    DELETETASK("deletetask", new I18n("子任务删除"), new I18n("步骤处理人删除子任务时触发通知")),
    COMPLETETASK("completetask", new I18n("子任务完成"), new I18n("任务处理人完成子任务时触发通知")),
    COMPLETEALLTASK("completealltask", new I18n("子任务满足步骤流转"), new I18n("所有子任务满足流转条件时触发通知"));

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
