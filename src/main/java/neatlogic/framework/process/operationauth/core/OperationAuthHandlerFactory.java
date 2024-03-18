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
