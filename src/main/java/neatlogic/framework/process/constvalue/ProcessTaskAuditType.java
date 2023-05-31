package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditType;
import neatlogic.framework.util.I18nUtils;

public enum ProcessTaskAuditType implements IProcessTaskAuditType {
	COMMENT("comment", "enum.process.processtaskaudittype.comment.a", "enum.process.processtaskaudittype.comment.b"),
	EDITCOMMENT("editcomment", "enum.process.processtaskaudittype.editcomment.a", "enum.process.processtaskaudittype.editcomment.b"),
	DELETECOMMENT("deletecomment", "enum.process.processtaskaudittype.deletecomment.a", "enum.process.processtaskaudittype.deletecomment.b"),
	UPDATE("update", "enum.process.processtaskaudittype.update.a", "enum.process.processtaskaudittype.update.b"),
	URGE("urge","enum.process.processtaskaudittype.urge.a","enum.process.processtaskaudittype.urge.b"),

	//任务
	CREATETASK("createtask", "enum.process.processtaskaudittype.createtask.a", "enum.process.processtaskaudittype.createtask.b"),
	EDITTASK("edittask", "enum.process.processtaskaudittype.edittask.a", "enum.process.processtaskaudittype.edittask.b"),
	COMPLETETASK("completetask", "enum.process.processtaskaudittype.completetask.a", "enum.process.processtaskaudittype.completetask.b"),
	DELETETASK("deletetask", "enum.process.processtaskaudittype.deletetask.a", "enum.process.processtaskaudittype.deletetask.b"),
	SAVETASKFILE("savetaskfile", "enum.process.processtaskaudittype.savetaskfile.a", "enum.process.processtaskaudittype.savetaskfile.b"),
	DELETETASKFILE("deletetaskfile", "enum.process.processtaskaudittype.deletetaskfile.a", "enum.process.processtaskaudittype.deletetaskfile.b"),


	START("start", "enum.process.processtaskaudittype.start.a", "enum.process.processtaskaudittype.start.b"),
	RETREAT("retreat", "撤回", "撤回【${DATA.processTaskStepName}】"),
	ABORTPROCESSTASK("abortprocesstask", "enum.process.processtaskaudittype.abortprocesstask.a", "enum.process.processtaskaudittype.abortprocesstask.b"),
	RECOVERPROCESSTASK("recoverprocesstask", "enum.process.processtaskaudittype.recoverprocesstask.a", "enum.process.processtaskaudittype.recoverprocesstask.b"),
	PAUSE("pause", "enum.process.processtaskaudittype.pause.a", "enum.process.processtaskaudittype.pause.b"),
	RECOVER("recover", "enum.process.processtaskaudittype.recover.a", "enum.process.processtaskaudittype.recover.b"),
	TRANSFER("transfer", "enum.process.processtaskaudittype.transfer.a", "enum.process.processtaskaudittype.transfer.b"),
	STARTPROCESS("startprocess", "enum.process.processtaskaudittype.startprocess.a", "enum.process.processtaskaudittype.startprocess.b"),
	RESTFULACTION("restfulaction", "enum.process.processtaskaudittype.restfulaction.a","enum.process.processtaskaudittype.restfulaction.b"),
	COMPLETE("complete", "enum.process.processtaskaudittype.complete.a", "enum.process.processtaskaudittype.complete.b"),
	REAPPROVAL("reapproval", "enum.process.processtaskaudittype.reapproval.a", "enum.process.processtaskaudittype.reapproval.b"),
	BACK("back", "enum.process.processtaskaudittype.back.a", "enum.process.processtaskaudittype.back.b"),
	CONDITION("condition", "enum.process.processtaskaudittype.condition.a", "enum.process.processtaskaudittype.condition.b"),
	SCORE("score", "enum.process.processtaskaudittype.score.a", "enum.process.processtaskaudittype.score.b"),
    TRANSFERREPORT("tranferreport", "enum.process.processtaskaudittype.transferreport.a", "enum.process.processtaskaudittype.transferreport.b"),
    REPORTRELATION("reportrelation", "enum.process.processtaskaudittype.reportrelation.a", "enum.process.processtaskaudittype.reportrelation.b"),
    RELATION("relation", "enum.process.processtaskaudittype.relation.a", "enum.process.processtaskaudittype.relation.b"),
    DELETERELATION("deleterelation", "enum.process.processtaskaudittype.deleterelation.a", "enum.process.processtaskaudittype.deleterelation.b"),
    REDO("redo", "enum.process.processtaskaudittype.redo.a", "enum.process.processtaskaudittype.redo.b"),
    TRANSFERKNOWLEDGE("transferknowledge", "enum.process.processtaskaudittype.transferknowledge.a", "enum.process.processtaskaudittype.transferknowledge.b"),
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
