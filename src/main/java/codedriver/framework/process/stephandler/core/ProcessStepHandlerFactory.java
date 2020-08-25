package codedriver.framework.process.stephandler.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.common.RootComponent;
import codedriver.framework.dto.ModuleVo;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.dto.ProcessStepHandlerVo;

@RootComponent
@Order(10)
public class ProcessStepHandlerFactory implements ApplicationListener<ContextRefreshedEvent> {
	private static Map<String, IProcessStepHandler> componentMap = new HashMap<String, IProcessStepHandler>();
	private static List<ProcessStepHandlerVo> processStepHandlerList = new ArrayList<>();

	// @PostConstruct
	// public void init() {
	// processStepHandlerMapper.resetProcessStepHandler();
	// }

	public static IProcessStepHandler getHandler(String handler) {
//		if (!componentMap.containsKey(handler) || componentMap.get(handler) == null) {
//			throw new ProcessStepHandlerNotFoundException(handler);
//		}
		return componentMap.get(handler);
	}

	public static IProcessStepHandler getHandler() {
		/** 随便返回一个handler，主要用来处理作业级操作 **/
		return componentMap.values().iterator().next();
	}

	public static List<ProcessStepHandlerVo> getActiveProcessStepHandler() {
		TenantContext tenantContext = TenantContext.get();
		List<ModuleVo> moduleList = tenantContext.getActiveModuleList();
		List<ProcessStepHandlerVo> returnProcessStepHandlerList = new ArrayList<>();
		for (ProcessStepHandlerVo processStepHandler : processStepHandlerList) {
			//开始和结束组件不用返回给前端
			if(processStepHandler.getType().equals(ProcessStepType.END.getValue()) || processStepHandler.getType().equals(ProcessStepType.START.getValue())) {
				continue;
			}
			for (ModuleVo moduleVo : moduleList) {
				if (moduleVo.getId().equalsIgnoreCase(processStepHandler.getModuleId())) {
					returnProcessStepHandlerList.add(processStepHandler);
					break;
				}
			}
		}
		return returnProcessStepHandlerList.stream().sorted(Comparator.comparing(ProcessStepHandlerVo::getSort)).collect(Collectors.toList());
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		Map<String, IProcessStepHandler> myMap = context.getBeansOfType(IProcessStepHandler.class);
		for (Map.Entry<String, IProcessStepHandler> entry : myMap.entrySet()) {
			IProcessStepHandler component = entry.getValue();
			if (component.getHandler() != null) {
				componentMap.put(component.getHandler(), component);
				ProcessStepHandlerVo processStepHandlerVo = new ProcessStepHandlerVo();
				processStepHandlerVo.setType(component.getType());
				processStepHandlerVo.setHandler(component.getHandler());
				processStepHandlerVo.setName(component.getName());
				processStepHandlerVo.setSort(component.getSort());
				processStepHandlerVo.setChartConfig(component.getChartConfig());
				processStepHandlerVo.setModuleId(context.getId());
				processStepHandlerVo.setIsActive(1);
				processStepHandlerVo.setIsAllowStart((component.isAllowStart()!=null&&component.isAllowStart())?1:0);
				processStepHandlerList.add(processStepHandlerVo);
			}
		}
	}
}
