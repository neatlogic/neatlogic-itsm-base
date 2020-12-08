package codedriver.framework.process.workerpolicy.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import codedriver.framework.applicationlistener.core.ApplicationListenerBase;
import codedriver.framework.common.RootComponent;
import codedriver.framework.process.dto.WorkerPolicyVo;

@RootComponent
public class WorkerPolicyHandlerFactory extends ApplicationListenerBase {

	private static Map<String, IWorkerPolicyHandler> componentMap = new HashMap<String, IWorkerPolicyHandler>();

	private static List<WorkerPolicyVo> workerPolicyList = new ArrayList<>();
	
	public static IWorkerPolicyHandler getHandler(String name) {
		if (!componentMap.containsKey(name) || componentMap.get(name) == null) {
			throw new RuntimeException("找不到类型为：" + name + "的处理人分配策略");
		}
		return componentMap.get(name);
	}

	public static List<WorkerPolicyVo> getAllActiveWorkerPolicy(){
		return workerPolicyList;
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		Map<String, IWorkerPolicyHandler> myMap = context.getBeansOfType(IWorkerPolicyHandler.class);
		for (Map.Entry<String, IWorkerPolicyHandler> entry : myMap.entrySet()) {
			IWorkerPolicyHandler component = entry.getValue();
			if (component.getType() != null) {
				componentMap.put(component.getType(), component);
				WorkerPolicyVo workerPolicy = new WorkerPolicyVo();
				workerPolicy.setType(component.getType());
				workerPolicy.setName(component.getName());
				workerPolicy.setModuleId(context.getId());
				workerPolicyList.add(workerPolicy);
			}
		}
	}

    @Override
    protected void myInit() {
        
    }
}
