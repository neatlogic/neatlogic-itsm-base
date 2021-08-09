package codedriver.framework.process.exception.processtask;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskPriorityIsEmptyException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186122774455812447L;

	public ProcessTaskPriorityIsEmptyException() {
		super("工单优先级不能为空");
	}
}
