/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.operationauth.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:chenqiwei
 * @Time:Jun 20, 2020
 * @ClassName: ProcessOperateHandlerFactory
 * @Description: 操作处理器工厂，根据当前节点状态返回操作列表，控制前端显示的操作按钮
 */
@RootComponent
public class OperationAuthHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IOperationAuthHandler> componentMap = new HashMap<>();

    public static IOperationAuthHandler getHandler(String handler) {
        return componentMap.get(handler);
    }

    @Override
    public void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IOperationAuthHandler> myMap = context.getBeansOfType(IOperationAuthHandler.class);
        for (Map.Entry<String, IOperationAuthHandler> entry : myMap.entrySet()) {
            IOperationAuthHandler component = entry.getValue();
            if (component.getHandler() != null) {
                if (componentMap.containsKey(component.getHandler())) {
                    throw new RuntimeException("操作处理器：" + component.getHandler() + "已存在，请修改代码");
                }
                componentMap.put(component.getHandler(), component);
            }
        }
    }

    @Override
    protected void myInit() {

    }
}
