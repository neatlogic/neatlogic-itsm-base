package neatlogic.framework.process.exception.channel;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ChannelRelationSettingException extends ApiRuntimeException {

    private static final long serialVersionUID = 291889722332376765L;

    public ChannelRelationSettingException() {
        super("服务允许转报后，转报设置必填");
    }
}
