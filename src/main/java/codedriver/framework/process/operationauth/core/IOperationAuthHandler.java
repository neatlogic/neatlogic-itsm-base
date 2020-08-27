package codedriver.framework.process.operationauth.core;

import java.util.List;
import java.util.Map;

import codedriver.framework.process.constvalue.OperationType;

public interface IOperationAuthHandler {
	public IOperationAuthHandlerType getHandler();

	public Map<String, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId);
	
	public boolean getFinalOperateMap(Long processTaskId, Long processTaskStepId, OperationType operationType);
	
	public List<OperationType> getAllOperationTypeList();
}
