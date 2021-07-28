package codedriver.framework.process.constvalue;

import codedriver.framework.process.audithandler.core.IProcessTaskAuditType;

public enum ProcessTaskAuditType implements IProcessTaskAuditType {
	COMMENT("comment", "回复", "回复【${DATA.processTaskStepName}】"),
	EDITCOMMENT("editcomment", "编辑回复", "编辑回复【${DATA.processTaskStepName}】"),
	DELETECOMMENT("deletecomment", "删除回复", "删除回复【${DATA.processTaskStepName}】"),
//	UPDATETITLE("updatetitle", "更新标题", "修改工单"),
//	UPDATEPRIORITY("updatepriority", "更新优先级", "修改工单"),
//	UPDATECONTENT("updatecontent", "更新上报描述内容", "修改工单"),
	UPDATE("update", "上报内容", "修改工单"),
	URGE("urge","催办","发起催办"),
	CREATESUBTASK("createsubtask", "创建子任务", "创建${DATA.replaceable_subtask}【${DATA.processTaskStepName}】"),
	EDITSUBTASK("editsubtask", "编辑子任务", "修改${DATA.replaceable_subtask}【${DATA.processTaskStepName}】"),
	ABORTSUBTASK("abortsubtask", "取消子任务", "取消${DATA.replaceable_subtask}【${DATA.processTaskStepName}】"),
	REDOSUBTASK("redosubtask", "重做子任务", "重做${DATA.replaceable_subtask}【${DATA.processTaskStepName}】"),
	COMPLETESUBTASK("completesubtask", "完成子任务", "完成${DATA.replaceable_subtask}【${DATA.processTaskStepName}】"),
	START("start", "开始", "开始【${DATA.processTaskStepName}】"),
	RETREAT("retreat", "撤回", "撤回【${DATA.processTaskStepName}】"),
	ABORTPROCESSTASK("abort", "取消", "取消工单"),
	RECOVERPROCESSTASK("recover", "恢复", "恢复工单"),
	PAUSE("pause", "暂停", "暂停【${DATA.processTaskStepName}】"),
	TRANSFER("transfer", "转交", "转交【${DATA.processTaskStepName}】"),
	STARTPROCESS("startprocess", "上报", "提交工单"),
	RESTFULACTION("restfulaction", "RESTFUL动作","【${DATA.processTaskStepName}】"),
	COMPLETE("complete", "流转", "完成【${DATA.processTaskStepName}】"),
	BACK("back", "回退", "回退【${DATA.processTaskStepName}】至【${DATA.nextStepName}】"),
	CONDITION("condition", "条件", "条件 <span style=\"color:${DATA.stepStatusVo.color};vertical-align: baseline;\">${DATA.stepStatusVo.text}</span> "),
	SCORE("score", "评分", "评价工单"),
    TRANFERREPORT("tranferreport", "转报", "转报并关联工单"),
    REPORTRELATION("reportrelation", "上报关联", "上报并关联工单"),
    RELATION("relation", "关联", "关联工单"),
    DELETERELATION("deleterelation", "解除关联", "解除关联工单"),
    REDO("redo", "回退", "回退至【${DATA.processTaskStepName}】"),
    TRANSFERKNOWLEDGE("transferknowledge", "转知识", "提交工单转知识"),
    UPDATEFOCUSUSER("updatefocususer", "修改工单关注人", "修改工单关注人"),
    FOCUSTASK("focustask", "关注工单", "关注工单"),
    UNDOFOCUSTASK("undofocustask", "取消关注工单", "取消关注工单")
	;
	private String value;
	private String text;
	private String description;
	
	private ProcessTaskAuditType(String value, String text, String description) {
		this.value = value;
		this.text = text;
		this.description = description;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public static ProcessTaskAuditType getProcessTaskAuditType(String _value) {
		for(ProcessTaskAuditType type : values()) {
			if(type.getValue().equals(_value)) {
				return type;
			}
		}
		return null;
	}

}
