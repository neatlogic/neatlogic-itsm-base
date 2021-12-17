package codedriver.framework.process.exception.event;

import codedriver.framework.exception.core.ApiRuntimeException;

public class EventTypeNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = 4304560151882499635L;

	public EventTypeNameRepeatException() {
		super("同一个层级下不能出现重名的类型");
	}

}
