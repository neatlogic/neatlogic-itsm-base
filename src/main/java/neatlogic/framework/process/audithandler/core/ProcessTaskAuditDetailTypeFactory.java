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

package neatlogic.framework.process.audithandler.core;

import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: ProcessTaskAuditDetailTypeFactory
 * @Description: 工单活动内容类型工厂类
 */
public class ProcessTaskAuditDetailTypeFactory {
	/** 标记是否未初始化数据，只初始化一次 **/
	private static volatile boolean isUninitialized = true;
	
	private static final Set<IProcessTaskAuditDetailType> set = new HashSet<>();

    public static Set<IProcessTaskAuditDetailType> getAuditDetailTypeList(){
		if(isUninitialized) {
			synchronized(ProcessTaskAuditDetailTypeFactory.class) {
				if(isUninitialized) {
					Reflections reflections = new Reflections("neatlogic");
					Set<Class<? extends IProcessTaskAuditDetailType>> classSet = reflections.getSubTypesOf(IProcessTaskAuditDetailType.class);
					for (Class<? extends IProcessTaskAuditDetailType> c : classSet) {
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

	public static int getSort(String _value) {
		for (IProcessTaskAuditDetailType s : getAuditDetailTypeList()) {
			if (s.getValue().equals(_value)) {
				return s.getSort();
			}
		}
		return 0;
	}

	public static String getText(String _value) {
		for (IProcessTaskAuditDetailType s : getAuditDetailTypeList()) {
			if (s.getValue().equals(_value)) {
				return s.getText();
			}
		}
		return "";
	}
	public static boolean getNeedCompression(String _value) {
		for (IProcessTaskAuditDetailType s : getAuditDetailTypeList()) {
			if (s.getValue().equals(_value)) {
				return s.getNeedCompression();
			}
		}
		return false;
	}
}
