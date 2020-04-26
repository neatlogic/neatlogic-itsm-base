package codedriver.framework.process.notify.template;

import org.springframework.util.ClassUtils;

import codedriver.framework.process.notify.core.NotifyDefaultTemplateFactory;
import codedriver.framework.process.notify.core.NotifyHandlerType;
import codedriver.framework.process.notify.core.NotifyTriggerType;

public interface IDefaultTemplate {
	public default String getUuid() {
		return NotifyDefaultTemplateFactory.DEFAULT_TEMPLATE_UUID_PREFIX + NotifyDefaultTemplateFactory.nextNum();
	}
	public default String getName() {
		return NotifyHandlerType.getText(getNotifyHandlerType())+ "_" + NotifyTriggerType.getText(getTrigger());
	}
	public default String getType() {
		return NotifyDefaultTemplateFactory.DEFAULT_TEMPLATE_TYPE;
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
