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
