package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskTitleIsEmptyException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186122774455812447L;

	public ProcessTaskTitleIsEmptyException() {
		super("exception.process.processtasktitleisemptyexception");
	}
}
