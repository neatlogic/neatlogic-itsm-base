package codedriver.framework.process.exception.priority;

import codedriver.framework.exception.core.ApiFieldValidRuntimeException;

public class PriorityNameRepeatException extends ApiFieldValidRuntimeException {

	private static final long serialVersionUID = -6084335002142350454L;

	public PriorityNameRepeatException(String name) {
		super("优先级：'" + name + "'已存在");
	}
}
