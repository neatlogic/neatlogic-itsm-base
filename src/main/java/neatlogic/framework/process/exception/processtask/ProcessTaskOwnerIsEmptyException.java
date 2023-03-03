package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskOwnerIsEmptyException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186122774455812447L;

	public ProcessTaskOwnerIsEmptyException() {
		super("exception.process.processtaskownerisemptyexception");
	}
}
