package codedriver.framework.process.stepremind.core;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

public class ProcessTaskStepRemindTypeFactory {
    /** 标记是否未初始化数据，只初始化一次 **/
    private static volatile boolean isUninitialized = true;
    
    private static Set<IProcessTaskStepRemindType> set = new HashSet<>();
    
    public static Set<IProcessTaskStepRemindType> getRemindTypeList(){        
        if(isUninitialized) {
            synchronized(ProcessTaskStepRemindTypeFactory.class) {
                if(isUninitialized) {
                    Reflections reflections = new Reflections("codedriver");
                    Set<Class<? extends IProcessTaskStepRemindType>> classSet = reflections.getSubTypesOf(IProcessTaskStepRemindType.class);
                    for (Class<? extends IProcessTaskStepRemindType> c : classSet) {
                        try {
                            for(IProcessTaskStepRemindType type : c.getEnumConstants()) {
                                set.add(type);
                            }
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
