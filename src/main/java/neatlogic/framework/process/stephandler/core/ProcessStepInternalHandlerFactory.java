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