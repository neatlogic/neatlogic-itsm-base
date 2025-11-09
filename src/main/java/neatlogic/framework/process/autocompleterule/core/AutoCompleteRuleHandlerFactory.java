/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.autocompleterule.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author linbq
 * @since 2021/10/29 15:48
 **/
@RootComponent
public class AutoCompleteRuleHandlerFactory extends ModuleInitializedListenerBase {

    private static final List<IAutoCompleteRuleHandler> list = new ArrayList<>();

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
