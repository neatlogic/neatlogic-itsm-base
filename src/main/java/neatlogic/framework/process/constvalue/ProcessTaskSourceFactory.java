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

package neatlogic.framework.process.constvalue;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProcessTaskSourceFactory {

    private static final Map<String, String> sourcelValueTextMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections("neatlogic");
        Set<Class<? extends IProcessTaskSource>> clazz = reflections.getSubTypesOf(IProcessTaskSource.class);
        for (Class<? extends IProcessTaskSource> c : clazz) {
            try {
                Object[] objects = c.getEnumConstants();
                for (Object o : objects) {
                    sourcelValueTextMap.put(((IProcessTaskSource) o).getValue(), ((IProcessTaskSource) o).getText());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getSourceName(String value) {
        return sourcelValueTextMap.get(value);
    }

}
