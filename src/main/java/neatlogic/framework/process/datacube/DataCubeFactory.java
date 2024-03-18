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

package neatlogic.framework.process.datacube;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.exception.process.ProcessStepHandlerNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class DataCubeFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IDataCubeHandler> componentMap = new HashMap<String, IDataCubeHandler>();

    public static IDataCubeHandler getComponent(String type) {
        if (!componentMap.containsKey(type) || componentMap.get(type) == null) {
            throw new ProcessStepHandlerNotFoundException(type);
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
