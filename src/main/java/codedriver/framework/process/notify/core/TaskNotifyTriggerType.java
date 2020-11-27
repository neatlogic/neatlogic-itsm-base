package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum TaskNotifyTriggerType implements INotifyTriggerType {

    STARTPROCESS("startprocess", "上报"),	
	URGE("urge", "催办"),	
	ABORTPROCESSTASK("abortprocessTask", "取消工单"),
	RECOVERPROCESSTASK("recoverprocessTask", "恢复工单"),
	COMPLETEPROCESSTASK("COMPLETEPROCESSTASK", "完成工单"),
	SCOREPROCESSTASK("SCOREPROCESSTASK", "评分"),
	;

	private String trigger;
	private String text;

	private TaskNotifyTriggerType(String _trigger, String _text) {
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
		for(TaskNotifyTriggerType n : TaskNotifyTriggerType.values()) {
			if(n.getTrigger().equals(trigger)) {
				return n.getText();
			}
		}
		return "";
	}
}
