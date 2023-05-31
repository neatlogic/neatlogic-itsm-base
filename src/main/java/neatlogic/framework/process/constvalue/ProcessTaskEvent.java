package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ProcessTaskEvent {

	ACTIVE("active", "common.active"),
	SUCCEED("succeed", "common.success"),
	FAILED("failed", "common.fail"),
	REDO("redo", "common.redo"),
	ABORT("abort", "enum.process.processtaskevent.abort"),
	HANDLE("handle", "common.handle"),
	TIMEOUT("timeout", "common.timeout");
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
