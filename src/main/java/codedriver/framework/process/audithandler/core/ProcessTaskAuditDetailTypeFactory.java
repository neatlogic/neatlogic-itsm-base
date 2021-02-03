package codedriver.framework.process.audithandler.core;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
/**
 * 
* @Time:2020年8月11日
* @ClassName: ProcessTaskAuditDetailTypeFactory 
* @Description: 工单活动内容类型工厂类
 */
public class ProcessTaskAuditDetailTypeFactory {
	/** 标记是否未初始化数据，只初始化一次 **/
	private static volatile boolean isUninitialized = true;
	
	private static Set<IProcessTaskAuditDetailType> set = new HashSet<>();
	
	public static Set<IProcessTaskAuditDetailType> getAuditDetailTypeList(){		
		if(isUninitialized) {
			synchronized(ProcessTaskAuditDetailTypeFactory.class) {
				if(isUninitialized) {
					Reflections reflections = new Reflections("codedriver");
					Set<Class<? extends IProcessTaskAuditDetailType>> classSet = reflections.getSubTypesOf(IProcessTaskAuditDetailType.class);
					for (Class<? extends IProcessTaskAuditDetailType> c : classSet) {
						try {
							for(IProcessTaskAuditDetailType type : c.getEnumConstants()) {
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
