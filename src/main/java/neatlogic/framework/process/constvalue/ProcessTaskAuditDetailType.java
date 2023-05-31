package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditDetailType;
import neatlogic.framework.util.I18nUtils;

public enum ProcessTaskAuditDetailType implements IProcessTaskAuditDetailType {

    CHANNELTYPERELATION("channeltyperelation", "enum.process.processtaskauditdetailtype.channeltyperelation", "channelTypeRelationId", "oldChannelTypeRelationId", 1, false),
    PROCESSTASKLIST("processtasklist", "common.itsm.processtask", "processTaskIdList", "oldProcessTaskIdList", 2, false),
    PROCESSTASK("fromprocesstask", "enum.process.processtaskauditdetailtype.processtask.a", "fromProcessTaskId", "oldFromProcessTaskId", 3, false),
	CONTENT("content", "enum.process.processtaskauditdetailtype.content", "content", "oldContent", 4, true),
	TITLE("title", "common.title", "title", "oldTitle", 5, true),
	PRIORITY("priority", "common.priority", "priorityUuid", "oldPriorityUuid", 6, false),
	FORM("form", "common.form", "processTaskFormAttributeDataList", "oldProcessTaskFormAttributeDataList", 7, false),
	WORKERLIST("workerlist", "common.worker", "workerList", "oldWorkerList", 8, false),
	FILE("file", "common.file", "fileIdList", "oldFileIdList", 10, false),
	RESTFULACTION("restfulaction", "common.restfulaction", "restfulAction", "oldRestfulAction", 12, false),
	CAUSE("cause", "common.reason", "cause", "oldCause", 13, true),
	RULE("rule", "enum.process.processtaskauditdetailtype.rule", "rule", "oldRule", 14, false),
	SCORE("score", "common.score", "score", "oldScore", 15, false),
	TAGLIST("taglist","common.tag","tagList","oldTagList",16, false),
	FOCUSUSER("focususer","enum.process.processtaskauditdetailtype.focususer","focusUser","oldFocusUser",17, false),
	TASK("task", "common.task", "task", "oldTask", 18, false),
	AUTOMATICINFO("automaticinfo", "enum.process.processtaskauditdetailtype.automaticinfo", "automaticinfo", "oldAutomaticInfo", 19, false),
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
