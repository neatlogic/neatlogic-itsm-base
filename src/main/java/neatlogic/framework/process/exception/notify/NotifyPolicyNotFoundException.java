package neatlogic.framework.process.exception.notify;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class NotifyPolicyNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -1086102274048263211L;

	public NotifyPolicyNotFoundException(String uuid) {
		super("通知策略：'" + uuid + "'不存在");	
	}
}
