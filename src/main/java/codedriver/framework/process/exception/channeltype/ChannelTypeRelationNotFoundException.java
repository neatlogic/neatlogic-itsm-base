package codedriver.framework.process.exception.channeltype;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ChannelTypeRelationNotFoundException extends ApiRuntimeException {

    private static final long serialVersionUID = 2411927472270844447L;

    public ChannelTypeRelationNotFoundException(Long id) {
        super("服务类型关系：'" + id + "'不存在");
    }
}
