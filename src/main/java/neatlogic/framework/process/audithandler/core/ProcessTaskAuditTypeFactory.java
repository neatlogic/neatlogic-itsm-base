package neatlogic.framework.process.audithandler.core;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
/**
 * 
* @Time:2020年8月11日
* @ClassName: ProcessTaskAuditTypeFactory 
* @Description: 工单活动类型工厂类
 */
public class ProcessTaskAuditTypeFactory {
	/** 标记是否未初始化数据，只初始化一次 **/
	private static volatile boolean isUninitialized = true;
	
	private static Set<IProcessTaskAuditType> set = new HashSet<>();
	
	public static Set<IProcessTaskAuditType> getAuditTypeList(){		
		if(isUninitialized) {
			synchronized(ProcessTaskAuditTypeFactory.class) {
				if(isUninitialized) {
					Reflections reflections = new Reflections("neatlogic");
					Set<Class<? extends IProcessTaskAuditType>> classSet = reflections.getSubTypesOf(IProcessTaskAuditType.class);
					for (Class<? extends IProcessTaskAuditType> c : classSet) {
						try {
							for(IProcessTaskAuditType type : c.getEnumConstants()) {
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
	public static String getDescription(String _value) {
		for(IProcessTaskAuditType type : getAuditTypeList()) {
			if(type.getValue().equals(_value)) {
				return type.getDescription();
			}
		}
		return "";
	}
}
