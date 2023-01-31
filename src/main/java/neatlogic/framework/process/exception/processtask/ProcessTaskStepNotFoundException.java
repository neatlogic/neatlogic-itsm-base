package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186121675355812447L;

	public ProcessTaskStepNotFoundException(String processTaskStep) {
		super("流程步骤：'" + processTaskStep + "'不存在");
	}

	public ProcessTaskStepNotFoundException(Long processTaskStep) {
		super("流程步骤：'" + processTaskStep + "'不存在");
	}
}
