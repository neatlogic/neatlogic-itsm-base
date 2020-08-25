package codedriver.framework.process.audithandler.core;

import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;

public interface IProcessTaskStepAuditDetailHandler {

	public String getType();
	
	public int handle(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);
}
