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

package neatlogic.framework.process.operationauth.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.exception.operationauth.OperationProcessorIsExistsException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:chenqiwei
 * @Time:Jun 20, 2020
 * @ClassName: ProcessOperateHandlerFactory
 * @Description: 操作处理器工厂，根据当前节点状态返回操作列表，控制前端显示的操作按钮
 */
@RootComponent
public class OperationAuthHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IOperationAuthHandler> componentMap = new HashMap<>();

    public static IOperationAuthHandler getHandler(String handler) {
        return componentMap.get(handler);
    }

    @Override
    public void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IOperationAuthHandler> myMap = context.getBeansOfType(IOperationAuthHandler.class);
        for (Map.Entry<String, IOperationAuthHandler> entry : myMap.entrySet()) {
            IOperationAuthHandler component = entry.getValue();
            if (component.getHandler() != null) {
                if (componentMap.containsKey(component.getHandler())) {
                    throw new OperationProcessorIsExistsException(component.getHandler());
                }
                componentMap.put(component.getHandler(), component);
            }
        }
    }

    @Override
    protected void myInit() {

    }
}
