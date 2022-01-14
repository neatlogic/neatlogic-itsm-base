package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

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
    public Map<ProcessTaskOperationType, TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String>>
        getOperationBiPredicateMap();

    public String getHandler();

    default Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo, String userUuid,
        Set<ProcessTaskOperationType> operationTypeList) {
        return getOperateMap(processTaskVo, null, userUuid, operationTypeList);
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 遍历执行权限逻辑 
    * @param processTaskVo 工单信息
    * @param processTaskStepVo 步骤信息
    * @param userUuid 用户
    * @param operationTypeList 需要判断的权限类型列表，可能为空，如果为空，则表示需要判断所有权限类型
    * @return Map<ProcessTaskOperationType,Boolean> 权限类型对应的值为true时，表示userUuid用户拥有当前工单或步骤的该权限；权限类型对应的值为false时，表示userUuid用户不拥有当前工单或步骤的该权限，
     */
    default Map<ProcessTaskOperationType, Boolean> getOperateMap(ProcessTaskVo processTaskVo,
        ProcessTaskStepVo processTaskStepVo, String userUuid, Set<ProcessTaskOperationType> operationTypeList) {
        Map<ProcessTaskOperationType, Boolean> resultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(operationTypeList)) {
            /** operationTypeList不为空时，只执行operationTypeList包含的权限判断逻辑 **/
            for (ProcessTaskOperationType operationType : operationTypeList) {
                TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String> predicate =
                    getOperationBiPredicateMap().get(operationType);
                if (predicate != null) {
                    resultMap.put(operationType, predicate.test(processTaskVo, processTaskStepVo, userUuid));
                }
            }
        } else {
            /** operationTypeList为空时，执行所有的权限判断逻辑 **/
            for (Entry<ProcessTaskOperationType,
                TernaryPredicate<ProcessTaskVo, ProcessTaskStepVo, String>> entry : getOperationBiPredicateMap()
                    .entrySet()) {
                resultMap.put(entry.getKey(), entry.getValue().test(processTaskVo, processTaskStepVo, userUuid));
            }
        }
        return resultMap;
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
