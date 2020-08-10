package codedriver.framework.process.constvalue;

public enum ProcessTaskAuditType implements IProcessTaskAuditType {
	COMMENT("comment", "回复", "回复【${DATA.processTaskStepName}】"),
	EDITCOMMENT("editcomment", "编辑回复", "编辑回复【${DATA.processTaskStepName}】"),
	DELETECOMMENT("deletecomment", "删除回复", "删除回复【${DATA.processTaskStepName}】"),
	UPDATETITLE("updatetitle", "更新标题", "修改工单"),
	UPDATEPRIORITY("updatepriority", "更新优先级", "修改工单"),
	UPDATECONTENT("updatecontent", "更新上报描述内容", "修改工单"),
	URGE("urge","催办","发起催办"),
	CREATESUBTASK("createsubtask", "创建子任务", "创建子任务【${DATA.processTaskStepName}】"),
	EDITSUBTASK("editsubtask", "编辑子任务", "修改子任务【${DATA.processTaskStepName}】"),
	ABORTSUBTASK("abortsubtask", "取消子任务", "取消子任务【${DATA.processTaskStepName}】"),
	REDOSUBTASK("redosubtask", "重做子任务", "重做子任务【${DATA.processTaskStepName}】"),
	COMPLETESUBTASK("completesubtask", "完成子任务", "完成子任务【${DATA.processTaskStepName}】"),
//	COMMENTSUBTASK("commentsubtask", "回复子任务", "回复子任务【${DATA.processTaskStepName}】"),
	START("start", "开始", "开始【${DATA.processTaskStepName}】"),
	RETREAT("retreat", "撤回", "撤回【${DATA.processTaskStepName}】"),
	ABORT("abort", "取消", "取消工单"),
	RECOVER("recover", "恢复", "恢复工单"),
	TRANSFER("transfer", "转交", "转交【${DATA.processTaskStepName}】"),
	STARTPROCESS("startprocess", "上报", "提交工单"),
	RESTFULACTION("restfulaction", "RESTFUL动作","【${DATA.processTaskStepName}】"),
	COMPLETE("complete", "流转", "完成【${DATA.processTaskStepName}】"),
	BACK("back", "回退", "完成【${DATA.processTaskStepName}】至【${DATA.nextStepName}】"),
	;
	private String value;
	private String text;
	private String description;
	
	private ProcessTaskAuditType(String value, String text, String description) {
		this.value = value;
		this.text = text;
		this.description = description;
		IProcessTaskAuditType.AUDIT_TYPE_LIST.add(this);
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
	
	public static String getDescription(String _value) {
		for(ProcessTaskAuditType type : values()) {
			if(type.getValue().equals(_value)) {
				return type.getDescription();
			}
		}
		return "";
	}
}
