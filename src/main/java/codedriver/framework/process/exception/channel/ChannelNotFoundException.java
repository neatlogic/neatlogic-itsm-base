/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.channel;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ChannelNotFoundException extends ApiRuntimeException {

    private static final long serialVersionUID = -5334268232696017057L;

    public ChannelNotFoundException(String channelUuid) {
        super("服务：'" + channelUuid + "'不存在");
    }
}
