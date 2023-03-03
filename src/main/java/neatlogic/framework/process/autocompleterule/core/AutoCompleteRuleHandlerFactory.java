/*
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.autocompleterule.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
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
    protected void onInitialized(NeatLogicWebApplicationContext context) {
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
