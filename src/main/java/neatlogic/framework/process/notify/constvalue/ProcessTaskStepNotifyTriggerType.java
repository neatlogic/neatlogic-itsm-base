package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskStepNotifyTriggerType implements INotifyTriggerType {

    ACTIVE("active", new I18n("enum.process.processtaskstepnotifytriggertype.active"), new I18n("enum.process.processtaskstepnotifytriggertype.active.1")),
    //	ASSIGN("assign", "步骤分配处理人","步骤激活后，系统根据配置为该步骤分配处理人时触发通知"),
    ASSIGNEXCEPTION("assignexception", new I18n("enum.process.processtaskstepnotifytriggertype.assignexception"), new I18n("enum.process.processtaskstepnotifytriggertype.assignexception.1")),
    START("start", new I18n("enum.process.processtaskstepnotifytriggertype.start"), new I18n("enum.process.processtaskstepnotifytriggertype.start.1")),
    TRANSFER("transfer", new I18n("enum.process.processtaskstepnotifytriggertype.transfer"), new I18n("enum.process.processtaskstepnotifytriggertype.transfer.1")),
    SUCCEED("succeed", new I18n("enum.process.processtaskstepnotifytriggertype.succeed"), new I18n("enum.process.processtaskstepnotifytriggertype.succeed.1")),
    BACK("back", new I18n("enum.process.processtaskstepnotifytriggertype.back"), new I18n("enum.process.processtaskstepnotifytriggertype.back.1")),
    RETREAT("retreat", new I18n("enum.process.processtaskstepnotifytriggertype.retreat"), new I18n("enum.process.processtaskstepnotifytriggertype.retreat.1")),
    //	HANG("hang", "步骤挂起","当前步骤被回退或撤回时触发通知，当前步骤将处于挂起状态"),
    PAUSE("pause", new I18n("enum.process.processtaskstepnotifytriggertype.pause"), new I18n("enum.process.processtaskstepnotifytriggertype.pause")),
    FAILED("failed", new I18n("enum.process.processtaskstepnotifytriggertype.failed"), new I18n("enum.process.processtaskstepnotifytriggertype.failed.1")),
    URGE("urge", new I18n("enum.process.processtaskstepnotifytriggertype.urge"), new I18n("enum.process.processtaskstepnotifytriggertype.urge.1")),
    ;

    private String trigger;
    private I18n text;
    private I18n description;

    private ProcessTaskStepNotifyTriggerType(String _trigger, I18n _text, I18n _description) {
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
        for (ProcessTaskStepNotifyTriggerType n : values()) {
            if (n.getTrigger().equals(trigger)) {
                return n.getText();
            }
        }
        return "";
    }
}
