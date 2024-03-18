/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.dto.module.ModuleVo;
import neatlogic.framework.process.constvalue.ProcessStepType;
import neatlogic.framework.process.dto.ProcessStepHandlerVo;
import neatlogic.framework.util.$;
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

	public static List<ProcessStepHandlerVo> getActiveProcessStepHandler() throws CloneNotSupportedException {
		TenantContext tenantContext = TenantContext.get();
		List<ModuleVo> moduleList = tenantContext.getActiveModuleList();
		List<ProcessStepHandlerVo> returnProcessStepHandlerList = new ArrayList<>();
		for (ProcessStepHandlerVo processStepHandler : processStepHandlerList) {
			ProcessStepHandlerVo processStepHandlerVo = processStepHandler.clone();
			processStepHandlerVo.setName($.t(processStepHandler.getName()));
			//开始组件不用返回给前端
			if(processStepHandlerVo.getType().equals(ProcessStepType.START.getValue())) {
				continue;
			}
			for (ModuleVo moduleVo : moduleList) {
				if (moduleVo.getId().equalsIgnoreCase(processStepHandlerVo.getModuleId())) {
					returnProcessStepHandlerList.add(processStepHandlerVo);
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
				processStepHandlerVo.setForwardOutputQuantity(component.getForwardOutputQuantity());
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
