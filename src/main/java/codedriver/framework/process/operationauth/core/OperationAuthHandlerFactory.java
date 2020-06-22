package codedriver.framework.process.operationauth.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import codedriver.framework.common.RootComponent;

/**
 * 
 * @Author:chenqiwei
 * @Time:Jun 20, 2020
 * @ClassName: ProcessOperateHandlerFactory
 * @Description: 操作处理器工厂，根据当前节点状态返回操作列表，控制前端显示的操作按钮
 */
@RootComponent
public class OperationAuthHandlerFactory implements ApplicationListener<ContextRefreshedEvent> {
	private static Map<IOperationAuthHandlerType, OperationAuthHandlerBase> componentMap = new HashMap<>();

	public static OperationAuthHandlerBase getHandler(IOperationAuthHandlerType handler) {
		return componentMap.get(handler);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		Map<String, OperationAuthHandlerBase> myMap = context.getBeansOfType(OperationAuthHandlerBase.class);
		for (Map.Entry<String, OperationAuthHandlerBase> entry : myMap.entrySet()) {
			OperationAuthHandlerBase component = entry.getValue();
			if (component.getHandler() != null) {
				if (componentMap.containsKey(component.getHandler())) {
					throw new RuntimeException("操作处理器：" + component.getHandler() + "已存在，请修改代码");
				}
				componentMap.put(component.getHandler(), component);
			}
		}
	}
}
