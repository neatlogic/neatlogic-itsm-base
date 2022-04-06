package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import codedriver.framework.process.dao.mapper.*;
import codedriver.framework.process.dto.*;
import codedriver.framework.process.dto.agent.ProcessTaskAgentTargetVo;
import codedriver.framework.process.dto.agent.ProcessTaskAgentVo;
import codedriver.framework.process.exception.operationauth.ProcessTaskPermissionDeniedException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.exception.processtask.ProcessTaskNoPermissionException;

/**
 * 
 * @Time:2020年12月15日
 * @ClassName: ProcessOperateManager
 * @Description: 权限判断管理类，给步骤操作页面返回操作按钮列表，校验操作是否有权限
 */
@Component
public class ProcessAuthManager {
    private final static Logger logger = LoggerFactory.getLogger(ProcessAuthManager.class);
    private static ProcessTaskMapper processTaskMapper;
    private static ProcessTaskAgentMapper processTaskAgentMapper;
    private static SelectContentByHashMapper selectContentByHashMapper;
    private static ChannelMapper channelMapper;
    private static CatalogMapper catalogMapper;
    @Autowired
    private ProcessAuthManager(
            ProcessTaskAgentMapper _processTaskAgentMapper,
            ProcessTaskMapper _processTaskMapper,
            SelectContentByHashMapper _selectContentByHashMapper,
            ChannelMapper _channelMapper,
            CatalogMapper _catalogMapper) {
        processTaskAgentMapper = _processTaskAgentMapper;
        processTaskMapper = _processTaskMapper;
        selectContentByHashMapper = _selectContentByHashMapper;
        channelMapper = _channelMapper;
        catalogMapper = _catalogMapper;
    }

    /** 需要校验的工单id列表 **/
    private Set<Long> processTaskIdSet;
    /** 需要校验的步骤id列表 **/
    private Set<Long> processTaskStepIdSet;
    /** 工单id与步骤idList的键值对 **/
    private Map<Long, Set<Long>> processTaskStepIdSetMap;
    /** 需要校验的权限列表 **/
    private Set<ProcessTaskOperationType> operationTypeSet;
    /** 需要校验的某个工单或步骤的某个权限 **/
    private Map<Long, ProcessTaskOperationType> checkOperationTypeMap;
    /** 缓存作用，保存授权给当前用户处理服务工单的用户列表 **/
    private Map<String, List<String>> channelUuidFromUserUuidListMap = new HashMap<>();
    /** 缓存作用，保存当前用户授权列表 **/
    private Map<String, List<ProcessTaskAgentVo>> processTaskAgentListMap = new HashMap<>();
    /** 保存某个工单或步骤的某个权限检验时，导致失败的原因 **/
    private Map<Long, Map<ProcessTaskOperationType, ProcessTaskPermissionDeniedException>> operationTypePermissionDeniedExceptionMap = new HashMap<>();
    /** 保存额外参数 **/
    private Map<Long, JSONObject> extraParamMap = new HashMap<>();
    public static class Builder {
        private Set<Long> processTaskIdSet = new HashSet<>();
        private Set<Long> processTaskStepIdSet = new HashSet<>();
        private Set<ProcessTaskOperationType> operationTypeSet = new HashSet<>();

        public Builder() {}

        public Builder addProcessTaskId(Long... processTaskIds) {
            for (Long processTaskId : processTaskIds) {
                if (processTaskId != null) {
                    processTaskIdSet.add(processTaskId);
                }
            }
            return this;
        }

        public Builder addProcessTaskStepId(Long... processTaskStepIds) {
            for (Long processTaskStepId : processTaskStepIds) {
                if (processTaskStepId != null) {
                    processTaskStepIdSet.add(processTaskStepId);
                }
            }
            return this;
        }
        public Builder addProcessTaskStepId(List<Long> processTaskStepIds) {
            for (Long processTaskStepId : processTaskStepIds) {
                if (processTaskStepId != null) {
                    processTaskStepIdSet.add(processTaskStepId);
                }
            }
            return this;
        }

        public Builder addOperationType(ProcessTaskOperationType operationType) {
            operationTypeSet.add(operationType);
            return this;
        }

        public ProcessAuthManager build() {
            return new ProcessAuthManager(this);
        }
    }

    /**
     *
     * @Time:2020年12月15日
     * @ClassName: TaskOperationChecker
     * @Description: 校验工单级权限
     */
    public static class TaskOperationChecker {
        private Long processTaskId;
        private ProcessTaskOperationType operationType;
        private JSONObject extraParam;

