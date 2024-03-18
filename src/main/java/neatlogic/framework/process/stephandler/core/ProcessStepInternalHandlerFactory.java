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
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

@RootComponent
@Order(10)
public class ProcessStepInternalHandlerFactory extends ModuleInitializedListenerBase {
	private static Map<String, IProcessStepInternalHandler> componentMap = new HashMap<String, IProcessStepInternalHandler>();

	public static IProcessStepInternalHandler getHandler(String handler) {
		return componentMap.get(handler);
	}

	public static IProcessStepInternalHandler getHandler() {
		/* 随便返回一个handler，主要用来处理作业级操作 **/
		return componentMap.values().iterator().next();
	}

	@Override
	public void onInitialized(NeatLogicWebApplicationContext context) {
		Map<String, IProcessStepInternalHandler> myMap = context.getBeansOfType(IProcessStepInternalHandler.class);
		for (Map.Entry<String, IProcessStepInternalHandler> entry : myMap.entrySet()) {
			IProcessStepInternalHandler component = entry.getValue();
			if (component.getHandler() != null) {
				componentMap.put(component.getHandler(), component);
			}
		}
	}

    @Override
    protected void myInit() {
        
    }
}
