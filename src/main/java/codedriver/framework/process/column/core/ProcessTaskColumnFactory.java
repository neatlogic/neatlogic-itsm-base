package codedriver.framework.process.column.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import codedriver.framework.applicationlistener.core.ApplicationListenerBase;
import codedriver.framework.common.RootComponent;

@RootComponent
public class ProcessTaskColumnFactory extends ApplicationListenerBase{

	public static Map<String, IProcessTaskColumn> columnComponentMap = new HashMap<>();
	
	public static IProcessTaskColumn getHandler(String name) {
		return columnComponentMap.get(name);
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		Map<String, IProcessTaskColumn> myMap = context.getBeansOfType(IProcessTaskColumn.class);
		for (Map.Entry<String, IProcessTaskColumn> entry : myMap.entrySet()) {
			IProcessTaskColumn column= entry.getValue();
			columnComponentMap.put(column.getName(), column);
		}
	}

	@Override
	protected void myInit() {
		// TODO Auto-generated method stub
		
	}

}
