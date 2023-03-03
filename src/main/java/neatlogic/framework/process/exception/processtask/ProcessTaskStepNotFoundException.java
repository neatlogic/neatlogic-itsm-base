package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186121675355812447L;

	public ProcessTaskStepNotFoundException(String processTaskStep) {
		super("exception.process.processtaskstepnotfoundexception", processTaskStep);
	}

	public ProcessTaskStepNotFoundException(Long processTaskStep) {
		super("exception.process.processtaskstepnotfoundexception", processTaskStep);
	}
}
