package codedriver.framework.process.operate.core;

import java.util.Set;

import org.springframework.util.ClassUtils;

public interface IProcessOperateHandler {
	public OperateHandlerType getHandler();

	public Set<String> getFinalOperateList(Long processTaskId, Long processTaskStepId);
}
