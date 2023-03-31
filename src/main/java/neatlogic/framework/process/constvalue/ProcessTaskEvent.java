package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ProcessTaskEvent {

	ACTIVE("active", "enum.process.processtaskevent.active"),
	SUCCEED("succeed", "enum.process.processtaskevent.succeed"),
	FAILED("failed", "enum.process.processtaskevent.failed"),
	REDO("redo", "enum.process.processtaskevent.redo"),
	ABORT("abort", "enum.process.processtaskevent.abort"),
	HANDLE("handle", "enum.process.processtaskevent.handle"),
	TIMEOUT("timeout", "enum.process.processtaskevent.timeout");
	private String name;
	private String text;

	private ProcessTaskEvent(String _name, String _text) {
		this.name = _name;
		this.text = _text;
	}

	public String getValue() {
		return name;
	}

	public String getText() {
		return I18nUtils.getMessage(text);
	}

	public static String getText(String name) {
		for (ProcessTaskEvent s : ProcessTaskEvent.values()) {
			if (s.getValue().equals(name)) {
				return s.getText();
			}
		}
		return "";
	}
}
