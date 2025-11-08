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
