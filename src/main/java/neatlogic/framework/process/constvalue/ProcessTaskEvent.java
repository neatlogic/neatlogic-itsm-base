package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessTaskEvent {

	ACTIVE("active", "nfpc.processtaskevent.text.active"),
	SUCCEED("succeed", "nfpc.processtaskevent.text.succeed"),
	FAILED("failed", "nfpc.processtaskevent.text.failed"),
	REDO("redo", "nfpc.processtaskevent.text.redo"),
	ABORT("abort", "nfpc.processtaskevent.text.abort"),
	HANDLE("handle", "nfpc.processtaskevent.text.handle"),
	TIMEOUT("timeout", "nfpc.processtaskevent.text.timeout");
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
		return $.t(text);
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
