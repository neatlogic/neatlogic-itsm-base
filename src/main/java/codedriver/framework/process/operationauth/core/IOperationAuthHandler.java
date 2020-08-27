package codedriver.framework.process.operationauth.core;

import java.util.Map;

public interface IOperationAuthHandler {
	public IOperationAuthHandlerType getHandler();

	public Map<String, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId);
}
