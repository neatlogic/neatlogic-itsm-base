package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum TaskNotifyTriggerType implements INotifyTriggerType {

    STARTPROCESS("startprocess", "上报"),	
	URGE("urge", "催办"),	
//	ABORT("abort", "步骤终止"),
//	RECOVER("recover", "步骤恢复"),
	PROCESSTASKCOMPLETE("processtaskcomplete", "工单完成"),
	PROCESSTASKSCORE("processtaskscore", "工单评分"),
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
