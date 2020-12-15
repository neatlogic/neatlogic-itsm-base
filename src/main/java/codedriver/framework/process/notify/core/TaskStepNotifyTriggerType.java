package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum TaskStepNotifyTriggerType implements INotifyTriggerType {

	ACTIVE("active", "步骤激活","流转到某个步骤时，该步骤被自动被激活时触发通知"),
	ASSIGN("assign", "步骤分配处理人","步骤激活后，系统根据配置为该步骤分配处理人时触发通知"),
	ASSIGNEXCEPTION("assignexception", "分配异常","步骤激活时分配处理人，处理人不存在或规则无法匹配到有效处理人时触发通知"),
	START("start", "步骤开始","步骤自动开始或者处理人手动开始处理时触发通知"),
	TRANSFER("transfer", "步骤转交","有步骤转交权限的用户将当前步骤处理权限转交给其他用户时触发通知"),
	SUCCEED("succeed", "步骤成功","步骤完成时触发通知"),
	BACK("back", "步骤回退","工单完成前，由处理人手动回退工单至前面的某个步骤时触发通知"),
	RETREAT("retreat", "步骤撤回","工单完成前，由上一步骤有权限的用户手动撤回工单时触发通知，重新处理上一步骤"),
	HANG("hang", "步骤挂起","当前步骤被回退或撤回时触发通知，当前步骤将处于挂起状态"),
//	ABORT("abort", "步骤终止","步骤终止"),
//	RECOVER("recover", "步骤恢复","步骤恢复"),
	PAUSE("pause", "步骤暂停","有权限的用户暂停当前步骤时触发通知"),
	FAILED("failed", "步骤失败","当前步骤出现分配异常、流转异常、系统异常导致步骤失败时触发通知"),
//	REDO("redo", "步骤打回","步骤打回"),
	;

	private String trigger;
	private String text;
	private String description;

	private TaskStepNotifyTriggerType(String _trigger, String _text, String _description) {
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
		for(TaskStepNotifyTriggerType n : TaskStepNotifyTriggerType.values()) {
			if(n.getTrigger().equals(trigger)) {
				return n.getText();
			}
		}
		return "";
	}
}
