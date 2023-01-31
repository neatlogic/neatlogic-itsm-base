package neatlogic.framework.process.exception.process;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = -4617724920030245149L;

	public ProcessNameRepeatException(String msg) {
		super("流程图：'" + msg + "'已存在");
	}
}
