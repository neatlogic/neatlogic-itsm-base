package codedriver.framework.process.exception.channeltype;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @Title: ChannelTypeRelationHasReferenceException
 * @Package codedriver.framework.process.exception.channeltype
 * @Description: TODO
 * @Author: linbq
 * @Date: 2021/1/31 15:34
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public class ChannelTypeRelationHasReferenceException extends ApiRuntimeException {

    private static final long serialVersionUID = 2411827472270844447L;

    public ChannelTypeRelationHasReferenceException(String name) {
        super("服务类型关系：'" + name + "'已被引用，不能修改");
    }
}
