/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.datacube;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.CodedriverWebApplicationContext;
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
    public void onInitialized(CodedriverWebApplicationContext context) {
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