        public TaskOperationChecker(Long processTaskId, ProcessTaskOperationType operationType) {
            this.processTaskId = processTaskId;
            this.operationType = operationType;
        }

        public TaskOperationChecker addExtraParam(String key, Object data) {
            if (extraParam == null) {
                extraParam = new JSONObject();
            }
            extraParam.put(key, data);
            return this;
        }
        public ProcessAuthManager build() {
            return new ProcessAuthManager(this);
        }
    }

    /**
     *
     * @Time:2020年12月15日
     * @ClassName: StepOperationChecker
     * @Description: 校验步骤级权限
     */
    public static class StepOperationChecker {
        private Long processTaskStepId;
        private ProcessTaskOperationType operationType;
        private JSONObject extraParam;

        public StepOperationChecker(Long processTaskStepId, ProcessTaskOperationType operationType) {
            this.processTaskStepId = processTaskStepId;
            this.operationType = operationType;
        }

        public StepOperationChecker addExtraParam(String key, Object data) {
            if (extraParam == null) {
                extraParam = new JSONObject();
            }
            extraParam.put(key, data);
            return this;
        }

        public ProcessAuthManager build() {
            return new ProcessAuthManager(this);
        }
    }

    private ProcessAuthManager(Builder builder) {
        this.processTaskIdSet = builder.processTaskIdSet;
        this.processTaskStepIdSet = builder.processTaskStepIdSet;
        this.operationTypeSet = builder.operationTypeSet;
    }

    private ProcessAuthManager(TaskOperationChecker checker) {
        this.processTaskIdSet = new HashSet<>();
        processTaskIdSet.add(checker.processTaskId);
        this.operationTypeSet = new HashSet<>();
        operationTypeSet.add(checker.operationType);
        this.checkOperationTypeMap = new HashMap<>();
        checkOperationTypeMap.put(checker.processTaskId, checker.operationType);
        extraParamMap.put(checker.processTaskId, checker.extraParam);
    }

    private ProcessAuthManager(StepOperationChecker checker) {
        this.processTaskStepIdSet = new HashSet<>();
        processTaskStepIdSet.add(checker.processTaskStepId);
        this.operationTypeSet = new HashSet<>();
        operationTypeSet.add(checker.operationType);
        this.checkOperationTypeMap = new HashMap<>();
        checkOperationTypeMap.put(checker.processTaskStepId, checker.operationType);
        extraParamMap.put(checker.processTaskStepId, checker.extraParam);
    }
    /**
     *
     * @Time:2020年12月21日
     * @Description: 返回多个工单及其步骤权限列表，返回值map中的key可能是工单id或步骤id，value就是其拥有的权限列表
     * @return Map<Long,Set<ProcessTaskOperationType>>
     */
    public Map<Long, Set<ProcessTaskOperationType>> getOperateMap() {
//        long startTime = System.currentTimeMillis();
        Map<Long, Set<ProcessTaskOperationType>> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(processTaskIdSet) && CollectionUtils.isEmpty(processTaskStepIdSet)) {
            return resultMap;
        }

