package neatlogic.framework.process.exception.process;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessStepHandlerNotFoundException extends ProcessTaskRuntimeException {
	
	private static final long serialVersionUID = -5334268232696017057L;

	public ProcessStepHandlerNotFoundException(String handler) {
		super("exception.process.processstephandlernotfoundexception", handler);
	}
}
