package neatlogic.framework.process.exception.process;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessStepUtilHandlerNotFoundException extends ProcessTaskRuntimeException {
	
	private static final long serialVersionUID = -5334268232696017057L;

	public ProcessStepUtilHandlerNotFoundException(String handler) {
		super("exception.process.processsteputilhandlernotfoundexception", handler);
	}
}
