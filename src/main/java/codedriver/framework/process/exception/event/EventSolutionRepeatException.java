package codedriver.framework.process.exception.event;

import codedriver.framework.exception.core.ApiFieldValidRuntimeException;

public class EventSolutionRepeatException extends ApiFieldValidRuntimeException {

	private static final long serialVersionUID = 7578574406227399198L;

	public EventSolutionRepeatException(String name) {
		super("解决方案：" + name + "已存在");
	}


}
