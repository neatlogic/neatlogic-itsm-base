package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;

public interface IOperationAuthHandler {
    public Map<ProcessTaskOperationType, TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String>> getOperationBiPredicateMap();
    
	public String getHandler();

	default Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, String userUuid) {
        return getOperateMap(processTaskVo, null, userUuid);
    }

    default Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, String userUuid, List<ProcessTaskOperationType> operationTypeList) {    
        return getOperateMap(processTaskVo, null, userUuid, operationTypeList);
    }
    
    default Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, String userUuid) {
        Map<ProcessTaskOperationType, Boolean> resultMap = new HashMap<>();
        for(Entry<ProcessTaskOperationType, TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String>> entry : getOperationBiPredicateMap().entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue().test(processTaskVo, processTaskStepVo, userUuid));
        }
        return resultMap;
    }

    default Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, String userUuid, List<ProcessTaskOperationType> operationTypeList) {
        Map<ProcessTaskOperationType, Boolean> resultMap = new HashMap<>();
        for(ProcessTaskOperationType operationType : operationTypeList) {
            TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String> predicate = getOperationBiPredicateMap().get(operationType);
            if(predicate != null) {
                resultMap.put(operationType, predicate.test(processTaskVo, processTaskStepVo, userUuid));
            }
//            else {
//                resultMap.put(operationType, false);
//            }
        }    
        return resultMap;
    }

    default List<ProcessTaskOperationType> getAllOperationTypeList() {      
        return new ArrayList<>(getOperationBiPredicateMap().keySet());
    }
}
