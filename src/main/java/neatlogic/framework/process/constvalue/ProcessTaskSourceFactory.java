/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

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

package neatlogic.framework.process.constvalue;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@RootComponent
public class ProcessTaskSourceFactory extends ModuleInitializedListenerBase {
    Logger logger = LoggerFactory.getLogger(ProcessTaskSourceFactory.class);
    private static final Map<String, String> sourcelValueTextMap = new HashMap<>();
    private static final Map<String, IProcessTaskSource> sourcelMap = new HashMap<>();
    private static final Map<String, IProcessTaskSource> handlerMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections("neatlogic");
        Set<Class<? extends IProcessTaskSource>> clazz = reflections.getSubTypesOf(IProcessTaskSource.class);
        for (Class<? extends IProcessTaskSource> c : clazz) {
            try {
                if (!c.isEnum()) {
                    continue;
                }
                Object[] objects = c.getEnumConstants();
                for (Object o : objects) {
                    sourcelValueTextMap.put(((IProcessTaskSource) o).getValue(), ((IProcessTaskSource) o).getText());
                    sourcelMap.put(((IProcessTaskSource) o).getValue(),(IProcessTaskSource)o);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getSourceName(String value) {
        return sourcelValueTextMap.get(value);
    }

    public static IProcessTaskSource getSource(String value) {
        return sourcelMap.get(value);
    }

    public static IProcessTaskSource getHandler(String handler) {
        return handlerMap.get(handler);
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IProcessTaskSource> myMap = context.getBeansOfType(IProcessTaskSource.class);
        for (Map.Entry<String, IProcessTaskSource> entry : myMap.entrySet()) {
            try {
                IProcessTaskSource handler = entry.getValue();
                if (handlerMap.containsKey(handler.getValue())) {
                    logger.error("IProcessTaskSource '" + handler.getClass().getSimpleName() + "(" + handler.getValue() + ")' repeat");
                    System.exit(1);
                }
                handlerMap.put(handler.getValue(), handler);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    protected void myInit() {

    }

}
