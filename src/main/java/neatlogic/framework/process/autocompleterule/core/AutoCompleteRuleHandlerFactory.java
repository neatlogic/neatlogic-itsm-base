/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.autocompleterule.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.CodedriverWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.*;

/**
 * @author linbq
 * @since 2021/10/29 15:48
 **/
@RootComponent
public class AutoCompleteRuleHandlerFactory extends ModuleInitializedListenerBase {

    private static List<IAutoCompleteRuleHandler> list = new ArrayList<>();

    public static int getHandlerSize(){
        return list.size();
    }

    public static IAutoCompleteRuleHandler getHandler(int index) {
        if (index < list.size()) {
            return list.get(index);
        }
        return null;
    }
    @Override
    protected void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IAutoCompleteRuleHandler> myMap = context.getBeansOfType(IAutoCompleteRuleHandler.class);
        for (Map.Entry<String, IAutoCompleteRuleHandler> entry : myMap.entrySet()) {
            IAutoCompleteRuleHandler autoCompleteRuleHandler = entry.getValue();
            list.add(autoCompleteRuleHandler);
        }
        list.sort(Comparator.comparingInt(IAutoCompleteRuleHandler::getPriority));
    }

    @Override
    protected void myInit() {

    }
}
