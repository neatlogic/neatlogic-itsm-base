package codedriver.framework.process.exception.processtask;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskFocusRepeatException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 5548450942218275120L;

	public ProcessTaskFocusRepeatException(Long processTaskId) {
		super("工单：'" + processTaskId + "'已关注");
	}
}
