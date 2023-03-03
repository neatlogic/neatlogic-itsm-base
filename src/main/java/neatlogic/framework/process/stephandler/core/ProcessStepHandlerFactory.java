/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
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
	public void onInitialized(NeatLogicWebApplicationContext context) {
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
