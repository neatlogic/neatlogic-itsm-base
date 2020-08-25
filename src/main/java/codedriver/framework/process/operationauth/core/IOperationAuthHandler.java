package codedriver.framework.process.operationauth.core;

import java.util.Set;

import org.springframework.util.ClassUtils;

public interface IOperationAuthHandler {
	public IOperationAuthHandlerType getHandler();

	public Set<String> getFinalOperateList(Long processTaskId, Long processTaskStepId);
}
