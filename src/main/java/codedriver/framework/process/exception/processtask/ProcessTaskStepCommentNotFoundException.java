package codedriver.framework.process.exception.processtask;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepCommentNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 186121675355812447L;

	public ProcessTaskStepCommentNotFoundException(String id) {
		super("步骤回复：'" + id + "'不存在");
	}
}
