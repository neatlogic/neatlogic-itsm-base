package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskAutomaticNotAllowNextStepsException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -7145916738483615561L;

	public ProcessTaskAutomaticNotAllowNextStepsException(){
		super("exception.process.processtaskautomaticnotallownextstepsexception");
	}
	
}
