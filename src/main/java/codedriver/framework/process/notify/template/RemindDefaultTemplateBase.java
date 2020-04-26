package codedriver.framework.process.notify.template;

import codedriver.framework.process.notify.core.NotifyHandlerType;

public abstract class RemindDefaultTemplateBase implements IDefaultTemplate {
	@Override
	public String getNotifyHandlerType() {
		return NotifyHandlerType.REMIND.getValue();
	}
	
	public static class Urge extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return new StringBuilder(39)//标题
					.append("【ITSM服务单催办】【${task.id}】-【${task.title}】")
					.toString();
		}

		@Override
		public String getContent() {
			return new StringBuilder(307)//内容
					.append("您好，<br>")
					.append("请注意，用户催办<a href=\"${home}task/getTaskStepDetail.do?processTaskId=${task.id}&processTaskStepId=${step.id}\"><b>【${task.id}】：【${task.title}】</b></a><br><br>")
					.append("请<a href=\"${home}process.html#/task-detail?processTaskId==${task.id}&processTaskStepId=${step.id}\"><b>点击此处</b></a>查看详情，及时派单或处理，并主动告知用户进度，谢谢！<br>")
					.toString();
		}
	}
	
	public static class Abort extends RemindDefaultTemplateBase {

		@Override
		public String getTitle() {
			return new StringBuilder(39)//标题
					.append("【ITSM服务单催办】【${task.id}】-【${task.title}】")
					.toString();
		}

		@Override
		public String getContent() {
			return new StringBuilder(307)//内容
					.append("您好，<br>")
					.append("请注意，用户催办<a href=\"${home}task/getTaskStepDetail.do?processTaskId=${task.id}&processTaskStepId=${step.id}\"><b>【${task.id}】：【${task.title}】</b></a><br><br>")
					.append("请<a href=\"${home}process.html#/task-detail?processTaskId==${task.id}&processTaskStepId=${step.id}\"><b>点击此处</b></a>查看详情，及时派单或处理，并主动告知用户进度，谢谢！<br>")
					.toString();
		}
	}
}
