package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum SubtaskNotifyTriggerType implements INotifyTriggerType {
	CREATESUBTASK("createsubtask", "子任务创建","子任务创建"),
	EDITSUBTASK("editsubtask", "子任务编辑","子任务编辑"),
	ABORTSUBTASK("abortsubtask", "子任务取消","子任务取消"),
	REDOSUBTASK("redosubtask", "子任务打回重做","子任务打回重做"),
	COMPLETESUBTASK("completesubtask", "子任务完成","子任务完成");

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
