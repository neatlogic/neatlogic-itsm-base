package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

@Deprecated
public class ProcessTaskStepSubtaskNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 506247933305553211L;

	public ProcessTaskStepSubtaskNotFoundException(String processTaskStepSubtaskId) {
		super("工单步骤子任务：'" + processTaskStepSubtaskId + "'不存在");
	}
}
