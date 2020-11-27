package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum TaskStepNotifyTriggerType implements INotifyTriggerType {

	ACTIVE("active", "步骤激活"),
	ASSIGN("assign", "步骤分配处理人"),
	START("start", "步骤开始"),
	TRANSFER("transfer", "步骤转交"),
	SUCCEED("succeed", "步骤成功"),
	BACK("back", "步骤回退"),
	RETREAT("retreat", "步骤撤回"),
	HANG("hang", "步骤挂起"),
	ABORT("abort", "步骤终止"),
	RECOVER("recover", "步骤恢复"),
	PAUSE("pause", "步骤暂停"),
	FAILED("failed", "步骤失败"),
	REDO("redo", "步骤打回"),
	;

	private String trigger;
	private String text;

	private TaskStepNotifyTriggerType(String _trigger, String _text) {
		this.trigger = _trigger;
		this.text = _text;
	}

	@Override
	public String getTrigger() {
		return trigger;
	}
	@Override
	public String getText() {
		return text;
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
