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
