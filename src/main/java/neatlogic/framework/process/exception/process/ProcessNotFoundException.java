package neatlogic.framework.process.exception.process;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessNotFoundException extends ApiRuntimeException {
	
	private static final long serialVersionUID = 2639465731103184228L;

	public ProcessNotFoundException(String uuid) {
		super("流程：“{0}”不存在", uuid);
	}
}
