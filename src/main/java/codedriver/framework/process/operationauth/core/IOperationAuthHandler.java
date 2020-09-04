package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;

import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;

public interface IOperationAuthHandler {
    public Map<ProcessTaskOperationType, BiPredicate<ProcessTaskVo, ProcessTaskStepVo>> getOperationBiPredicateMap();
    
	public IOperationAuthHandlerType getHandler();

//	public Map<ProcessTaskOperationType, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId);
//	
//	public Map<ProcessTaskOperationType, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId, List<ProcessTaskOperationType> operationTypeList);

    default Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo) {
        Map<ProcessTaskOperationType, Boolean> resultMap = new HashMap<>();
        for(Entry<ProcessTaskOperationType, BiPredicate<ProcessTaskVo, ProcessTaskStepVo>> entry : getOperationBiPredicateMap().entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue().test(processTaskVo, processTaskStepVo));
        }
        return resultMap;
    }

    default Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, List<ProcessTaskOperationType> operationTypeList) {
        Map<ProcessTaskOperationType, Boolean> resultMap = new HashMap<>();
        for(ProcessTaskOperationType operationType : operationTypeList) {
            BiPredicate<ProcessTaskVo, ProcessTaskStepVo> predicate = getOperationBiPredicateMap().get(operationType);
            if(predicate != null) {
                resultMap.put(operationType, predicate.test(processTaskVo, processTaskStepVo));
            }else {
                resultMap.put(operationType, false);
            }
        }    
        return resultMap;
    }

    default List<ProcessTaskOperationType> getAllOperationTypeList() {      
        return new ArrayList<>(getOperationBiPredicateMap().keySet());
    }
}
