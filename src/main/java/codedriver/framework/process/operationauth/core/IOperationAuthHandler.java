package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;

public interface IOperationAuthHandler {
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 保存权限类型和该权限的判断逻辑 
    * @return Map<ProcessTaskOperationType,TernaryPredicate<ProcessTaskVo,ProcessTaskStepVo,String>>
     */
    Map<ProcessTaskOperationType, TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String>>
        getOperationBiPredicateMap();

    String getHandler();

    default Boolean getOperateMap(ProcessTaskVo processTaskVo, String userUuid,
                                  ProcessTaskOperationType operationType) {
        return getOperateMap(processTaskVo, null, userUuid, operationType);
    }

    /**
     *
     * @param processTaskVo 工单信息
     * @param processTaskStepVo 步骤信息
     * @param userUuid 用户
     * @param operationType 需要判断的权限类型
     * @return
     */
    default Boolean getOperateMap(ProcessTaskVo processTaskVo,
                                  ProcessTaskStepVo processTaskStepVo,
                                  String userUuid,
                                  ProcessTaskOperationType operationType) {
        TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String> predicate =
                getOperationBiPredicateMap().get(operationType);
        if (predicate != null) {
            return predicate.test(processTaskVo, processTaskStepVo, userUuid);
        }
        return null;
    }

    /**
     * 
    * @Time:2020年12月21日
    * @Description: 返回当前handler能判断的权限列表
    * @return List<ProcessTaskOperationType>
     */
    default List<ProcessTaskOperationType> getAllOperationTypeList() {
        return new ArrayList<>(getOperationBiPredicateMap().keySet());
    }
}
