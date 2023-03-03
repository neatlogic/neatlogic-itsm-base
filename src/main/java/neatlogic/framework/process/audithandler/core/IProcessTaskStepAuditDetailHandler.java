package neatlogic.framework.process.audithandler.core;

import neatlogic.framework.process.dto.ProcessTaskStepAuditDetailVo;

public interface IProcessTaskStepAuditDetailHandler {

	public String getType();
	
	public int handle(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);
}
