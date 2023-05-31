package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.audithandler.core.IProcessTaskAuditType;
import neatlogic.framework.util.I18nUtils;

public enum ProcessTaskAuditType implements IProcessTaskAuditType {
	COMMENT("comment", "common.reply", "enum.process.processtaskaudittype.comment.b"),
	EDITCOMMENT("editcomment", "common.editreply", "enum.process.processtaskaudittype.editcomment.b"),
	DELETECOMMENT("deletecomment", "common.deletereply", "enum.process.processtaskaudittype.deletecomment.b"),
	UPDATE("update", "common.contend", "enum.process.processtaskaudittype.update.b"),
	URGE("urge","common.urgent","enum.process.processtaskaudittype.urge.b"),

	//任务
	CREATETASK("createtask", "common.itsm.createtask", "common.itsm.createtasktriggernotify"),
	EDITTASK("edittask", "common.edittask", "common.itsm.editnotify"),
	COMPLETETASK("completetask", "common.replytask", "common.itsm.completedtips"),
	DELETETASK("deletetask", "common.deletesubtask", "common.deletetriggernotify"),
	SAVETASKFILE("savetaskfile", "enum.process.processtaskaudittype.savetaskfile.a", "enum.process.processtaskaudittype.savetaskfile.b"),
	DELETETASKFILE("deletetaskfile", "enum.process.processtaskaudittype.deletetaskfile.a", "enum.process.processtaskaudittype.deletetaskfile.b"),


	START("start", "common.start", "common.itsm.starttriggernotify"),
	RETREAT("retreat", "撤回", "撤回【${DATA.processTaskStepName}】"),
	ABORTPROCESSTASK("abortprocesstask", "common.cancel", "common.cancelworkorder"),
	RECOVERPROCESSTASK("recoverprocesstask", "common.recover", "common.restoreworkorder"),
	PAUSE("pause", "common.pause", "common.itsm.pausetriggernotify"),
	RECOVER("recover", "common.recover", "common.itsm.recovertips"),
	TRANSFER("transfer", "common.forward", "enum.process.processtaskaudittype.transfer.b"),
	STARTPROCESS("startprocess", "common.report", "enum.process.processtaskaudittype.startprocess.b"),
	RESTFULACTION("restfulaction", "common.restfulaction","enum.process.processtaskaudittype.restfulaction.b"),
	COMPLETE("complete", "common.flow", "enum.process.processtaskaudittype.complete.b"),
	REAPPROVAL("reapproval", "common.review", "enum.process.processtaskaudittype.reapproval.b"),
	BACK("back", "common.back", "enum.process.processtaskaudittype.back.b"),
	CONDITION("condition", "common.condition", "enum.process.processtaskaudittype.condition.b"),
	SCORE("score", "common.score", "enum.process.processtaskaudittype.score.b"),
    TRANSFERREPORT("tranferreport", "common.transferreport", "enum.process.processtaskaudittype.transferreport.b"),
    REPORTRELATION("reportrelation", "enum.process.processtaskaudittype.reportrelation.a", "enum.process.processtaskaudittype.reportrelation.b"),
    RELATION("relation", "enum.process.processtaskaudittype.relation.a", "common.associatedworkorder"),
    DELETERELATION("deleterelation", "enum.process.processtaskaudittype.deleterelation.a", "enum.process.processtaskaudittype.deleterelation.b"),
    REDO("redo", "common.back", "enum.process.processtaskaudittype.redo.b"),
    TRANSFERKNOWLEDGE("transferknowledge", "enum.process.processtaskaudittype.transferknowledge.a", "enum.process.processtaskaudittype.transferknowledge.b"),
    UPDATEFOCUSUSER("updatefocususer", "common.modifyworkorderfocus", "common.modifyworkorderfocus"),
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
