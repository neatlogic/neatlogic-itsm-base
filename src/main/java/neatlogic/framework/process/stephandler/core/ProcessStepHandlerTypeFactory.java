/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

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
