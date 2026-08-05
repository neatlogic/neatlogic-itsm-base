package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditType;
import neatlogic.framework.util.$;

public enum ProcessTaskAuditType implements IProcessTaskAuditType {
	COMMENT("comment", "nfpc.processtaskaudittype.text.comment", "nfpc.processtaskaudittype.description.comment"),
	EDITCOMMENT("editcomment", "nfpc.processtaskaudittype.text.editcomment", "nfpc.processtaskaudittype.description.editcomment"),
	DELETECOMMENT("deletecomment", "nfpc.processtaskaudittype.text.deletecomment", "nfpc.processtaskaudittype.description.deletecomment"),
	UPDATE("update", "nfpc.processtaskaudittype.text.update", "nfpc.processtaskaudittype.description.update"),
	UPDATEFORM("updateform", "nfpc.processtaskaudittype.text.updateform", "nfpc.processtaskaudittype.description.updateform"),
	URGE("urge","nfpc.processtaskaudittype.text.urge","nfpc.processtaskaudittype.description.urge"),

	//任务
	CREATETASK("createtask", "nfpc.processtaskaudittype.text.createtask", "nfpc.processtaskaudittype.description.createtask"),
	EDITTASK("edittask", "nfpc.processtaskaudittype.text.edittask", "nfpc.processtaskaudittype.description.edittask"),
	COMPLETETASK("completetask", "nfpc.processtaskaudittype.text.completetask", "nfpc.processtaskaudittype.description.completetask"),
	DELETETASK("deletetask", "nfpc.processtaskaudittype.text.deletetask", "nfpc.processtaskaudittype.description.deletetask"),
	SAVETASKFILE("savetaskfile", "nfpc.processtaskaudittype.text.savetaskfile", "nfpc.processtaskaudittype.description.savetaskfile"),
	DELETETASKFILE("deletetaskfile", "nfpc.processtaskaudittype.text.deletetaskfile", "nfpc.processtaskaudittype.description.deletetaskfile"),

	ACTIVE("active", "nfpc.processtaskaudittype.text.active", "nfpc.processtaskaudittype.description.active"),
	START("start", "nfpc.processtaskaudittype.text.start", "nfpc.processtaskaudittype.description.start"),
	RETREAT("retreat", "nfpc.processtaskaudittype.text.retreat", "nfpc.processtaskaudittype.description.retreat"),
	ABORTPROCESSTASK("abortprocesstask", "nfpc.processtaskaudittype.text.abortprocesstask", "nfpc.processtaskaudittype.description.abortprocesstask"),
	RECOVERPROCESSTASK("recoverprocesstask", "nfpc.processtaskaudittype.text.recoverprocesstask", "nfpc.processtaskaudittype.description.recoverprocesstask"),
	PAUSE("pause", "nfpc.processtaskaudittype.text.pause", "nfpc.processtaskaudittype.description.pause"),
	RECOVER("recover", "nfpc.processtaskaudittype.text.recover", "nfpc.processtaskaudittype.description.recover"),
	TRANSFER("transfer", "nfpc.processtaskaudittype.text.transfer", "nfpc.processtaskaudittype.description.transfer"),
	STARTPROCESS("startprocess", "nfpc.processtaskaudittype.text.startprocess", "nfpc.processtaskaudittype.description.startprocess"),
	RESTFULACTION("restfulaction", "nfpc.processtaskaudittype.text.restfulaction","【${DATA.processTaskStepName}】"),
	COMPLETE("complete", "nfpc.processtaskaudittype.text.complete", "nfpc.processtaskaudittype.description.complete"),
	REAPPROVAL("reapproval", "nfpc.processtaskaudittype.text.reapproval", "nfpc.processtaskaudittype.description.reapproval"),
	BACK("back", "nfpc.processtaskaudittype.text.back", "nfpc.processtaskaudittype.description.back"),
	CONDITION("condition", "nfpc.processtaskaudittype.text.condition", "nfpc.processtaskaudittype.description.condition"),
	SCORE("score", "nfpc.processtaskaudittype.text.score", "nfpc.processtaskaudittype.description.score"),
    TRANSFERREPORT("tranferreport", "nfpc.processtaskaudittype.text.transferreport", "nfpc.processtaskaudittype.description.transferreport"),
    REPORTRELATION("reportrelation", "nfpc.processtaskaudittype.text.reportrelation", "nfpc.processtaskaudittype.description.reportrelation"),
    RELATION("relation", "nfpc.processtaskaudittype.text.relation", "nfpc.processtaskaudittype.description.relation"),
    DELETERELATION("deleterelation", "nfpc.processtaskaudittype.text.deleterelation", "nfpc.processtaskaudittype.description.deleterelation"),
    REDO("redo", "nfpc.processtaskaudittype.text.redo", "nfpc.processtaskaudittype.description.redo"),
    TRANSFERKNOWLEDGE("transferknowledge", "nfpc.processtaskaudittype.text.transferknowledge", "nfpc.processtaskaudittype.description.transferknowledge"),
    UPDATEFOCUSUSER("updatefocususer", "nfpc.processtaskaudittype.text.updatefocususer", "nfpc.processtaskaudittype.description.updatefocususer"),
    FOCUSTASK("focustask", "nfpc.processtaskaudittype.text.focustask", "nfpc.processtaskaudittype.description.focustask"),
    UNDOFOCUSTASK("undofocustask", "nfpc.processtaskaudittype.text.undofocustask", "nfpc.processtaskaudittype.description.undofocustask"),
	BINDREPEAT("bindrepeat", "nfpc.processtaskaudittype.text.bindrepeat", "nfpc.processtaskaudittype.description.bindrepeat"),
	UNBINDREPEAT("unbindrepeat", "nfpc.processtaskaudittype.text.unbindrepeat", "nfpc.processtaskaudittype.description.unbindrepeat"),
	BOUNDREPEAT("boundrepeat", "nfpc.processtaskaudittype.text.boundrepeat", "nfpc.processtaskaudittype.description.boundrepeat"),
	UNBOUNDREPEAT("unboundrepeat", "nfpc.processtaskaudittype.text.unboundrepeat", "nfpc.processtaskaudittype.description.unboundrepeat"),
	AUTOCANCELREPEAT("autocancelrepeat", "nfpc.processtaskaudittype.text.autocancelrepeat", "nfpc.processtaskaudittype.description.autocancelrepeat"),
	;
	private final String value;
	private final String text;
	private final String description;
	
	ProcessTaskAuditType(String value, String text, String description) {
		this.value = value;
		this.text = text;
		this.description = description;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getText() {
		return $.t(text);
	}

	@Override
	public String getDescription() {
		return $.t(description);
	}

	public static ProcessTaskAuditType getProcessTaskAuditType(String _value) {
		for(ProcessTaskAuditType type : values()) {
			if(type.getValue().equals(_value)) {
				return type;
			}
		}
		return null;
	}

}
