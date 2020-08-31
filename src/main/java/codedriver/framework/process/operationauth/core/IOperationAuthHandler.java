package codedriver.framework.process.operationauth.core;

import java.util.List;
import java.util.Map;

import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;

public interface IOperationAuthHandler {
	public IOperationAuthHandlerType getHandler();

//	public Map<ProcessTaskOperationType, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId);
//	
//	public Map<ProcessTaskOperationType, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId, List<ProcessTaskOperationType> operationTypeList);
	
	public Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo);
	
	public Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, List<ProcessTaskOperationType> operationTypeList);
	
	public List<ProcessTaskOperationType> getAllOperationTypeList();
}
