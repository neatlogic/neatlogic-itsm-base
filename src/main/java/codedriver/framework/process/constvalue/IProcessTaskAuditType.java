package codedriver.framework.process.constvalue;

import java.util.ArrayList;
import java.util.List;

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
		return "";
	}
}
