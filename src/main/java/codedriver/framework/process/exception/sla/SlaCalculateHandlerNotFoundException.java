/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.sla;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author linbq
 * @since 2021/11/22 14:28
 **/
public class SlaCalculateHandlerNotFoundException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -6956721990886391194L;

    public SlaCalculateHandlerNotFoundException(String handler) {
        super("sla计算规则处理器：'" + handler + "'不存在");
    }
}
