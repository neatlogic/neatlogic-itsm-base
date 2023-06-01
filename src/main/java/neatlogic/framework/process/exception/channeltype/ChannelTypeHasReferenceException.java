package neatlogic.framework.process.exception.channeltype;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ChannelTypeHasReferenceException extends ApiRuntimeException {

	private static final long serialVersionUID = -1130799055432844626L;

	public ChannelTypeHasReferenceException(String name,String action) {
		super("服务类型：“{0}”已关联服务，不可{1}", name, action);
	}
}
