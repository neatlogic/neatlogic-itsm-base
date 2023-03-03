package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditDetailType;

public enum ProcessTaskAuditDetailType implements IProcessTaskAuditDetailType {

    CHANNELTYPERELATION("channeltyperelation", "关系类型", "channelTypeRelationId", "oldChannelTypeRelationId", 1, false),
    PROCESSTASKLIST("processtasklist", "工单", "processTaskIdList", "oldProcessTaskIdList", 2, false),
    PROCESSTASK("fromprocesstask", "原工单", "fromProcessTaskId", "oldFromProcessTaskId", 3, false),
	CONTENT("content", "内容", "content", "oldContent", 4, true),
	TITLE("title", "标题", "title", "oldTitle", 5, true),
	PRIORITY("priority", "优先级", "priorityUuid", "oldPriorityUuid", 6, false),
	FORM("form", "表单", "processTaskFormAttributeDataList", "oldProcessTaskFormAttributeDataList", 7, false),
	WORKERLIST("workerlist", "处理人", "workerList", "oldWorkerList", 8, false),
	FILE("file", "文件", "fileIdList", "oldFileIdList", 10, false),
	RESTFULACTION("restfulaction", "RESTFUL动作", "restfulAction", "oldRestfulAction", 12, false),
	CAUSE("cause", "原因", "cause", "oldCause", 13, true),
	RULE("rule", "流转规则", "rule", "oldRule", 14, false),
	SCORE("score", "评分", "score", "oldScore", 15, false),
	TAGLIST("taglist","标签","tagList","oldTagList",16, false),
	FOCUSUSER("focususer","工单关注人","focusUser","oldFocusUser",17, false),
	TASK("task", "任务", "task", "oldTask", 18, false),
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
		return text;
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
