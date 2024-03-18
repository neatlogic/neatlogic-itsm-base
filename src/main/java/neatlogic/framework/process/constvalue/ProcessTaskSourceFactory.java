/*Copyright (C) 2023  深圳极向量科技有限公司 All Rights Reserved.

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
