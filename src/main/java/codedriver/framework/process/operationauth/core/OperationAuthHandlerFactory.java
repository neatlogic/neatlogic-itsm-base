package codedriver.framework.process.operationauth.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import codedriver.framework.applicationlistener.core.ApplicationListenerBase;
import codedriver.framework.common.RootComponent;

/**
 * 
 * @Author:chenqiwei
 * @Time:Jun 20, 2020
 * @ClassName: ProcessOperateHandlerFactory
 * @Description: 操作处理器工厂，根据当前节点状态返回操作列表，控制前端显示的操作按钮
 */
@RootComponent
public class OperationAuthHandlerFactory extends ApplicationListenerBase {
	private static Map<IOperationAuthHandlerType, IOperationAuthHandler> componentMap = new HashMap<>();

	public static IOperationAuthHandler getHandler(IOperationAuthHandlerType handler) {
		return componentMap.get(handler);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
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
