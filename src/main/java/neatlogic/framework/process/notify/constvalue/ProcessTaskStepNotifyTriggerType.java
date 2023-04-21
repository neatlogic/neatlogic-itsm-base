package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;

public enum ProcessTaskStepNotifyTriggerType implements INotifyTriggerType {

    ACTIVE("active", "enum.process.processtaskstepnotifytriggertype.active", "enum.process.processtaskstepnotifytriggertype.active.1"),
    //	ASSIGN("assign", "步骤分配处理人","步骤激活后，系统根据配置为该步骤分配处理人时触发通知",
    ASSIGNEXCEPTION("assignexception", "enum.process.processtaskstepnotifytriggertype.assignexception", "enum.process.processtaskstepnotifytriggertype.assignexception.1"),
    START("start", "enum.process.processtaskstepnotifytriggertype.start", "enum.process.processtaskstepnotifytriggertype.start.1"),
    TRANSFER("transfer", "enum.process.processtaskstepnotifytriggertype.transfer", "enum.process.processtaskstepnotifytriggertype.transfer.1"),
    SUCCEED("succeed", "enum.process.processtaskstepnotifytriggertype.succeed", "enum.process.processtaskstepnotifytriggertype.succeed.1"),
    BACK("back", "enum.process.processtaskstepnotifytriggertype.back", "enum.process.processtaskstepnotifytriggertype.back.1"),
    RETREAT("retreat", "enum.process.processtaskstepnotifytriggertype.retreat", "enum.process.processtaskstepnotifytriggertype.retreat.1"),
    //	HANG("hang", "步骤挂起","当前步骤被回退或撤回时触发通知，当前步骤将处于挂起状态",
    PAUSE("pause", "enum.process.processtaskstepnotifytriggertype.pause", "enum.process.processtaskstepnotifytriggertype.pause"),
    FAILED("failed", "enum.process.processtaskstepnotifytriggertype.failed", "enum.process.processtaskstepnotifytriggertype.failed.1"),
    URGE("urge", "enum.process.processtaskstepnotifytriggertype.urge", "enum.process.processtaskstepnotifytriggertype.urge.1"),
	COMMENT("comment", "步骤回复","用户对步骤进行回复时触发通知"),
	;

    private String trigger;
    private String text;
    private String description;

    private ProcessTaskStepNotifyTriggerType(String _trigger, String _text, String _description) {
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
