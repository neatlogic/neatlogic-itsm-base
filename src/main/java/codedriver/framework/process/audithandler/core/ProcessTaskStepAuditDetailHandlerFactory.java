/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.audithandler.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@RootComponent
public class ProcessTaskStepAuditDetailHandlerFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskStepAuditDetailHandler> handlerMap = new HashMap<>();

    public static IProcessTaskStepAuditDetailHandler getHandler(String type) {
        IProcessTaskStepAuditDetailHandler handler = handlerMap.get(type);
        return handler;
    }

    @Override
    public void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IProcessTaskStepAuditDetailHandler> map = context.getBeansOfType(IProcessTaskStepAuditDetailHandler.class);
        for (Entry<String, IProcessTaskStepAuditDetailHandler> entry : map.entrySet()) {
            IProcessTaskStepAuditDetailHandler handler = entry.getValue();
            handlerMap.put(handler.getType(), handler);
        }
    }

    @Override
    protected void myInit() {

    }

}
