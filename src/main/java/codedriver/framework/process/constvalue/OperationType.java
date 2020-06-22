package codedriver.framework.process.constvalue;

public enum OperationType {
	/**
	 * 查看节点信息
	 * 无论该节点的状态如何，只有被授权的对象可以查看活动和步骤中所有与该节点相关的信息，否则不可见；（活动是整个活动不可见，步骤只可见步骤名称和状态，其他内容不可见）
	 * 查看节点信息不会在步骤中记录或生成活动；
	 */
	VIEW("view", "查看节点信息"),
	POCESSTASKVIEW("pocesstaskview","工单查看"),//有服务上报权限才能查看对应工单信息
	STARTPROCESS("startprocess", "上报"),
	START("start", "开始"),
	ACTIVE("active", "激活"),
	COMPLETE("complete", "流转"),//下一步骤列表中有未激活的步骤时，有流转权限
	RETREAT("retreat", "撤回"),
	ACCEPT("accept", "开始"),
	WORK("work","处理"),//可处理步骤权限，有accept、start、complete、startprocess、completesubtask其中一个权限，就会有work权限
	/**
	 * 取消
	 * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
	 * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
	 */
	ABORT("abort", "取消"),
	/**
	 * 恢复
	 * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
	 * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
	 */
	RECOVER("recover", "恢复"),
	/**
	 * 转交
	 * 只有该节点处于‘待处理’或‘进行中’状态时，才允许转交；无论是查看页面还是处理页面，处理人还是非处理人，只要用户有权限转交，便能看到该按钮，否则按钮不显示；
	 * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
	 */
	TRANSFER("transfer", "转交"),
	BACK("back", "回退"),//下一步骤列表中有已激活的步骤时，有回退权限
	SAVE("save", "暂存"),
	/**
	 * 修改上报内容
	 * 被授权的对象可在该步骤处于‘待处理’或‘进行中’时修改上报内容，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
	 * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
	 */
	UPDATE("update", "修改上报内容"),//包括标题、优先级、描述
	UPDATETITLE("updatetitle", "更新标题"),
	UPDATEPRIORITY("updatepriority", "更新优先级"),
	UPDATECONTENT("updatecontent", "更新上报描述内容"),
	COMMENT("comment", "回复"),
	EDITCOMMENT("editcomment", "编辑回复"),
	DELETECOMMENT("deletecomment", "删除回复"),
	CREATESUBTASK("createsubtask", "创建子任务"),
	EDITSUBTASK("editsubtask", "编辑子任务"),
	ABORTSUBTASK("abortsubtask", "取消子任务"),
	REDOSUBTASK("redosubtask", "打回重做子任务"),
	COMPLETESUBTASK("completesubtask", "完成子任务"),
	/**
	 * 只有子任务创建人和处理人有回复子任务权限
	 */
	COMMENTSUBTASK("commentsubtask", "回复子任务"),
	/**
	 * 催办
	 * 被授权的对象可在该步骤处于‘待处理’或‘进行中’时催办，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集，配置的对象都有权限进行催办操作，无论是查看页面还是处理页面，处理人还是非处理人，有权限的用户就能看到该按钮，否则按钮不显示；
	 * 催办时会生成一条活动，并在对应的步骤中记录；如果配置了催办的通知设置，还会根据配置进行通知；
	 */
	URGE("urge","催办"),
	RESTFULACTION("restfulaction", "RESTFUL动作");
	private String status;
	private String text;

	private OperationType(String _status, String _text) {
		this.status = _status;
		this.text = _text;
	}

	public String getValue() {
		return status;
	}

	public String getText() {
		return text;
	}

	public static String getValue(String _status) {
		for (OperationType s : OperationType.values()) {
			if (s.getValue().equals(_status)) {
				return s.getValue();
			}
		}
		return null;
	}

	public static String getText(String _status) {
		for (OperationType s : OperationType.values()) {
			if (s.getValue().equals(_status)) {
				return s.getText();
			}
		}
		return "";
	}
}
