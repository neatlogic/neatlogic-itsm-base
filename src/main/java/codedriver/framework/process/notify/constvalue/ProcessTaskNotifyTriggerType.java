package codedriver.framework.process.notify.constvalue;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum ProcessTaskNotifyTriggerType implements INotifyTriggerType {

    STARTPROCESS("startprocess", "上报", "用户上报提交工单时触发通知"),
    URGE("urge", "催办", "工单完成前，用户对工单进行催办时触发通知"),
    ABORTPROCESSTASK("abortprocesstask", "取消工单", "工单完成前，有权限用户取消工单时触发通知"),
    RECOVERPROCESSTASK("recoverprocesstask", "恢复工单", "工单完成前，有权限用户恢复工单时触发通知"),
    COMPLETEPROCESSTASK("completeprocesstask", "完成工单", "工单流转至结束时触发通知"),
    WAITINGSCOREPROCESSTASK("waitingscoreprocesstask", "待评分", "工单完成后需要评分时触发通知"),
    SCOREPROCESSTASK("scoreprocesstask", "评分", "评分后触发通知"),
    REOPENPROCESSTASK("reopenprocesstask", "重新打开工单", "工单完成后，用户评分前，有权限的用户重新打开工单并回退至某一步骤重新开始处理时触发通知"),
    MARKREPEATPROCESSTASK("markrepeatprocesstask", "标记重复事件", "标记重复事件"),
    DELETEPROCESSTASK("deleteprocesstask", "删除工单", "删除工单"),
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
