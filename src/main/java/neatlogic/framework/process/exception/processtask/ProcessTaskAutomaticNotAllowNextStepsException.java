package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskAutomaticNotAllowNextStepsException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -7145916738483615561L;

	public ProcessTaskAutomaticNotAllowNextStepsException(){
		super("自动化处理步骤只允许存在一个流转/回退步骤");
	}
	
}
