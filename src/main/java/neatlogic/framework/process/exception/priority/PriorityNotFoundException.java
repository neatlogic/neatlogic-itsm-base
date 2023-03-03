package neatlogic.framework.process.exception.priority;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class PriorityNotFoundException extends ApiRuntimeException {
	
	private static final long serialVersionUID = -5334268232696017057L;

	public PriorityNotFoundException(String priorityUuid) {
		super("exception.process.prioritynotfoundexception", priorityUuid);
	}
}
