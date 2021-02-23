package codedriver.framework.process.exception.channel;

import codedriver.framework.exception.core.ApiFieldValidRuntimeException;

public class ChannelNameRepeatException extends ApiFieldValidRuntimeException {

	private static final long serialVersionUID = -4617724920030245143L;

	public ChannelNameRepeatException(String msg) {
		super("服务通道：'" + msg + "'已存在");
	}
}
