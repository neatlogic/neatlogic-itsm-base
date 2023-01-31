/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.CodedriverWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

@RootComponent
@Order(10)
public class ProcessStepInternalHandlerFactory extends ModuleInitializedListenerBase {
	private static Map<String, IProcessStepInternalHandler> componentMap = new HashMap<String, IProcessStepInternalHandler>();

	public static IProcessStepInternalHandler getHandler(String handler) {
		return componentMap.get(handler);
	}

	public static IProcessStepInternalHandler getHandler() {
		/* 随便返回一个handler，主要用来处理作业级操作 **/
		return componentMap.values().iterator().next();
	}

	@Override
	public void onInitialized(CodedriverWebApplicationContext context) {
		Map<String, IProcessStepInternalHandler> myMap = context.getBeansOfType(IProcessStepInternalHandler.class);
		for (Map.Entry<String, IProcessStepInternalHandler> entry : myMap.entrySet()) {
			IProcessStepInternalHandler component = entry.getValue();
			if (component.getHandler() != null) {
				componentMap.put(component.getHandler(), component);
			}
		}
	}

    @Override
    protected void myInit() {
        
    }
}
