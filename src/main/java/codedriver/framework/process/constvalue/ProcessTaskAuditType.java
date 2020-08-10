package codedriver.framework.process.constvalue;

public enum ProcessTaskAuditType implements IProcessTaskAuditType {
	COMMENT("comment", "回复"),
	EDITCOMMENT("editcomment", "编辑回复"),
	DELETECOMMENT("deletecomment", "删除回复"),
	UPDATETITLE("updatetitle", "更新标题"),
	UPDATEPRIORITY("updatepriority", "更新优先级"),
	UPDATECONTENT("updatecontent", "更新上报描述内容"),
	URGE("urge","催办"),
	CREATESUBTASK("createsubtask", "创建子任务"),
	EDITSUBTASK("editsubtask", "编辑"),
	ABORTSUBTASK("abortsubtask", "取消"),
	REDOSUBTASK("redosubtask", "重做"),
	COMPLETESUBTASK("completesubtask", "完成"),
	COMMENTSUBTASK("commentsubtask", "评论"),
	START("start", "开始"),
	RETREAT("retreat", "撤回"),
	ABORT("abort", "取消"),
	RECOVER("recover", "恢复"),
	TRANSFER("transfer", "转交"),
	STARTPROCESS("startprocess", "上报"),
	RESTFULACTION("restfulaction", "RESTFUL动作"),
//	COMPLETE("complete", "流转")
	;
	private String value;
	private String text;
	
	private ProcessTaskAuditType(String value, String text) {
		this.value = value;
		this.text = text;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getText() {
		return text;
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
