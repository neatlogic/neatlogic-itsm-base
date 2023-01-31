package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepUnActivedException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -5334268232696017057L;

	public ProcessTaskStepUnActivedException() {
		super("当前步骤未激活");
	}
}
