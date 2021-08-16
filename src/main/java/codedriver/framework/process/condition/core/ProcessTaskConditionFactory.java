/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.condition.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class ProcessTaskConditionFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskCondition> conditionComponentMap = new HashMap<>();

    public static IProcessTaskCondition getHandler(String name) {
        return conditionComponentMap.get(name);
    }

    @Override
    public void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IProcessTaskCondition> myMap = context.getBeansOfType(IProcessTaskCondition.class);
        for (Map.Entry<String, IProcessTaskCondition> entry : myMap.entrySet()) {
            IProcessTaskCondition column = entry.getValue();
            conditionComponentMap.put(column.getName(), column);
        }
    }

    public static Map<String, IProcessTaskCondition> getConditionComponentMap() {
        return conditionComponentMap;
    }

    @Override
    protected void myInit() {
        // TODO Auto-generated method stub

    }

}
