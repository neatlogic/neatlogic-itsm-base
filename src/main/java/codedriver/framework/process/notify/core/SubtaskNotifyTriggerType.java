package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum SubtaskNotifyTriggerType implements INotifyTriggerType {
	CREATESUBTASK("createsubtask", "子任务创建","步骤处理人为当前步骤创建子任务时触发通知"),
	EDITSUBTASK("editsubtask", "子任务编辑","步骤处理人编辑子任务内容时触发通知"),
	ABORTSUBTASK("abortsubtask", "子任务取消","步骤处理人取消子任务时触发通知"),
	REDOSUBTASK("redosubtask", "子任务打回重做","步骤处理人将子任务打回给子任务处理人重做时触发通知"),
	COMPLETESUBTASK("completesubtask", "子任务完成","子任务处理人完成子任务时触发通知"),
	COMPLETEALLSUBTASK("completeallsubtask", "所有子任务完成","所有子任务处理人完成全部子任务时触发通知");

	private String trigger;
	private String text;
	private String description;

	private SubtaskNotifyTriggerType(String _trigger, String _text, String _description) {
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
		for(SubtaskNotifyTriggerType n : SubtaskNotifyTriggerType.values()) {
			if(n.getTrigger().equals(trigger)) {
				return n.getText();
			}
		}
		return "";
	}
}
