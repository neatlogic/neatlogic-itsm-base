package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNoPermissionException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -7145916738483615561L;

	public ProcessTaskNoPermissionException(String action){
		super("exception.process.processtasknopermissionexception", action);
	}
	
}
