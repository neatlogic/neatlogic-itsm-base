package neatlogic.framework.process.exception.channeltype;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ChannelTypeNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = 1518460455913935928L;

	public ChannelTypeNotFoundException(String uuid) {
		super("服务类型：“{0}”不存在", uuid);
	}
}
