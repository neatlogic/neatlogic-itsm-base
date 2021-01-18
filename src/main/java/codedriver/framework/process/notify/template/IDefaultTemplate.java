package codedriver.framework.process.notify.template;

import codedriver.framework.process.constvalue.ProcessTaskParams;
import org.springframework.util.ClassUtils;

import codedriver.framework.notify.core.NotifyHandlerType;
import codedriver.framework.process.notify.core.NotifyDefaultTemplateFactory;
import codedriver.framework.process.notify.core.TaskStepNotifyTriggerType;

public interface IDefaultTemplate {
	
	String DEFAULT_TEMPLATE_TYPE = "默认";
	
	String DEFAULT_TEMPLATE_UUID_PREFIX = "default_";

	String OPERATOR = "【" + ProcessTaskParams.OPERATOR.getFreemarkerTemplate() + "】";
	String PROCESSTASK_STEP_NAME = "【" + ProcessTaskParams.STEPNAME.getFreemarkerTemplate() + "】";
	String PROCESSTASK_STEP_WORKER = "【" + ProcessTaskParams.STEPWORKER.getFreemarkerTemplate() + "】";
	String REASON = "【" + ProcessTaskParams.REASON.getFreemarkerTemplate() + "】";
	String SUBTASK_CONTENT = "【" + ProcessTaskParams.SUBTASKCONTENT.getFreemarkerTemplate() + "】";
	String SUBTASK_WORKER = "【" + ProcessTaskParams.SUBTASKWORKER.getFreemarkerTemplate() + "】";
	String CHANGE_STEP_NAME = "【" + ProcessTaskParams.CHANGESTEPNAME.getFreemarkerTemplate() + "】";
	String CHANGE_STEP_WORKER = "【" + ProcessTaskParams.CHANGESTEPWORKER.getFreemarkerTemplate() + "】";

	String PROCESSTASK_DETAILS_URL = "${homeUrl}/process.html#/task-detail?processTaskId=${DATA.id}";
	String PROCESSTASK_ID_TITLE = "【" + ProcessTaskParams.ID.getFreemarkerTemplate() + "-" + ProcessTaskParams.TITLE.getFreemarkerTemplate() + "】";
	String PROCESSTASK_DETAILS_LINK = "查看详情：<a href=" + PROCESSTASK_DETAILS_URL + "><b>【工单链接】</b></a>";
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
