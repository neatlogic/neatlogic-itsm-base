package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class WorkcenterHandlerNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 8358695524151979636L;

	public WorkcenterHandlerNotFoundException(String handler) {
		super("exception.process.workcenterhandlernotfoundexception", handler);
	}
}
