package codedriver.framework.process.operationauth.core;

import java.util.List;
import java.util.Map;

import codedriver.framework.process.constvalue.ProcessTaskOperationType;

public interface IOperationAuthHandler {
	public IOperationAuthHandlerType getHandler();

	public Map<ProcessTaskOperationType, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId);
	
	public Map<ProcessTaskOperationType, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId, List<ProcessTaskOperationType> operationTypeList);
	
	public List<ProcessTaskOperationType> getAllOperationTypeList();
}
