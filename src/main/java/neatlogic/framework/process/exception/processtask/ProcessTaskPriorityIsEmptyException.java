package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskPriorityIsEmptyException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186122774455812447L;

	public ProcessTaskPriorityIsEmptyException() {
		super("exception.process.processtaskpriorityisemptyexception");
	}
}
