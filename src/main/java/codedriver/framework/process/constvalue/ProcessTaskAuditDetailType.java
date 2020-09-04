package codedriver.framework.process.constvalue;

import codedriver.framework.process.audithandler.core.IProcessTaskAuditDetailType;

public enum ProcessTaskAuditDetailType implements IProcessTaskAuditDetailType {

	CONTENT("content", "内容", "content", "oldContent", 1),
	TITLE("title", "标题", "title", "oldTitle", 2),
	PRIORITY("priority", "优先级", "priorityUuid", "oldPriorityUuid", 3),
	FORM("form", "表单", "processTaskFormAttributeDataList", "oldProcessTaskFormAttributeDataList", 4),
	WORKERLIST("workerlist", "处理人", "workerList", "oldWorkerList", 5),
	SUBTASK("subtask", "子任务", "subtask", "oldSubtask", 6),
	FILE("file", "上传文件", "fileIdList", "oldFileIdList", 7),
	TASKSTEP("taskstep", "工单步骤", "nextStepId", "oldNextStepId", 8),
	RESTFULACTION("restfulaction", "RESTFUL动作", "restfulAction", "oldRestfulAction", 9),
	CAUSE("cause", "原因", "cause", "oldCause", 10),
	RULE("rule", "流转规则", "rule", "oldRule", 11),
	SCORE("score", "评分", "score", "oldScore", 12)
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
