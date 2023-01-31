/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.channel;

import neatlogic.framework.exception.core.ApiRuntimeException;

/**
 * @author: linbq
 * @since: 2021/3/30 14:39
 **/
public class ChannelParentUuidCannotBeZeroException extends ApiRuntimeException {

    private static final long serialVersionUID = 1747171456084633383L;

    public ChannelParentUuidCannotBeZeroException(){
        super("服务的parentUuid不能为零");
    }
}
