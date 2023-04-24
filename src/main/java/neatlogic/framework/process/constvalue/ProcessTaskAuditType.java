package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditType;
import neatlogic.framework.util.I18nUtils;

public enum ProcessTaskAuditType implements IProcessTaskAuditType {
	COMMENT("comment", "enum.process.processtaskaudittype.comment", "enum.process.processtaskaudittype.comment.1"),
	EDITCOMMENT("editcomment", "enum.process.processtaskaudittype.editcomment", "enum.process.processtaskaudittype.editcomment.1"),
	DELETECOMMENT("deletecomment", "enum.process.processtaskaudittype.deletecomment", "enum.process.processtaskaudittype.deletecomment.1"),
	UPDATE("update", "enum.process.processtaskaudittype.update", "enum.process.processtaskaudittype.update.1"),
	URGE("urge","enum.process.processtaskaudittype.urge","enum.process.processtaskaudittype.urge.1"),

	//任务
	CREATETASK("createtask", "enum.process.processtaskaudittype.createtask", "enum.process.processtaskaudittype.createtask.1"),
	EDITTASK("edittask", "enum.process.processtaskaudittype.edittask", "enum.process.processtaskaudittype.edittask.1"),
	COMPLETETASK("completetask", "enum.process.processtaskaudittype.completetask", "enum.process.processtaskaudittype.completetask.1"),
	DELETETASK("deletetask", "enum.process.processtaskaudittype.deletetask", "enum.process.processtaskaudittype.deletetask.1"),
	SAVETASKFILE("savetaskfile", "enum.process.processtaskaudittype.savetaskfile", "enum.process.processtaskaudittype.savetaskfile.1"),
	DELETETASKFILE("deletetaskfile", "enum.process.processtaskaudittype.deletetaskfile", "enum.process.processtaskaudittype.deletetaskfile.1"),


	START("start", "enum.process.processtaskaudittype.start", "enum.process.processtaskaudittype.start.1"),
	RETREAT("retreat", "撤回", "撤回【${DATA.processTaskStepName}】"),
	ABORTPROCESSTASK("abort", "enum.process.processtaskaudittype.abortprocesstask", "enum.process.processtaskaudittype.abortprocesstask.1"),
	RECOVERPROCESSTASK("recover", "enum.process.processtaskaudittype.recoverprocesstask", "enum.process.processtaskaudittype.recoverprocesstask.1"),
	PAUSE("pause", "enum.process.processtaskaudittype.pause", "enum.process.processtaskaudittype.pause.1"),
	RECOVER("recover", "enum.process.processtaskaudittype.recover", "enum.process.processtaskaudittype.recover.1"),
	TRANSFER("transfer", "enum.process.processtaskaudittype.transfer", "enum.process.processtaskaudittype.transfer.1"),
	STARTPROCESS("startprocess", "enum.process.processtaskaudittype.startprocess", "enum.process.processtaskaudittype.startprocess.1"),
	RESTFULACTION("restfulaction", "enum.process.processtaskaudittype.restfulaction","enum.process.processtaskaudittype.restfulaction.1"),
	COMPLETE("complete", "enum.process.processtaskaudittype.complete", "enum.process.processtaskaudittype.complete.1"),
	REAPPROVAL("reapproval", "enum.process.processtaskaudittype.reapproval", "enum.process.processtaskaudittype.reapproval.1"),
	BACK("back", "enum.process.processtaskaudittype.back", "enum.process.processtaskaudittype.back.1"),
	CONDITION("condition", "enum.process.processtaskaudittype.condition", "enum.process.processtaskaudittype.condition.1"),
	SCORE("score", "enum.process.processtaskaudittype.score", "enum.process.processtaskaudittype.score.1"),
    TRANSFERREPORT("tranferreport", "enum.process.processtaskaudittype.transferreport", "enum.process.processtaskaudittype.transferreport.1"),
    REPORTRELATION("reportrelation", "enum.process.processtaskaudittype.reportrelation", "enum.process.processtaskaudittype.reportrelation.1"),
    RELATION("relation", "enum.process.processtaskaudittype.relation", "enum.process.processtaskaudittype.relation.1"),
    DELETERELATION("deleterelation", "enum.process.processtaskaudittype.deleterelation", "enum.process.processtaskaudittype.deleterelation.1"),
    REDO("redo", "enum.process.processtaskaudittype.redo", "enum.process.processtaskaudittype.redo.1"),
    TRANSFERKNOWLEDGE("transferknowledge", "enum.process.processtaskaudittype.transferknowledge", "enum.process.processtaskaudittype.transferknowledge.1"),
    UPDATEFOCUSUSER("updatefocususer", "enum.process.processtaskaudittype.updatefocususer", "enum.process.processtaskaudittype.updatefocususer"),
    FOCUSTASK("focustask", "enum.process.processtaskaudittype.focustask", "enum.process.processtaskaudittype.focustask"),
    UNDOFOCUSTASK("undofocustask", "enum.process.processtaskaudittype.undofocustask", "enum.process.processtaskaudittype.undofocustask"),
	BINDREPEAT("bindrepeat", "enum.process.processtaskaudittype.bindrepeat", "enum.process.processtaskaudittype.bindrepeat"),
	UNBINDREPEAT("unbindrepeat", "enum.process.processtaskaudittype.unbindrepeat", "enum.process.processtaskaudittype.unbindrepeat")

	;
	private String value;
	private String text;
	private String description;
	
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
		return I18nUtils.getMessage(text);
	}

	@Override
	public String getDescription() {
		return I18nUtils.getMessage(description);
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