        if (processTaskStepIdSetMap == null) {
            processTaskStepIdSetMap = new HashMap<>();
        }
        if (CollectionUtils.isNotEmpty(processTaskStepIdSet)) {
            if (processTaskIdSet == null) {
                processTaskIdSet = new HashSet<>();
            }
            List<ProcessTaskStepVo> processTaskStepList =
                    processTaskMapper.getProcessTaskStepListByIdList(new ArrayList<>(processTaskStepIdSet));
            for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
                processTaskIdSet.add(processTaskStepVo.getProcessTaskId());
                processTaskStepIdSetMap.computeIfAbsent(processTaskStepVo.getProcessTaskId(), k -> new HashSet<>())
                        .add(processTaskStepVo.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(processTaskIdSet)) {
            List<Long> processTaskIdList = new ArrayList<>(processTaskIdSet);
            List<ProcessTaskStepWorkerVo> processTaskStepWorkerList =
                    processTaskMapper.getProcessTaskStepWorkerListByProcessTaskIdList(processTaskIdList);
            Map<Long, List<ProcessTaskStepWorkerVo>> processTaskStepWorkerListMap = new HashMap<>();
            for (ProcessTaskStepWorkerVo processTaskStepWorkerVo : processTaskStepWorkerList) {
                processTaskStepWorkerListMap
                        .computeIfAbsent(processTaskStepWorkerVo.getProcessTaskStepId(), k -> new ArrayList<>())
                        .add(processTaskStepWorkerVo);
            }
            List<ProcessTaskStepUserVo> processTaskStepUserList =
                    processTaskMapper.getProcessTaskStepUserListByProcessTaskIdList(processTaskIdList);
            Map<Long, List<ProcessTaskStepUserVo>> processTaskStepUserListMap = new HashMap<>();
            for (ProcessTaskStepUserVo processTaskStepUserVo : processTaskStepUserList) {
                processTaskStepUserListMap
                        .computeIfAbsent(processTaskStepUserVo.getProcessTaskStepId(), k -> new ArrayList<>())
                        .add(processTaskStepUserVo);
            }
            List<ProcessTaskStepVo> processTaskStepList =
                    processTaskMapper.getProcessTaskStepListByProcessTaskIdList(processTaskIdList);
            Map<Long, List<ProcessTaskStepVo>> processTaskStepListMap = new HashMap<>();
            for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
                processTaskStepVo.setWorkerList(
                        processTaskStepWorkerListMap.computeIfAbsent(processTaskStepVo.getId(), k -> new ArrayList<>()));
                processTaskStepVo.setUserList(
                        processTaskStepUserListMap.computeIfAbsent(processTaskStepVo.getId(), k -> new ArrayList<>()));
                processTaskStepListMap.computeIfAbsent(processTaskStepVo.getProcessTaskId(), k -> new ArrayList<>())
                        .add(processTaskStepVo);
            }
            List<ProcessTaskStepRelVo> processTaskStepRelList =
                    processTaskMapper.getProcessTaskStepRelListByProcessTaskIdList(processTaskIdList);
            Map<Long, List<ProcessTaskStepRelVo>> processTaskStepRelListMap = new HashMap<>();
            for (ProcessTaskStepRelVo processTaskStepRelVo : processTaskStepRelList) {
                processTaskStepRelListMap
                        .computeIfAbsent(processTaskStepRelVo.getProcessTaskId(), k -> new ArrayList<>())
                        .add(processTaskStepRelVo);
            }

            List<ProcessTaskVo> processTaskList = processTaskMapper.getProcessTaskListByIdList(processTaskIdList);
            Set<String> hashSet = processTaskList.stream().map(ProcessTaskVo::getConfigHash).collect(Collectors.toSet());
//            long startTime3 = System.currentTimeMillis();
            List<ProcessTaskConfigVo> processTaskConfigList = selectContentByHashMapper.getProcessTaskConfigListByHashList(new ArrayList<>(hashSet));
//            logger.error("D:" + (System.currentTimeMillis() - startTime3));
            Map<String, String> processTaskConfigMap = processTaskConfigList.stream().collect(Collectors.toMap(e->e.getHash(), e -> e.getConfig()));
//            logger.error("A:" + (System.currentTimeMillis() - startTime));
            for (ProcessTaskVo processTaskVo : processTaskList) {
                processTaskVo.setConfig(processTaskConfigMap.get(processTaskVo.getConfigHash()));
//                startTime = System.currentTimeMillis();
                processTaskVo.setStepList(processTaskStepListMap.computeIfAbsent(processTaskVo.getId(), k -> new ArrayList<>()));
                processTaskVo.setStepRelList(processTaskStepRelListMap.computeIfAbsent(processTaskVo.getId(), k -> new ArrayList<>()));
                resultMap.putAll(getOperateMap(processTaskVo));
//                logger.error("B(" + processTaskVo.getId() + "):" + (System.currentTimeMillis() - startTime));
            }
        }
        return resultMap;
    }
    /**
     *
     * @Time:2020年12月21日
     * @Description: 返回一个工单及其步骤权限列表，返回值map中的key可能是工单id或步骤id，value就是其拥有的权限列表
     * @return Map<Long,Set<ProcessTaskOperationType>>
     */
    private Map<Long, Set<ProcessTaskOperationType>> getOperateMap(ProcessTaskVo processTaskVo) {
        Set<ProcessTaskOperationType> taskOperationTypeSet = new HashSet<>();
        Set<ProcessTaskOperationType> stepOperationTypeSet = new HashSet<>();
        List<ProcessTaskOperationType> taskOperationTypeList = OperationAuthHandlerType.TASK.getOperationTypeList();
        List<ProcessTaskOperationType> stepOperationTypeList = OperationAuthHandlerType.STEP.getOperationTypeList();
        if (CollectionUtils.isEmpty(operationTypeSet)) {
            taskOperationTypeSet.addAll(taskOperationTypeList);
            stepOperationTypeSet.addAll(stepOperationTypeList);
        } else {
            for (ProcessTaskOperationType operationType : operationTypeSet) {
                if (taskOperationTypeList.contains(operationType)) {
                    taskOperationTypeSet.add(operationType);
                } else if (stepOperationTypeList.contains(operationType)) {
                    stepOperationTypeSet.add(operationType);
                }
            }
        }
        Map<Long, Set<ProcessTaskOperationType>> resultMap = new HashMap<>();
        String userUuid = UserContext.get().getUserUuid(true);
        JSONObject extraParam = extraParamMap.get(processTaskVo.getId());
        if (CollectionUtils.isNotEmpty(taskOperationTypeSet)) {
            IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(OperationAuthHandlerType.TASK.getValue());
            Set<ProcessTaskOperationType> resultSet = new HashSet<>();
            for (ProcessTaskOperationType operationType : taskOperationTypeSet) {
                boolean result = handler.getOperateMap(processTaskVo, userUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                if (result) {
                    resultSet.add(operationType);
                } else {
                    /** 因为上报权限不能授权，所以转报和复制上报权限不能授权 **/
                    if (ProcessTaskOperationType.PROCESSTASK_TRANSFERREPORT == operationType) {
                        continue;
                    }
                    if (ProcessTaskOperationType.PROCESSTASK_COPYPROCESSTASK == operationType) {
                        continue;
                    }
                    if (ProcessTaskOperationType.PROCESSTASK_START == operationType) {
                        continue;
                    }
                    /** 如果当前用户接受了其他用户的授权，查出其他用户拥有的权限，叠加当前用户权限里 **/
                    List<String> fromUuidList = getFromUuidListByChannelUuid(processTaskVo.getChannelUuid());
                    if (CollectionUtils.isNotEmpty(fromUuidList)) {
                        for (String fromUuid : fromUuidList) {
                            result = handler.getOperateMap(processTaskVo, fromUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                            if (result) {
                                resultSet.add(operationType);
                                break;
                            }
                        }
                    }
                }
            }
            resultMap.put(processTaskVo.getId(), resultSet);
        }
        if (CollectionUtils.isNotEmpty(stepOperationTypeSet)) {
            Set<Long> processTaskStepIdList = processTaskStepIdSetMap.get(processTaskVo.getId());
            if (CollectionUtils.isNotEmpty(processTaskStepIdList)) {
                IOperationAuthHandler stepHandler = OperationAuthHandlerFactory.getHandler(OperationAuthHandlerType.STEP.getValue());
                for (ProcessTaskStepVo processTaskStepVo : processTaskVo.getStepList()) {
                    if (processTaskStepIdList.contains(processTaskStepVo.getId())) {
                        extraParam = extraParamMap.get(processTaskStepVo.getId());
                        Set<ProcessTaskOperationType> resultSet = new HashSet<>();
                        for (ProcessTaskOperationType operationType : stepOperationTypeSet) {
                            Boolean result = null;
                            IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(processTaskStepVo.getHandler());
                            if (handler != null) {
                                result = handler.getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                            }
                            if(result == null) {
                                result = stepHandler.getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                                if (result == null) {
                                    result = false;
                                }
                            }
                            if (result) {
                                resultSet.add(operationType);
                            } else {
                                /** 如果当前用户接受了其他用户的授权，查出其他用户拥有的权限，叠加当前用户权限里 **/
                                List<String> fromUuidList = getFromUuidListByChannelUuid(processTaskVo.getChannelUuid());
                                if (CollectionUtils.isNotEmpty(fromUuidList)) {
                                    for (String fromUuid : fromUuidList) {
                                        if (handler != null) {
                                            result = handler.getOperateMap(processTaskVo, processTaskStepVo, fromUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                                        }
                                        if(result == null) {
                                            result = stepHandler.getOperateMap(processTaskVo, processTaskStepVo, fromUuid, operationType, operationTypePermissionDeniedExceptionMap, extraParam);
                                            if (result == null) {
                                                result = false;
                                            }
                                        }
                                        if (result) {
                                            resultSet.add(operationType);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        resultMap.put(processTaskStepVo.getId(), resultSet);
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     *  获取授权给当前用户处理服务工单的用户列表
     * @param channelUuid
     * @return
     */
    private List<String> getFromUuidListByChannelUuid(String channelUuid) {
        List<String> fromUserUuidList = channelUuidFromUserUuidListMap.get(channelUuid);
        if (fromUserUuidList == null) {
            fromUserUuidList = new ArrayList<>();
            List<ProcessTaskAgentVo> processTaskAgentList = processTaskAgentListMap.get(UserContext.get().getUserUuid(true));
            if (processTaskAgentList == null) {
                processTaskAgentList = processTaskAgentMapper.getProcessTaskAgentDetailListByToUserUuid(UserContext.get().getUserUuid(true));
                processTaskAgentListMap.put(UserContext.get().getUserUuid(true), processTaskAgentList);
            }
            if (CollectionUtils.isNotEmpty(processTaskAgentList)) {
                for (ProcessTaskAgentVo processTaskAgentVo : processTaskAgentList) {
                    String fromUserUuid = processTaskAgentVo.getFromUserUuid();
                    if (fromUserUuidList.contains(fromUserUuid)) {
                        continue;
                    }
                    boolean flag = false;
                    List<String> catalogUuidList = new ArrayList<>();
                    List<ProcessTaskAgentTargetVo> processTaskAgentTargetList = processTaskAgentVo.getProcessTaskAgentTargetVos();
                    for (ProcessTaskAgentTargetVo processTaskAgentTargetVo : processTaskAgentTargetList) {
                        String type = processTaskAgentTargetVo.getType();
                        if ("channel".equals(type)) {
                            if (channelUuid.equals(processTaskAgentTargetVo.getTarget())) {
                                flag = true;
                                break;
                            }
                        } else if ("catalog".equals(type)) {
                            catalogUuidList.add(processTaskAgentTargetVo.getTarget());
                        }
                    }
                    if (!flag && CollectionUtils.isNotEmpty(catalogUuidList)) {
                        ChannelVo channelVo = channelMapper.getChannelByUuid(channelUuid);
                        CatalogVo catalogVo = catalogMapper.getCatalogByUuid(channelVo.getParentUuid());
                        List<String> upwardUuidList = catalogMapper.getUpwardUuidListByLftRht(catalogVo.getLft(), catalogVo.getRht());
                        flag = catalogUuidList.removeAll(upwardUuidList);
                    }
                    if (flag) {
                        fromUserUuidList.add(fromUserUuid);
                    }
                }
            }
            channelUuidFromUserUuidListMap.put(channelUuid, fromUserUuidList);
        }
        return fromUserUuidList;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 检查是否拥有某个权限 
    * @return boolean
     */
    public boolean check() {
        if (MapUtils.isNotEmpty(checkOperationTypeMap)) {
            Map<Long, Set<ProcessTaskOperationType>> resultMap = getOperateMap();
            for (Map.Entry<Long, ProcessTaskOperationType> entry : checkOperationTypeMap.entrySet()) {
                return resultMap.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).contains(entry.getValue());
            }
        }
        return false;
    }
    /**
     * 
    * @Time:2020年12月21日
    * @Description: 检查是否拥有某个权限，如果有，则返回true，没有则抛异常  
    * @return boolean
     */
    public boolean checkAndNoPermissionThrowException() {
        if (!check()) {
            for (Map.Entry<Long, ProcessTaskOperationType> entry : checkOperationTypeMap.entrySet()) {
                Map<ProcessTaskOperationType, ProcessTaskPermissionDeniedException> map = operationTypePermissionDeniedExceptionMap.get(entry.getKey());
                if (MapUtils.isNotEmpty(map)) {
                    ProcessTaskPermissionDeniedException exception = map.get(entry.getValue());
                    if (exception != null) {
                        throw exception;
                    }
                }
                throw new ProcessTaskNoPermissionException(entry.getValue().getText());
            }
        }
        return true;
    }

    /**
     * 根据工单id或步骤id和操作权限类型获取没有权限的原因
     * @param id 工单id或步骤id
     * @param operationType 操作权限类型
     * @return
     */
    public ProcessTaskPermissionDeniedException getProcessTaskPermissionDeniedException(Long id, ProcessTaskOperationType operationType) {
        Map<ProcessTaskOperationType, ProcessTaskPermissionDeniedException> map = operationTypePermissionDeniedExceptionMap.get(id);
        if (MapUtils.isNotEmpty(map)) {
            return map.get(operationType);
        }
        return null;
    }
}
