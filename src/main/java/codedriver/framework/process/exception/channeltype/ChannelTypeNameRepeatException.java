package codedriver.framework.process.exception.channeltype;

import codedriver.framework.exception.core.ApiFieldValidRuntimeException;

public class ChannelTypeNameRepeatException extends ApiFieldValidRuntimeException {

	private static final long serialVersionUID = 1729102432983052302L;

	public ChannelTypeNameRepeatException(String name) {
		super("服务类型：'" + name + "'已存在");
	}
}
