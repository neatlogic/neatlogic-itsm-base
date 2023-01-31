/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.stepremind.core;

import org.reflections.Reflections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProcessTaskStepRemindTypeFactory {
    /** 标记是否未初始化数据，只初始化一次 **/
    private static volatile boolean isUninitialized = true;

    private static final Set<IProcessTaskStepRemindType> set = new HashSet<>();
    
    public static Set<IProcessTaskStepRemindType> getRemindTypeList(){        
        if(isUninitialized) {
            synchronized(ProcessTaskStepRemindTypeFactory.class) {
                if(isUninitialized) {
                    Reflections reflections = new Reflections("neatlogic");
                    Set<Class<? extends IProcessTaskStepRemindType>> classSet = reflections.getSubTypesOf(IProcessTaskStepRemindType.class);
                    for (Class<? extends IProcessTaskStepRemindType> c : classSet) {
                        try {
                            Collections.addAll(set, c.getEnumConstants());
                        }catch(Exception e) {
                            
                        }       
                    }
                    isUninitialized = false;
                }
            }
        }
        return set;
    }

    public static String getText(String _value) {
        for (IProcessTaskStepRemindType s : getRemindTypeList()) {
            if (s.getValue().equals(_value)) {
                return s.getText();
            }
        }
        return "";
    }
}
