package codedriver.framework.process.matrixattribute.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import codedriver.framework.applicationlistener.core.ApplicationListenerBase;
import codedriver.framework.common.RootComponent;
import codedriver.framework.process.constvalue.ProcessMatrixAttributeType;

@RootComponent
public class MatrixAttributeHandlerFactory extends ApplicationListenerBase {
	
	private static Map<String, IMatrixAttributeHandler> handlerMap = new HashMap<>();

	public static IMatrixAttributeHandler getHandler(String type) {
		IMatrixAttributeHandler handler = handlerMap.get(type);
		if(handler == null) {
			handler = handlerMap.get(ProcessMatrixAttributeType.FORMINPUT.getValue());
		}
		return handler;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		Map<String, IMatrixAttributeHandler> myMap = context.getBeansOfType(IMatrixAttributeHandler.class);
		for (Map.Entry<String, IMatrixAttributeHandler> entry : myMap.entrySet()) {
			IMatrixAttributeHandler handler = entry.getValue();
			if (handler.getType() != null) {
				handlerMap.put(handler.getType(), handler);
			}
		}
	}

	@Override
	protected void myInit() {
		// TODO Auto-generated method stub
		
	}

}
