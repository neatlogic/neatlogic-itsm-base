/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
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
public class ProcessStepAssistantHandlerFactory extends ModuleInitializedListenerBase {
    private static Map<String, IProcessStepAssistantHandler> componentMap = new HashMap<>();

    public static IProcessStepAssistantHandler getHandler(String handler) {
        return componentMap.get(handler);
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IProcessStepAssistantHandler> myMap = context.getBeansOfType(IProcessStepAssistantHandler.class);
        for (Map.Entry<String, IProcessStepAssistantHandler> entry : myMap.entrySet()) {
            IProcessStepAssistantHandler component = entry.getValue();
            if (component.getHandler() != null) {
                componentMap.put(component.getHandler(), component);
            }
        }
    }

    @Override
    protected void myInit() {

    }
}
