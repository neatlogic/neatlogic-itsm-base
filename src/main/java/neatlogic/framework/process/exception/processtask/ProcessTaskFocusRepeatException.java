package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskFocusRepeatException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 5548450942218275120L;

	public ProcessTaskFocusRepeatException(Long processTaskId) {
		super("exception.process.processtaskfocusrepeatexception", processTaskId);
	}
}
