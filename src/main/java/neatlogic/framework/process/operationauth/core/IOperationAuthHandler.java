package neatlogic.framework.process.operationauth.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.ProcessTaskVo;
import neatlogic.framework.process.exception.operationauth.ProcessTaskPermissionDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IOperationAuthHandler {
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 保存权限类型和该权限的判断逻辑 
    * @return Map<ProcessTaskOperationType,TernaryPredicate<ProcessTaskVo,ProcessTaskStepVo,String>>
     */
    Map<IOperationType, TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String, Map<Long, Map<IOperationType, ProcessTaskPermissionDeniedException>>, JSONObject>>
        getOperationBiPredicateMap();

    String getHandler();

    default Boolean getOperateMap(ProcessTaskVo processTaskVo, String userUuid,
                                  IOperationType operationType,
                                  Map<Long, Map<IOperationType, ProcessTaskPermissionDeniedException>> operationTypePermissionDeniedExceptionMap,
                                  JSONObject extraParam
    ) {
        return getOperateMap(processTaskVo, null, userUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
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
                                  IOperationType operationType,
                                  Map<Long, Map<IOperationType, ProcessTaskPermissionDeniedException>> operationTypePermissionDeniedExceptionMap,
                                  JSONObject extraParam
    ) {
        TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String, Map<Long, Map<IOperationType, ProcessTaskPermissionDeniedException>>, JSONObject> predicate =
                getOperationBiPredicateMap().get(operationType);
        if (predicate != null) {
            return predicate.test(processTaskVo, processTaskStepVo, userUuid, operationTypePermissionDeniedExceptionMap, extraParam);
        }
        return null;
    }

    /**
     * 
    * @Time:2020年12月21日
    * @Description: 返回当前handler能判断的权限列表
    * @return List<ProcessTaskOperationType>
     */
    default List<IOperationType> getAllOperationTypeList() {
        return new ArrayList<>(getOperationBiPredicateMap().keySet());
    }
}
