package codedriver.framework.process.audithandler.core;

import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;

public abstract class ProcessTaskStepAuditDetailHandlerBase implements IProcessTaskStepAuditDetailHandler {

	@Override
	public void handle(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo) {
		myHandle(processTaskStepAuditDetailVo);		
	}

	protected abstract void myHandle(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);
}
