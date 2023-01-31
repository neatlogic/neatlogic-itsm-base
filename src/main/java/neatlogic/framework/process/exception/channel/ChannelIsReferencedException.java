/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.channel;

import neatlogic.framework.exception.core.ApiRuntimeException;

/**
 * @author: linbq
 * @since: 2021/3/30 12:12
 **/
public class ChannelIsReferencedException extends ApiRuntimeException {

    private static final long serialVersionUID = 291889732332376765L;

    public ChannelIsReferencedException(String channelName){
        super("服务：'" + channelName + "'已被引用");
    }
}
