package neatlogic.framework.process.exception.priority;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class PriorityNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = -6084335002142350454L;

	public PriorityNameRepeatException(String name) {
		super("优先级：'" + name + "'已存在");
	}
}
