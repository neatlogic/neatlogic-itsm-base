package neatlogic.framework.process.exception.channeltype;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ChannelTypeNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = 1729102432983052302L;

	public ChannelTypeNameRepeatException(String name) {
		super("服务类型：'" + name + "'已存在");
	}
}
