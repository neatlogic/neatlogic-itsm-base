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

package neatlogic.framework.process.datacube;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class DataCubeFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IDataCubeHandler> componentMap = new HashMap<String, IDataCubeHandler>();

    public static IDataCubeHandler getComponent(String type) {
        if (!componentMap.containsKey(type) || componentMap.get(type) == null) {
            throw new RuntimeException("找不到类型为：" + type + "的流程组件");
        }
        return componentMap.get(type);
    }

    @Override
    public void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IDataCubeHandler> myMap = context.getBeansOfType(IDataCubeHandler.class);
        for (Map.Entry<String, IDataCubeHandler> entry : myMap.entrySet()) {
            IDataCubeHandler component = entry.getValue();
            if (component.getType() != null) {
                componentMap.put(component.getType(), component);
            }
        }
    }

    @Override
    protected void myInit() {
        // TODO Auto-generated method stub

    }
}
