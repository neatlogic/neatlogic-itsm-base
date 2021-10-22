/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.channeltype;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ChannelTypeRelationNameRepeatException extends ApiRuntimeException {

    private static final long serialVersionUID = -5875667147910092453L;
    
    public ChannelTypeRelationNameRepeatException(String name) {
        super("服务类型关系“" + name + "”已存在");
    }

}
