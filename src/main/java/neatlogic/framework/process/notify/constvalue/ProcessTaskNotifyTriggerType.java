package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskNotifyTriggerType implements INotifyTriggerType {

    STARTPROCESS("startprocess", new I18n("上报"), new I18n("用户上报提交工单时触发通知")),
    ABORTPROCESSTASK("abortprocesstask", new I18n("取消工单"), new I18n("取消工单")),
    RECOVERPROCESSTASK("recoverprocesstask", new I18n("恢复工单"), new I18n("工单完成前，有权限用户恢复工单时触发通知")),
    COMPLETEPROCESSTASK("completeprocesstask", new I18n("完成工单"), new I18n("工单流转至结束时触发通知")),
    WAITINGSCOREPROCESSTASK("waitingscoreprocesstask", new I18n("待评分"), new I18n("工单完成后需要评分时触发通知")),
    SCOREPROCESSTASK("scoreprocesstask", new I18n("评分"), new I18n("评分")),
    REOPENPROCESSTASK("reopenprocesstask", new I18n("重新打开工单"), new I18n("工单完成后，用户评分前，有权限的用户重新打开工单并回退至某一步骤重新开始处理时触发通知")),
    MARKREPEATPROCESSTASK("markrepeatprocesstask", new I18n("标记重复事件"), new I18n("标记重复事件")),
    DELETEPROCESSTASK("deleteprocesstask", new I18n("删除工单"), new I18n("删除工单")),
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
