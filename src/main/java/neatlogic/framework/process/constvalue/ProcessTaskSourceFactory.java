/*
 * Copyright (c)  2022 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
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
