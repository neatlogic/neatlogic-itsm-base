package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditDetailType;
import neatlogic.framework.util.I18nUtils;

public enum ProcessTaskAuditDetailType implements IProcessTaskAuditDetailType {

    CHANNELTYPERELATION("channeltyperelation", "enum.process.processtaskauditdetailtype.channeltyperelation", "channelTypeRelationId", "oldChannelTypeRelationId", 1, false),
    PROCESSTASKLIST("processtasklist", "enum.process.processtaskauditdetailtype.processtasklist", "processTaskIdList", "oldProcessTaskIdList", 2, false),
    PROCESSTASK("fromprocesstask", "enum.process.processtaskauditdetailtype.processtask", "fromProcessTaskId", "oldFromProcessTaskId", 3, false),
	CONTENT("content", "enum.process.processtaskauditdetailtype.content", "content", "oldContent", 4, true),
	TITLE("title", "enum.process.processtaskauditdetailtype.title", "title", "oldTitle", 5, true),
	PRIORITY("priority", "enum.process.processtaskauditdetailtype.priority", "priorityUuid", "oldPriorityUuid", 6, false),
	FORM("form", "enum.process.processtaskauditdetailtype.form", "processTaskFormAttributeDataList", "oldProcessTaskFormAttributeDataList", 7, false),
	WORKERLIST("workerlist", "enum.process.processtaskauditdetailtype.workerlist", "workerList", "oldWorkerList", 8, false),
	FILE("file", "enum.process.processtaskauditdetailtype.file", "fileIdList", "oldFileIdList", 10, false),
	RESTFULACTION("restfulaction", "enum.process.processtaskauditdetailtype.restfulaction", "restfulAction", "oldRestfulAction", 12, false),
	CAUSE("cause", "enum.process.processtaskauditdetailtype.cause", "cause", "oldCause", 13, true),
	RULE("rule", "enum.process.processtaskauditdetailtype.rule", "rule", "oldRule", 14, false),
	SCORE("score", "enum.process.processtaskauditdetailtype.score", "score", "oldScore", 15, false),
	TAGLIST("taglist","enum.process.processtaskauditdetailtype.taglist","tagList","oldTagList",16, false),
	FOCUSUSER("focususer","enum.process.processtaskauditdetailtype.focususer","focusUser","oldFocusUser",17, false),
	TASK("task", "enum.process.processtaskauditdetailtype.task", "task", "oldTask", 18, false),
	;
	
	private String value;
	private String text;
	private String paramName;
	private String oldDataParamName;
	private int sort;
	private boolean needCompression;
	
	private ProcessTaskAuditDetailType(String _value, String _text, String _paramName, String _oldDataParamName, int _sort, boolean _needCompression) {
		this.value = _value;
		this.text = _text;
		this.paramName = _paramName;
		this.oldDataParamName = _oldDataParamName;
		this.sort = _sort;
		this.needCompression = _needCompression;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return I18nUtils.getMessage(text);
	}

	public String getParamName() {
		return paramName;
	}

	public String getOldDataParamName() {
		return oldDataParamName;
	}

	public int getSort() {
		return sort;
	}

	@Override
	public boolean getNeedCompression() {
		return needCompression;
	}

}
