package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepCommentNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186121675355812447L;

	public ProcessTaskStepCommentNotFoundException(String id) {
		super("步骤回复：“{0}”不存在", id);
	}
}
