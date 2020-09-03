package codedriver.framework.process.notify.core;

import codedriver.framework.notify.core.INotifyTriggerType;

public enum NotifyTriggerType implements INotifyTriggerType {
	ACTIVE("active", "步骤激活", "流程步骤已激活", "流程步骤已激活"),
	ASSIGN("assign", "步骤分配处理人", "流程步骤已分配", "流程步骤已分配"),
	START("start", "步骤开始", "流程步骤已开始", "流程步骤已开始"),
	TRANSFER("transfer", "步骤转交", "流程步骤已转交", "流程步骤已转交"),
	URGE("urge", "步骤催办", "催办标题", "催办内容"),
	SUCCEED("succeed", "步骤成功", "流程步骤已成功", "流程步骤已成功"),
	BACK("back", "步骤回退", "回退标题", "回退内容"),
	RETREAT("retreat", "步骤撤回", "撤回标题", "撤回内容"),
	HANG("hang", "步骤挂起", "流程步骤已挂起", "流程步骤已挂起"),
	ABORT("abort", "步骤终止", "流程步骤已终止", "流程步骤已终止"),
	RECOVER("recover", "步骤恢复", "流程步骤已恢复", "流程步骤已恢复"),
	PAUSE("pause", "步骤暂停", "流程步骤已暂停", "流程步骤已暂停"),
	FAILED("failed", "步骤失败", "流程步骤已失败", "流程步骤已失败"),
	//ACCEPT("accept", "接管", "流程步骤接管", "流程步骤接管"),
	TIMEOUT("timeout", "超时", "超时标题", "超时内容"),
	//STARTPROCESS("startprocess", "上报", "", ""),
	//UPDATE("update", "工单更新", "", ""),
	CREATESUBTASK("createsubtask", "子任务创建", "", ""),
	EDITSUBTASK("editsubtask", "子任务编辑", "", ""),
	ABORTSUBTASK("abortsubtask", "子任务取消", "", ""),
	REDOSUBTASK("redosubtask", "子任务打回重做", "", ""),
	COMPLETESUBTASK("completesubtask", "子任务完成", "", "");

	private String trigger;
	private String text;
	private String titleTemplate;
	private String contentTemplate;

	private NotifyTriggerType(String _trigger, String _text, String _titleTemplate, String _contentTemplate) {
		this.trigger = _trigger;
		this.text = _text;
		this.titleTemplate = _titleTemplate;
		this.contentTemplate = _contentTemplate;
	}

	public String getTitleTemplate() {
		return titleTemplate;
	}

	public String getContentTemplate() {
		return contentTemplate;
	}
	@Override
	public String getTrigger() {
		return trigger;
	}
	@Override
	public String getText() {
		return text;
	}

	public static String getTitleTemplate(String trigger) {
		for (NotifyTriggerType s : NotifyTriggerType.values()) {
			if (s.getTrigger().equals(trigger)) {
				return s.getTitleTemplate();
			}
		}
		return "";
	}

	public static String getContentTemplate(String trigger) {
		for (NotifyTriggerType s : NotifyTriggerType.values()) {
			if (s.getTrigger().equals(trigger)) {
				return s.getContentTemplate();
			}
		}
		return "";
	}
	
	public static String getText(String trigger) {
		for(NotifyTriggerType n : NotifyTriggerType.values()) {
			if(n.getTrigger().equals(trigger)) {
				return n.getText();
			}
		}
		return "";
	}
}
