package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18nUtils;

public enum ProcessTaskGroupSearch {
	PROCESSUSERTYPE("processUserType","enum.process.processtaskgroupsearch.processusertype");
	
	private String value;
	private String text;
	private ProcessTaskGroupSearch(String value, String text) {
		this.value = value;
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	
	public String getValuePlugin() {
		return value + "#";
	}

	public String getText() {
		return I18nUtils.getMessage(text);
	}
}
