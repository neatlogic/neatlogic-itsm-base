package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditDetailType;
import neatlogic.framework.util.$;

public enum ProcessTaskAuditDetailType implements IProcessTaskAuditDetailType {

    CHANNELTYPERELATION("channeltyperelation", "nfpc.processtaskauditdetailtype.text.channeltyperelation", "channelTypeRelationId", "oldChannelTypeRelationId", 1, false),
    PROCESSTASKLIST("processtasklist", "nfpc.processtaskauditdetailtype.text.processtasklist", "processTaskIdList", "oldProcessTaskIdList", 2, false),
    PROCESSTASK("fromprocesstask", "nfpc.processtaskauditdetailtype.text.processtask", "fromProcessTaskId", "oldFromProcessTaskId", 3, false),
	CONTENT("content", "nfpc.processtaskauditdetailtype.text.content", "content", "oldContent", 4, true),
	TITLE("title", "nfpc.processtaskauditdetailtype.text.title", "title", "oldTitle", 5, true),
	PRIORITY("priority", "nfpc.processtaskauditdetailtype.text.priority", "priorityUuid", "oldPriorityUuid", 6, false),
	FORM("form", "nfpc.processtaskauditdetailtype.text.form", "processTaskFormAttributeDataList", "oldProcessTaskFormAttributeDataList", 7, false),
	WORKERLIST("workerlist", "nfpc.processtaskauditdetailtype.text.workerlist", "workerList", "oldWorkerList", 8, false),
	FILE("file", "nfpc.processtaskauditdetailtype.text.file", "fileIdList", "oldFileIdList", 10, false),
	RESTFULACTION("restfulaction", "nfpc.processtaskauditdetailtype.text.restfulaction", "restfulAction", "oldRestfulAction", 12, false),
	CAUSE("cause", "nfpc.processtaskauditdetailtype.text.cause", "cause", "oldCause", 13, true),
	RULE("rule", "nfpc.processtaskauditdetailtype.text.rule", "rule", "oldRule", 14, false),
	SCORE("score", "nfpc.processtaskauditdetailtype.text.score", "score", "oldScore", 15, false),
	TAGLIST("taglist","nfpc.processtaskauditdetailtype.text.taglist","tagList","oldTagList",16, false),
	FOCUSUSER("focususer","nfpc.processtaskauditdetailtype.text.focususer","focusUser","oldFocusUser",17, false),
	TASK("task", "nfpc.processtaskauditdetailtype.text.task", "task", "oldTask", 18, false),
	AUTOMATICINFO("automaticinfo", "nfpc.processtaskauditdetailtype.text.automaticinfo", "automaticinfo", "oldAutomaticInfo", 19, false),
	DESCRIPTION("description", "nfpc.processtaskauditdetailtype.text.description", "description", "oldDescription", 21, true),
	OPINION("opinion", "nfpc.processtaskauditdetailtype.text.opinion", "opinion", "oldOpinion", 22, true),
	ASSIGNWORKERLIST("assignworkerlist", "nfpc.processtaskauditdetailtype.text.assignworkerlist", "assignWorkerList", "oldAssignWorkerList", 23, false),
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
		return $.t(text);
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
