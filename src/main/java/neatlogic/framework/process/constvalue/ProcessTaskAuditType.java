package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditType;
import neatlogic.framework.util.$;

public enum ProcessTaskAuditType implements IProcessTaskAuditType {
	COMMENT("comment", "回复", "回复【${DATA.processTaskStepName}】"),
	EDITCOMMENT("editcomment", "编辑回复", "编辑回复【${DATA.processTaskStepName}】"),
	DELETECOMMENT("deletecomment", "删除回复", "删除回复【${DATA.processTaskStepName}】"),
	UPDATE("update", "上报内容", "修改工单"),
	URGE("urge","催办","发起催办"),

	//任务
	CREATETASK("createtask", "创建子任务", "创建${DATA.replaceable_task}【${DATA.processTaskStepName}】"),
	EDITTASK("edittask", "编辑子任务", "编辑${DATA.replaceable_task}【${DATA.processTaskStepName}】"),
	COMPLETETASK("completetask", "回复子任务", "完成${DATA.replaceable_task}【${DATA.processTaskStepName}】"),
	DELETETASK("deletetask", "删除子任务", "删除${DATA.replaceable_task}【${DATA.processTaskStepName}】"),
	SAVETASKFILE("savetaskfile", "上传子任务附件", "${DATA.replaceable_task}【${DATA.processTaskStepName}】 上传附件"),
	DELETETASKFILE("deletetaskfile", "删除子任务附件", "${DATA.replaceable_task}【${DATA.processTaskStepName}】 删除附件"),


	START("start", "开始", "开始【${DATA.processTaskStepName}】"),
	RETREAT("retreat", "撤回", "撤回【${DATA.processTaskStepName}】"),
	ABORTPROCESSTASK("abortprocesstask", "取消", "取消工单"),
	RECOVERPROCESSTASK("recoverprocesstask", "恢复", "恢复工单"),
	PAUSE("pause", "暂停", "暂停【${DATA.processTaskStepName}】"),
	RECOVER("recover", "恢复", "恢复【${DATA.processTaskStepName}】"),
	TRANSFER("transfer", "转交", "转交【${DATA.processTaskStepName}】"),
	STARTPROCESS("startprocess", "上报", "提交工单"),
	RESTFULACTION("restfulaction", "RESTFUL动作","【${DATA.processTaskStepName}】"),
	COMPLETE("complete", "流转", "完成【${DATA.processTaskStepName}】"),
	REAPPROVAL("reapproval", "重审", "重审【${DATA.processTaskStepName}】"),
	BACK("back", "回退", "回退【${DATA.processTaskStepName}】至【${DATA.nextStepName}】"),
	CONDITION("condition", "条件", "enum.process.processtaskaudittype.condition.b"),
	SCORE("score", "评分", "评价工单"),
    TRANSFERREPORT("tranferreport", "转报", "转报并关联工单"),
    REPORTRELATION("reportrelation", "上报关联", "上报并关联工单"),
    RELATION("relation", "关联", "关联工单"),
    DELETERELATION("deleterelation", "解除关联", "解除关联工单"),
    REDO("redo", "回退", "回退至【${DATA.processTaskStepName}】"),
    TRANSFERKNOWLEDGE("transferknowledge", "转知识", "提交工单转知识"),
    UPDATEFOCUSUSER("updatefocususer", "修改工单关注人", "修改工单关注人"),
    FOCUSTASK("focustask", "关注工单", "关注工单"),
    UNDOFOCUSTASK("undofocustask", "取消关注工单", "取消关注工单"),
	BINDREPEAT("bindrepeat", "绑定重复工单", "绑定重复工单"),
	UNBINDREPEAT("unbindrepeat", "解绑重复工单", "解绑重复工单")

	;
	private String value;
	private String text;
	private String description;
	
	ProcessTaskAuditType(String value, String text, String description) {
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
		return $.t(text);
	}

	@Override
	public String getDescription() {
		return $.t(description);
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
