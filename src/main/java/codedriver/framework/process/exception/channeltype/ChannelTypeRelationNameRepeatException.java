package codedriver.framework.process.exception.channeltype;

import codedriver.framework.exception.core.ApiFieldValidRuntimeException;

public class ChannelTypeRelationNameRepeatException extends ApiFieldValidRuntimeException {

    private static final long serialVersionUID = -5875667147910092453L;
    
    public ChannelTypeRelationNameRepeatException(String name) {
        super("服务类型关系：'" + name + "'已存在");
    }

}
