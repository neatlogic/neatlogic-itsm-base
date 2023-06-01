package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskStepNotifyTriggerType implements INotifyTriggerType {

    ACTIVE("active", new I18n("步骤激活"), new I18n("流转到某个步骤，该步骤被自动激活时触发通知")),
    ASSIGNEXCEPTION("assignexception", new I18n("分配异常"), new I18n("步骤激活时分配处理人，处理人不存在或规则无法匹配到有效处理人时触发通知")),
    START("start", new I18n("步骤开始"), new I18n("步骤自动开始或者处理人手动开始处理时触发通知")),
    TRANSFER("transfer", new I18n("步骤转交"), new I18n("有步骤转交权限的用户将当前步骤处理权限转交给其他用户时触发通知")),
    SUCCEED("succeed", new I18n("步骤成功"), new I18n("步骤完成时触发通知")),
    BACK("back", new I18n("步骤回退"), new I18n("工单完成前，由处理人手动回退工单至前面的某个步骤时触发通知")),
    RETREAT("retreat", new I18n("步骤撤回"), new I18n("工单完成前，由上一步骤有权限的用户手动撤回工单时触发通知，重新处理上一步骤")),
    PAUSE("pause", new I18n("步骤暂停"), new I18n("有权限的用户暂停当前步骤时触发通知")),
    RECOVER("recover", new I18n("步骤恢复"), new I18n("有权限的用户恢复当前步骤时触发通知")),
    FAILED("failed", new I18n("步骤失败"), new I18n("当前步骤出现分配异常、流转异常、系统异常导致步骤失败时触发通知")),
    URGE("urge", new I18n("步骤催办"), new I18n("工单完成前，用户对工单进行催办时触发通知")),
	COMMENT("comment", new I18n("步骤回复"),new I18n("用户对步骤进行回复时触发通知")),
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
