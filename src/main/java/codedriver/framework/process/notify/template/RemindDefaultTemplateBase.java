package codedriver.framework.process.notify.template;

import codedriver.framework.process.notify.core.NotifyHandlerType;

public abstract class RemindDefaultTemplateBase implements IDefaultTemplate {
	
	@Override
	public String getNotifyHandlerType() {
		return NotifyHandlerType.REMIND.getValue();
	}
	
	public static class Active extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "步骤激活提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "已流转至【${step.name}】；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知步骤激活默认模板";
		}
		
	}
	
	public static class Start extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "处理人响应提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "的【${step.name}】步骤已由处理人【${step.majorUser.userName}】受理；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知步骤激活默认模板";
		}
		
	}
	
	public static class Transfer extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "步骤转交提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "中，【${currentUserName}】已将【${step.name}】步骤转交给" + PROCESSTASK_STEP_MOJOR_OR_WORKERLIST + "处理；<br>")
					.append("原因：【${content}】 ；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知步骤激活默认模板";
		}
		
	}
	
	public static class Urge extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "您收到了一条催办提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "中，【${currentUserName}】发起了一条催办通知，请尽快处理；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知催办默认模板";
		}
	}
	
	public static class Succeed extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "步骤完成提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "的【${step.name}】步骤已处理完成；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知成功默认模板";
		}
	}
	
	public static class Back extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "步骤退回提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "已回退至【${step.name}】；<br>")
					.append("原因：【${content}】 ；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知退回默认模板";
		}
	}
	
	public static class Retreat extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "步骤撤回提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("【${currentUserName}】已将工单" + PROCESSTASK_ID_TITLE + "的【${step.name}】步骤撤回；<br>")
					.append("原因：【${content}】 ；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知撤回默认模板";
		}
	}
	
	public static class Hang extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "步骤挂起提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "中，【${step.name}】步骤已挂起；<br>")
					.append("原因：因【步骤名称】步骤回退导致  ；<br>")//TODO linbq步骤名称有替换成变量
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知挂起默认模板";
		}
	}
	
	public static class Abort extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "取消工单——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("【${currentUser}】取消了工单" + PROCESSTASK_ID_TITLE + "；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知取消默认模板";
		}
	}
	
	public static class Recover extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "恢复工单——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("【${currentUser}】恢复了工单" + PROCESSTASK_ID_TITLE + "；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知恢复默认模板";
		}
	}
	
	public static class Failed extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "步骤失败提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "中，【${step.name}】步骤流转失败；<br>")
					.append("原因：【${step.error}】  ；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知失败默认模板";
		}
	}
	
	public static class CreateSubtask extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "子任务创建提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("工单" + PROCESSTASK_ID_TITLE + "中，【${subtask.ownerName}】为【${step.name}】步骤创建了一个子任务，待【${subtask.userName}】处理；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知子任务创建默认模板";
		}
	}
	
	public static class EditSubtask extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "更新子任务提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("【${subtask.ownerName}】更新了分配给【${subtask.userName}】的任务：<br>")
					.append("【${subtask.content}】；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知更新子任务默认模板";
		}
	}
	
	public static class CompleteSubtask extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "子任务完成提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("【${subtask.userName}】完成了【${subtask.ownerName}】分配的任务：<br>")
					.append("【${subtask.content}】；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知子任务完成默认模板";
		}
	}
	
	public static class RedoSubtask extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "退回子任务提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("【${subtask.ownerName}】退回了分配给【${subtask.userName}】的任务：<br>")
					.append("【${subtask.content}】；<br>")
					.append("原因：【${content}】  ；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知退回子任务默认模板";
		}
	}
	
	public static class AbortSubtask extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return "取消子任务提醒——" + PROCESSTASK_ID_TITLE;
		}

		@Override
		public String getContent() {
			return new StringBuilder()
					.append("【${subtask.ownerName}】取消了分配给【${subtask.userName}】的任务：<br>")
					.append("【${subtask.content}】；<br>")
					.append(PROCESSTASK_DETAILS_LINK)
					.toString();
		}

		@Override
		public String description() {
			return "消息通知取消子任务默认模板";
		}
	}
}
