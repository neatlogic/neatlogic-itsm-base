/*
 * Copyright (c)  2022 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.constvalue;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProcessTaskChannelFactory {

    private static final Map<String, String> channelValueTextMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections("codedriver");
        Set<Class<? extends IProcessTaskChannel>> clazz = reflections.getSubTypesOf(IProcessTaskChannel.class);
        for (Class<? extends IProcessTaskChannel> c : clazz) {
            try {
                Object[] objects = c.getEnumConstants();
                for (Object o : objects) {
                    channelValueTextMap.put(((IProcessTaskChannel) o).getValue(), ((IProcessTaskChannel) o).getText());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getChannelText(String value) {
        return channelValueTextMap.get(value);
    }

}
