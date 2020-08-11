package codedriver.framework.process.audithandler.core;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

import codedriver.framework.process.constvalue.IProcessTaskAuditDetailType;

public class ProcessTaskAuditDetailTypeFactory {

	private static volatile boolean isUnloaded = true;
	
	private static Set<IProcessTaskAuditDetailType> set = new HashSet<>();
	
	public static Set<IProcessTaskAuditDetailType> getAuditDetailTypeList(){		
		if(isUnloaded) {
			synchronized(ProcessTaskAuditDetailTypeFactory.class) {
				if(isUnloaded) {
					Reflections reflections = new Reflections("codedriver");
					Set<Class<? extends IProcessTaskAuditDetailType>> classSet = reflections.getSubTypesOf(IProcessTaskAuditDetailType.class);
					for (Class<? extends IProcessTaskAuditDetailType> c : classSet) {
						try {
							Method m = c.getMethod("values");
							IProcessTaskAuditDetailType[] result = (IProcessTaskAuditDetailType[]) m.invoke(null);
							for(IProcessTaskAuditDetailType type : result) {
								System.out.println("IProcessTaskAuditDetailType:" + type);
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
}
