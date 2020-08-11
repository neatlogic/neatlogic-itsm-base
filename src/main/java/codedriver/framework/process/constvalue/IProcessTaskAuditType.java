package codedriver.framework.process.constvalue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

public interface IProcessTaskAuditType {
	public final static List<IProcessTaskAuditType> AUDIT_TYPE_LIST = new ArrayList<>();
	public String getValue();
	public String getText();
	public String getDescription();
	
//	public static IProcessTaskAuditType getProcessTaskAuditType(String _value) {
//		for(IProcessTaskAuditType type : AUDIT_TYPE_LIST) {
//			if(type.getValue().equals(_value)) {
//				return type;
//			}
//		}
//		return null;
//	}
	public static String getDescription(String _value) {
		for(IProcessTaskAuditType type : AUDIT_TYPE_LIST) {
			if(type.getValue().equals(_value)) {
				return type.getDescription();
			}
		}
		return getDescription2(_value);
	}
	
	public static String getDescription2(String _value) {
		Reflections reflections = new Reflections("codedriver");
		Set<Class<? extends IProcessTaskAuditType>> classSet = reflections.getSubTypesOf(IProcessTaskAuditType.class);

		for (Class<? extends IProcessTaskAuditType> c : classSet) {
			try {
				Method m = c.getMethod("getDescription", String.class);
				String result = (String) m.invoke(null, _value);
				if(StringUtils.isNotBlank(result)) {
					return result;
				}
			}catch(Exception e) {
				
			}		
		}
		return "";
	}
}
