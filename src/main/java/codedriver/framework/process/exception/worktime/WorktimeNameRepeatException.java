package codedriver.framework.process.exception.worktime;

import codedriver.framework.exception.core.ApiRuntimeException;

public class WorktimeNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = -3658559078633543316L;

	public WorktimeNameRepeatException(String msg) {
		super("服务窗口：'" + msg + "'已存在");
	}
}
