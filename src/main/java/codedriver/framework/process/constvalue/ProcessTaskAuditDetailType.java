package codedriver.framework.process.constvalue;

import codedriver.framework.process.audithandler.core.IProcessTaskAuditDetailType;

public enum ProcessTaskAuditDetailType implements IProcessTaskAuditDetailType {

    CHANNELTYPERELATION("channeltyperelation", "关系类型", "channelTypeRelationId", "oldChannelTypeRelationId", 1),
    PROCESSTASKLIST("processtasklist", "工单", "processTaskIdList", "oldProcessTaskIdList", 2),
    PROCESSTASK("fromprocesstask", "原工单", "fromProcessTaskId", "oldFromProcessTaskId", 3),
	CONTENT("content", "内容", "content", "oldContent", 4),
	TITLE("title", "标题", "title", "oldTitle", 5),
	PRIORITY("priority", "优先级", "priorityUuid", "oldPriorityUuid", 6),
	FORM("form", "表单", "processTaskFormAttributeDataList", "oldProcessTaskFormAttributeDataList", 7),
	WORKERLIST("workerlist", "处理人", "workerList", "oldWorkerList", 8),
	SUBTASK("subtask", "子任务", "subtask", "oldSubtask", 9),
	FILE("file", "上传文件", "fileIdList", "oldFileIdList", 10),
	TASKSTEP("taskstep", "工单步骤", "nextStepId", "oldNextStepId", 11),
	RESTFULACTION("restfulaction", "RESTFUL动作", "restfulAction", "oldRestfulAction", 12),
	CAUSE("cause", "原因", "cause", "oldCause", 13),
	RULE("rule", "流转规则", "rule", "oldRule", 14),
	SCORE("score", "评分", "score", "oldScore", 15)
	;
	
	private String value;
	private String text;
	private String paramName;
	private String oldDataParamName;
	private int sort;
	
	private ProcessTaskAuditDetailType(String _value, String _text, String _paramName, String _oldDataParamName, int _sort) {
		this.value = _value;
		this.text = _text;
		this.paramName = _paramName;
		this.oldDataParamName = _oldDataParamName;
		this.sort = _sort;
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

}
