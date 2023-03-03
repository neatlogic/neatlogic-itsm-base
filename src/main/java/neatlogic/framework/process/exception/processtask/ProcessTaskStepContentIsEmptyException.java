package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepContentIsEmptyException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186122673355812447L;

	public ProcessTaskStepContentIsEmptyException() {
		super("exception.process.processtaskstepcontentisemptyexception");
	}
}
