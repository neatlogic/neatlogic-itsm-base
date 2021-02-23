package codedriver.framework.process.exception.process;

import codedriver.framework.exception.core.ApiFieldValidRuntimeException;

public class ProcessNameRepeatException extends ApiFieldValidRuntimeException {

	private static final long serialVersionUID = -4617724920030245149L;

	public ProcessNameRepeatException(String msg) {
		super("流程图：'" + msg + "'已存在");
	}
}
