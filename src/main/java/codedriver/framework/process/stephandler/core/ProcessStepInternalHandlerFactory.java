package codedriver.framework.process.stephandler.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;

import codedriver.framework.applicationlistener.core.ApplicationListenerBase;
import codedriver.framework.common.RootComponent;

@RootComponent
@Order(10)
public class ProcessStepInternalHandlerFactory extends ApplicationListenerBase {
	private static Map<String, IProcessStepInternalHandler> componentMap = new HashMap<String, IProcessStepInternalHandler>();

	public static IProcessStepInternalHandler getHandler(String handler) {
		return componentMap.get(handler);
	}

	public static IProcessStepInternalHandler getHandler() {
		/** 随便返回一个handler，主要用来处理作业级操作 **/
		return componentMap.values().iterator().next();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
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
