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
public class ProcessStepUtilHandlerFactory extends ApplicationListenerBase {
	private static Map<String, IProcessStepUtilHandler> componentMap = new HashMap<String, IProcessStepUtilHandler>();

	public static IProcessStepUtilHandler getHandler(String handler) {
		return componentMap.get(handler);
	}

	public static IProcessStepUtilHandler getHandler() {
		/** 随便返回一个handler，主要用来处理作业级操作 **/
		return componentMap.values().iterator().next();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		Map<String, IProcessStepUtilHandler> myMap = context.getBeansOfType(IProcessStepUtilHandler.class);
		for (Map.Entry<String, IProcessStepUtilHandler> entry : myMap.entrySet()) {
			IProcessStepUtilHandler component = entry.getValue();
			if (component.getHandler() != null) {
				componentMap.put(component.getHandler(), component);
			}
		}
	}

    @Override
    protected void myInit() {
        
    }
}
