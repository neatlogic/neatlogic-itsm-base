package codedriver.framework.process.notify.constvalue;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum TaskNotifyTriggerType implements INotifyTriggerType {
	CREATETASK("createtask", "任务创建","步骤处理人为当前步骤创建任务时触发通知"),
	EDITTASK("edittask", "任务编辑","步骤处理人编辑任务内容时触发通知"),
	DELETETASK("deletetask", "任务删除","步骤处理人删除任务时触发通知"),
	COMPLETETASK("completetask", "任务完成","任务处理人完成任务时触发通知"),
	COMPLETEALLTASK("completealltask", "所有任务完成","所有任务满足流转条件时触发通知");

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
		for(TaskNotifyTriggerType n : values()) {
			if(n.getTrigger().equals(trigger)) {
				return n.getText();
			}
		}
		return "";
	}
}
