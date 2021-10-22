/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.channeltype;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ChannelTypeRelationNotFoundException extends ApiRuntimeException {

    private static final long serialVersionUID = 2411927472270844447L;

    public ChannelTypeRelationNotFoundException(Long id) {
        super("服务类型关系“" + id + "”不存在");
    }
}
