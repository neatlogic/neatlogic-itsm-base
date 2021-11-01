/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.autocompleterule.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linbq
 * @since 2021/10/29 15:48
 **/
@RootComponent
public class AutoCompleteRuleHandlerFactory extends ModuleInitializedListenerBase {

    private static Map<String, IAutoCompleteRuleHandler> map = new HashMap<>();

    public static IAutoCompleteRuleHandler getHandler(String handler) {
        return map.get(handler);
    }
    @Override
    protected void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IAutoCompleteRuleHandler> myMap = context.getBeansOfType(IAutoCompleteRuleHandler.class);
        for (Map.Entry<String, IAutoCompleteRuleHandler> entry : myMap.entrySet()) {
            IAutoCompleteRuleHandler autoCompleteRuleHandler = entry.getValue();
            map.put(autoCompleteRuleHandler.getHandler(), autoCompleteRuleHandler);
        }
    }

    @Override
    protected void myInit() {

    }
}
