package neatlogic.framework.process.exception.priority;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class PriorityNotFoundException extends ApiRuntimeException {
	
	private static final long serialVersionUID = -5334268232696017057L;

	public PriorityNotFoundException(String priorityUuid) {
		super("优先级：“{0}”不存在", priorityUuid);
	}
}
