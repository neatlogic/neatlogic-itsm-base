/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

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
