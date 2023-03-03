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

package neatlogic.framework.process.workerpolicy.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.dto.WorkerPolicyVo;

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
            throw new RuntimeException("找不到类型为：" + name + "的处理人分配策略");
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
