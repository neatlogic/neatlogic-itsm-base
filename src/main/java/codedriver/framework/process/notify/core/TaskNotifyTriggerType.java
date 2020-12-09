package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum TaskNotifyTriggerType implements INotifyTriggerType {

    STARTPROCESS("startprocess", "上报","上报"),
	URGE("urge", "催办","催办"),
	ABORTPROCESSTASK("abortprocessTask", "取消工单","取消工单"),
	RECOVERPROCESSTASK("recoverprocessTask", "恢复工单","恢复工单"),
	COMPLETEPROCESSTASK("COMPLETEPROCESSTASK", "完成工单","完成工单"),
	SCOREPROCESSTASK("SCOREPROCESSTASK", "评分","评分"),
	;

	private String trigger;
	private String text;
	private String description;

	private TaskNotifyTriggerType(String _trigger, String _text, String _description) {
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
		for(TaskNotifyTriggerType n : TaskNotifyTriggerType.values()) {
			if(n.getTrigger().equals(trigger)) {
				return n.getText();
			}
		}
		return "";
	}
}
