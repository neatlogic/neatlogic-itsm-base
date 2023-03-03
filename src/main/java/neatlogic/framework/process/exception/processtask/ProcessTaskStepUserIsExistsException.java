package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepUserIsExistsException extends ProcessTaskRuntimeException {
	private static final long serialVersionUID = 7077952789160608694L;

	public ProcessTaskStepUserIsExistsException(String userName) {
		super("exception.process.processtaskstepuserisexistsexception", userName);
	}
}
