package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum SlaNotifyTriggerType implements INotifyTriggerType {
	TIMEOUT("timeout", "超时");

	private String trigger;
	private String text;

	private SlaNotifyTriggerType(String _trigger, String _text) {
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
		for(SlaNotifyTriggerType n : SlaNotifyTriggerType.values()) {
			if(n.getTrigger().equals(trigger)) {
				return n.getText();
			}
		}
		return "";
	}
}
