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

package neatlogic.framework.process.workerpolicy.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.dto.WorkerPolicyVo;
import neatlogic.framework.process.exception.workcenter.ProcessorAllocationPolicyTypeNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RootComponent
public class WorkerPolicyHandlerFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IWorkerPolicyHandler> componentMap = new HashMap<String, IWorkerPolicyHandler>();

    private static final List<WorkerPolicyVo> workerPolicyList = new ArrayList<>();

    public static IWorkerPolicyHandler getHandler(String name) {
        if (!componentMap.containsKey(name) || componentMap.get(name) == null) {
            throw new ProcessorAllocationPolicyTypeNotFoundException(name);
        }
        return componentMap.get(name);
    }

    public static List<WorkerPolicyVo> getAllActiveWorkerPolicy() {
        return workerPolicyList;
    }

    @Override
    public void onInitialized(NeatLogicWebApplicationContext context) {
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
