package codedriver.framework.process.stephandler.core;

import java.util.List;
import java.util.Set;

import codedriver.framework.process.audithandler.core.IProcessTaskAuditType;
import codedriver.framework.process.constvalue.OperationType;
import codedriver.framework.process.constvalue.ProcessTaskStepAction;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.notify.core.NotifyTriggerType;

public abstract class ProcessStepUtilHandlerBase extends ProcessStepHandlerUtilBase implements IProcessStepUtilHandler {



	@Override
	public void activityAudit(ProcessTaskStepVo currentProcessTaskStepVo, IProcessTaskAuditType action) {
		AuditHandler.audit(currentProcessTaskStepVo, action);
	}

	@Override
	public List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId) {
		return ActionRoleChecker.getProcessTaskStepActionList(processTaskId, processTaskStepId);
	}

	@Override
	public List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId, List<String> verifyActionList) {
		return ActionRoleChecker.getProcessTaskStepActionList(processTaskId, processTaskStepId, verifyActionList);
	}

	@Override
	public boolean verifyActionAuthoriy(Long processTaskId, Long processTaskStepId, ProcessTaskStepAction action) {
		return ActionRoleChecker.verifyActionAuthoriy(processTaskId, processTaskStepId, action);
	}

	@Override
	public List<ProcessTaskStepVo> getProcessableStepList(Long processTaskId) {
		return ActionRoleChecker.getProcessableStepList(processTaskId);
	}

	@Override
	public Set<ProcessTaskStepVo> getRetractableStepList(Long processTaskId) {
		return ActionRoleChecker.getRetractableStepListByProcessTaskId(processTaskId);
	}

	@Override
	public List<ProcessTaskStepVo> getUrgeableStepList(Long processTaskId) {
		return ActionRoleChecker.getUrgeableStepList(processTaskId);
	}

	@Override
	public void notify(ProcessTaskStepVo currentProcessTaskStepVo, NotifyTriggerType trigger) {
		NotifyHandler.notify(currentProcessTaskStepVo, trigger);
	}
	@Override
	public boolean verifyOperationAuthoriy(Long processTaskId, Long processTaskStepId, OperationType operation) {
		return true;
	}
}
