/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.bootstrap.CodedriverWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.dto.module.ModuleVo;
import neatlogic.framework.process.constvalue.ProcessStepType;
import neatlogic.framework.process.dto.ProcessStepHandlerVo;
import org.springframework.core.annotation.Order;

import java.util.*;
import java.util.stream.Collectors;

@RootComponent
@Order(10)
public class ProcessStepHandlerFactory extends ModuleInitializedListenerBase {
	private static final Map<String, IProcessStepHandler> componentMap = new HashMap<>();
	private static final List<ProcessStepHandlerVo> processStepHandlerList = new ArrayList<>();

	public static IProcessStepHandler getHandler(String handler) {
//		if (!componentMap.containsKey(handler) || componentMap.get(handler) == null) {
//			throw new ProcessStepHandlerNotFoundException(handler);
//		}
		return componentMap.get(handler);
	}

	public static IProcessStepHandler getHandler() {
		/* 随便返回一个handler，主要用来处理作业级操作 **/
		return componentMap.values().iterator().next();
	}

	public static List<IProcessStepHandler> getHandlerList(){
		return new ArrayList<>(componentMap.values());
	}

	public static List<ProcessStepHandlerVo> getActiveProcessStepHandler() {
		TenantContext tenantContext = TenantContext.get();
		List<ModuleVo> moduleList = tenantContext.getActiveModuleList();
		List<ProcessStepHandlerVo> returnProcessStepHandlerList = new ArrayList<>();
		for (ProcessStepHandlerVo processStepHandler : processStepHandlerList) {
			//开始组件不用返回给前端
			if(processStepHandler.getType().equals(ProcessStepType.START.getValue())) {
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
	public void onInitialized(CodedriverWebApplicationContext context) {
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
				processStepHandlerVo.setForwardInputQuantity(component.getForwardInputQuantity());
				processStepHandlerVo.setForwardOutputQuantity(component.getForwardOutnputQuantity());
				processStepHandlerVo.setBackwardInputQuantity(component.getBackwardInputQuantity());
				processStepHandlerVo.setBackwardOutputQuantity(component.getBackwardOutputQuantity());
				processStepHandlerList.add(processStepHandlerVo);
			}
		}
	}

    @Override
    protected void myInit() {
        
    }
}
