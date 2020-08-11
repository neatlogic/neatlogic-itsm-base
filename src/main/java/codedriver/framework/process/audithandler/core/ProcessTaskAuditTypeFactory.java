package codedriver.framework.process.audithandler.core;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

import codedriver.framework.process.constvalue.IProcessTaskAuditType;

public class ProcessTaskAuditTypeFactory {
	
	private static volatile boolean isUnloaded = true;
	
	private static Set<IProcessTaskAuditType> set = new HashSet<>();
	
	public static Set<IProcessTaskAuditType> getAuditTypeList(){		
		if(isUnloaded) {
			synchronized(ProcessTaskAuditTypeFactory.class) {
				if(isUnloaded) {
					Reflections reflections = new Reflections("codedriver");
					Set<Class<? extends IProcessTaskAuditType>> classSet = reflections.getSubTypesOf(IProcessTaskAuditType.class);
					for (Class<? extends IProcessTaskAuditType> c : classSet) {
						try {
							Method m = c.getMethod("values");
							IProcessTaskAuditType[] result = (IProcessTaskAuditType[]) m.invoke(null);
							for(IProcessTaskAuditType type : result) {
								set.add(type);
							}
						}catch(Exception e) {
							
						}		
					}
					isUnloaded = false;
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
