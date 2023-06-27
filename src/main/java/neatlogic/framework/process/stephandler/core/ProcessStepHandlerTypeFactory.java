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

package neatlogic.framework.process.stephandler.core;

import org.reflections.Reflections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 流程组件类型工厂
 */
public class ProcessStepHandlerTypeFactory {
    /**
     * 标记是否未初始化数据，只初始化一次
     **/
    private static volatile boolean isUninitialized = true;

    private static final Set<IProcessStepHandlerType> set = new HashSet<>();

    /**
     * 获取IProcessStepHandlerType接口所有实现枚举类集合
     */
    public static Set<IProcessStepHandlerType> getProcessStepHandlerTypeSet() {
        if (isUninitialized) {
            synchronized (ProcessStepHandlerTypeFactory.class) {
                if (isUninitialized) {
                    Reflections reflections = new Reflections("neatlogic");
                    Set<Class<? extends IProcessStepHandlerType>> classSet = reflections.getSubTypesOf(IProcessStepHandlerType.class);
                    for (Class<? extends IProcessStepHandlerType> c : classSet) {
                        Collections.addAll(set, c.getEnumConstants());
                    }
                    isUninitialized = false;
                }
            }
        }
        return set;
    }

    /**
     * 通过_handler值查询对应的name
     */
    public static String getName(String _handler) {
        for (IProcessStepHandlerType s : getProcessStepHandlerTypeSet()) {
            if (s.getHandler().equals(_handler)) {
                return s.getName();
            }
        }
        return "";
    }

    /**
     * 通过_handler值查询对应的type
     */
    public static String getType(String _handler) {
        for (IProcessStepHandlerType s : getProcessStepHandlerTypeSet()) {
            if (s.getHandler().equals(_handler)) {
                return s.getType();
            }
        }
        return "";
    }
}
