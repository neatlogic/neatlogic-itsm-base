package neatlogic.framework.process.exception.process;

import neatlogic.framework.exception.core.NotFoundEditTargetException;

public class ProcessNotFoundEditTargetException extends NotFoundEditTargetException {

	private static final long serialVersionUID = 2639465731103184229L;

	public ProcessNotFoundEditTargetException(String uuid) {
		super("nfpep.processnotfoundedittargetexception.processnotfoundedittargetexception", uuid);
	}
}
