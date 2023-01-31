package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskTitleIsEmptyException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186122774455812447L;

	public ProcessTaskTitleIsEmptyException() {
		super("工单标题不能为空");
	}
}
