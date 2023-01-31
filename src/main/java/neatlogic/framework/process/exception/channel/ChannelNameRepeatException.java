/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.channel;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ChannelNameRepeatException extends ApiRuntimeException {

    private static final long serialVersionUID = -4617724920030245143L;

    public ChannelNameRepeatException(String msg) {
        super("服务：'" + msg + "'已存在");
    }
}
