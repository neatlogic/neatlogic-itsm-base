/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.process.operationauth.core;

import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OperationTypeFactory {
    /** 标记是否未初始化数据，只初始化一次 **/
    private static volatile boolean isUninitialized = true;

    private static final Set<IOperationType> set = new HashSet<>();

    public static Set<IOperationType> getOperationTypeList(){
        if(isUninitialized) {
            synchronized(OperationTypeFactory.class) {
                if(isUninitialized) {
                    Reflections reflections = new Reflections("neatlogic");
                    Set<Class<? extends IOperationType>> classSet = reflections.getSubTypesOf(IOperationType.class);
                    for (Class<? extends IOperationType> c : classSet) {
                        try {
                            set.addAll(Arrays.asList(c.getEnumConstants()));
                        } catch (Exception ignored) {

                        }
                    }
                    isUninitialized = false;
                }
            }
        }
        return set;
    }
}
