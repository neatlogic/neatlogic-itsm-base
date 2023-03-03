/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
