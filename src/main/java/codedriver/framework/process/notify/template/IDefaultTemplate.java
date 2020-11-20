package codedriver.framework.process.notify.template;

import org.springframework.util.ClassUtils;

import codedriver.framework.notify.core.NotifyHandlerType;
import codedriver.framework.process.notify.core.NotifyDefaultTemplateFactory;
import codedriver.framework.process.notify.core.TaskStepNotifyTriggerType;

public interface IDefaultTemplate {
	
	String DEFAULT_TEMPLATE_TYPE = "默认";
	
	String DEFAULT_TEMPLATE_UUID_PREFIX = "default_";

	String PROCESSTASK_DETAILS_URL = "${DATA.homeUrl}/${DATA.tenant}/process.html#/task-detail?processTaskId=${DATA.task.id}";
	String PROCESSTASK_ID_TITLE = "<a href=" + PROCESSTASK_DETAILS_URL + "><b>【${DATA.task.channelType.prefix}${DATA.task.id}-${task.title}】</b></a>";
	String PROCESSTASK_DETAILS_LINK = "点击查看详情：<a href=" + PROCESSTASK_DETAILS_URL + "><b>【工单链接】</b></a>";
	String PROCESSTASK_STEP_MOJOR_OR_WORKERLIST = "<#if step.majorUser??>【${DATA.step.majorUser.name}】<#else><#if step.workerList?? && step.workerList.size > 0><#list step.workerList as worker>【${DATA.worker.name}】<#if worker_has_next>、</#if></#list></#if></#if>";
	public default Long getId() {
		//return DEFAULT_TEMPLATE_UUID_PREFIX + NotifyDefaultTemplateFactory.nextNum();
		return NotifyDefaultTemplateFactory.nextNum();
	}
	public default String getName() {
		return NotifyHandlerType.getText(getNotifyHandlerType())+ "_" + TaskStepNotifyTriggerType.getText(getTrigger());
	}
	public default String getType() {
		return DEFAULT_TEMPLATE_TYPE;
	}
	public default int getIsReadOnly() {
		return 1;
	}
	public default String getTrigger() {
		return ClassUtils.getUserClass(this.getClass()).getSimpleName().toLowerCase();
	}

	public String getNotifyHandlerType();
	public String getTitle();
	public String getContent();
	public String description();
}
