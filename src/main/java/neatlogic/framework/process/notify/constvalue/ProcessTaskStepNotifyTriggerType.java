package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskStepNotifyTriggerType implements INotifyTriggerType {

    ACTIVE("active", new I18n("enum.process.processtaskstepnotifytriggertype.active.a"), new I18n("enum.process.processtaskstepnotifytriggertype.active.b")),
    ASSIGNEXCEPTION("assignexception", new I18n("enum.process.processtaskstepnotifytriggertype.assignexception.a"), new I18n("enum.process.processtaskstepnotifytriggertype.assignexception.b")),
    START("start", new I18n("enum.process.processtaskstepnotifytriggertype.start.a"), new I18n("enum.process.processtaskstepnotifytriggertype.start.b")),
    TRANSFER("transfer", new I18n("enum.process.processtaskstepnotifytriggertype.transfer.a"), new I18n("enum.process.processtaskstepnotifytriggertype.transfer.b")),
    SUCCEED("succeed", new I18n("enum.process.processtaskstepnotifytriggertype.succeed.a"), new I18n("enum.process.processtaskstepnotifytriggertype.succeed.b")),
    BACK("back", new I18n("common.steprollback"), new I18n("enum.process.processtaskstepnotifytriggertype.back.b")),
    RETREAT("retreat", new I18n("enum.process.processtaskstepnotifytriggertype.retreat.a"), new I18n("enum.process.processtaskstepnotifytriggertype.retreat.b")),
    PAUSE("pause", new I18n("enum.process.processtaskstepnotifytriggertype.pause.a"), new I18n("enum.process.processtaskstepnotifytriggertype.pause.b")),
    RECOVER("recover", new I18n("enum.process.processtaskstepnotifytriggertype.recover.a"), new I18n("enum.process.processtaskstepnotifytriggertype.recover.b")),
    FAILED("failed", new I18n("enum.process.processtaskstepnotifytriggertype.failed.a"), new I18n("enum.process.processtaskstepnotifytriggertype.failed.b")),
    URGE("urge", new I18n("enum.process.processtaskstepnotifytriggertype.urge.a"), new I18n("common.urgetrigger")),
	COMMENT("comment", new I18n("common.stepreply"),new I18n("enum.process.processtaskstepnotifytriggertype.comment.b")),
	;

    private String trigger;
    private I18n text;
    private I18n description;

    ProcessTaskStepNotifyTriggerType(String _trigger, I18n _text, I18n _description) {
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
