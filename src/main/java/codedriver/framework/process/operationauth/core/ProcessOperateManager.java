package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.dao.mapper.UserMapper;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.processtask.ProcessTaskNoPermissionException;

public class ProcessOperateManager {

    private Set<Long> processTaskIdSet;
    private Map<Long, Set<Long>> processTaskStepIdSetMap;
    private Set<ProcessTaskOperationType> operationTypeSet;
    private ProcessTaskMapper processTaskMapper;
    private UserMapper userMapper;
    private boolean isThrowException;
    private Map<Long, Set<ProcessTaskOperationType>> checkOperationTypeSetMap;

    public static class Builder {
        private Set<Long> processTaskIdSet = new HashSet<>();
        private Map<Long, Set<Long>> processTaskStepIdSetMap = new HashMap<>();
        private Set<ProcessTaskOperationType> operationTypeSet = new HashSet<>();
        private ProcessTaskMapper processTaskMapper;
        private UserMapper userMapper;
        private boolean isThrowException;
        private Map<Long, Set<ProcessTaskOperationType>> checkOperationTypeSetMap = new HashMap<>();

        public Builder(ProcessTaskMapper processTaskMapper, UserMapper userMapper) {
            this.processTaskMapper = processTaskMapper;
            this.userMapper = userMapper;
        }

        public Builder addProcessTaskId(Long processTaskId) {
            processTaskIdSet.add(processTaskId);
            return this;
        }

        public Builder addProcessTaskStepId(Long processTaskId, Long processTaskStepId) {
            if (processTaskStepId != null) {
                processTaskStepIdSetMap.computeIfAbsent(processTaskId, k -> new HashSet<>()).add(processTaskStepId);
            }
            processTaskIdSet.add(processTaskId);
            return this;
        }

        public Builder addOperationType(ProcessTaskOperationType operationType) {
            operationTypeSet.add(operationType);
            return this;
        }

        public Builder addCheckOperationType(Long id, ProcessTaskOperationType operationType) {
            checkOperationTypeSetMap.computeIfAbsent(id, k -> new HashSet<>()).add(operationType);
            return this;
        }

        public Builder withIsThrowException(boolean isThrowException) {
            this.isThrowException = isThrowException;
            return this;
        }

        public ProcessOperateManager build() {
            return new ProcessOperateManager(this);
        }
    }

    private ProcessOperateManager(Builder builder) {
        this.processTaskIdSet = builder.processTaskIdSet;
        this.processTaskStepIdSetMap = builder.processTaskStepIdSetMap;
        this.operationTypeSet = builder.operationTypeSet;
        this.processTaskMapper = builder.processTaskMapper;
        this.userMapper = builder.userMapper;
        this.isThrowException = builder.isThrowException;
        this.checkOperationTypeSetMap = builder.checkOperationTypeSetMap;
    }

    public Map<Long, Set<ProcessTaskOperationType>> getOperateMap() {
        Map<Long, Set<ProcessTaskOperationType>> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(processTaskIdSet)) {
            return resultMap;
        }
        if (processTaskMapper == null) {
            return resultMap;
        }
        if (userMapper == null) {
            return resultMap;
        }
        List<String> userUuidList = new ArrayList<>();
        userUuidList.add(UserContext.get().getUserUuid(true));
        /** 如果当前用户接受了其他用户的授权，查出其他用户拥有的权限，叠加当前用户权限里 **/
        if (!SystemUser.SYSTEM.getUserUuid().equals(UserContext.get().getUserUuid(true))) {
            String uuid = userMapper.getUserUuidByAgentUuidAndFunc(UserContext.get().getUserUuid(true), "processtask");
            if (StringUtils.isNotBlank(uuid)) {
                userUuidList.add(uuid);
            }
        }

        List<ProcessTaskVo> processTaskList = processTaskMapper.getProcessTaskDetailListByIdList(new ArrayList<>(processTaskIdSet));
        for (ProcessTaskVo processTaskVo : processTaskList) {
            getOperateMap(processTaskVo, userUuidList, operationTypeSet, resultMap);
        }
        return resultMap;
    }

    private void getOperateMap(ProcessTaskVo processTaskVo, List<String> userUuidList,
        Set<ProcessTaskOperationType> operationTypeSet, Map<Long, Set<ProcessTaskOperationType>> resultMap) {
        if (CollectionUtils.isNotEmpty(operationTypeSet)
            || OperationAuthHandlerType.TASK.getOperationTypeList().removeAll(operationTypeSet)) {
            IOperationAuthHandler handler =
                OperationAuthHandlerFactory.getHandler(OperationAuthHandlerType.TASK.getValue());
            Set<ProcessTaskOperationType> resultSet = new HashSet<>();
            for (String userUuid : userUuidList) {
                // 系统用户拥有所有权限
                if (SystemUser.SYSTEM.getUserUuid().equals(userUuid)) {
                    if (CollectionUtils.isNotEmpty(operationTypeSet)) {
                        resultSet.addAll(operationTypeSet);
                    } else {
                        resultSet.addAll(OperationAuthHandlerType.TASK.getOperationTypeList());
                    }
                } else {
                    Map<ProcessTaskOperationType, Boolean> operateMap =
                        handler.getOperateMap(processTaskVo, userUuid, operationTypeSet);
                    for (Entry<ProcessTaskOperationType, Boolean> entry : operateMap.entrySet()) {
                        if (entry.getValue() == Boolean.TRUE) {
                            resultSet.add(entry.getKey());
                        }
                    }
                }
                resultMap.put(processTaskVo.getId(), resultSet);
            }
        }
        if (CollectionUtils.isNotEmpty(operationTypeSet)
            || OperationAuthHandlerType.STEP.getOperationTypeList().removeAll(operationTypeSet)) {
            Set<Long> processTaskStepIdList = processTaskStepIdSetMap.get(processTaskVo.getId());
            if (CollectionUtils.isNotEmpty(processTaskStepIdList)) {
                IOperationAuthHandler stepHandler =
                    OperationAuthHandlerFactory.getHandler(OperationAuthHandlerType.STEP.getValue());
                Map<Long, ProcessTaskStepVo> processTaskStepMap =
                    processTaskVo.getStepList().stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
                for (Long processTaskStepId : processTaskStepIdList) {
                    ProcessTaskStepVo processTaskStepVo = processTaskStepMap.get(processTaskStepId);
                    if (processTaskStepVo != null) {
                        Set<ProcessTaskOperationType> resultSet = new HashSet<>();
                        for (String userUuid : userUuidList) {
                            // 系统用户拥有所有权限
                            if (SystemUser.SYSTEM.getUserUuid().equals(userUuid)) {
                                if (CollectionUtils.isNotEmpty(operationTypeSet)) {
                                    resultSet.addAll(operationTypeSet);
                                } else {
                                    resultSet.addAll(OperationAuthHandlerType.STEP.getOperationTypeList());
                                }
                            } else {
                                Map<ProcessTaskOperationType, Boolean> operateMap = stepHandler
                                    .getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationTypeSet);
                                IOperationAuthHandler handler =
                                    OperationAuthHandlerFactory.getHandler(processTaskStepVo.getHandler());
                                Map<ProcessTaskOperationType, Boolean> nextOperateMap = handler
                                    .getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationTypeSet);
                                if (MapUtils.isNotEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
                                    operateMap.putAll(nextOperateMap);
                                } else if (MapUtils.isEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
                                    operateMap = nextOperateMap;
                                }
                                for (Entry<ProcessTaskOperationType, Boolean> entry : operateMap.entrySet()) {
                                    if (entry.getValue() == Boolean.TRUE) {
                                        resultSet.add(entry.getKey());
                                    }
                                }
                            }
                            resultMap.put(processTaskStepVo.getId(), resultSet);
                        }
                    }
                }
            }
        }
    }

    public boolean check() {
        if (MapUtils.isNotEmpty(checkOperationTypeSetMap)) {
            Map<Long, Set<ProcessTaskOperationType>> resultMap = getOperateMap();
            for (Map.Entry<Long, Set<ProcessTaskOperationType>> entry : checkOperationTypeSetMap.entrySet()) {
                Set<ProcessTaskOperationType> value = entry.getValue();
                Set<ProcessTaskOperationType> resultSet = resultMap.get(entry.getKey());
                if (CollectionUtils.isNotEmpty(resultSet)) {
                    value.removeAll(resultSet);
                }
                if (CollectionUtils.isNotEmpty(value)) {
                    if (isThrowException) {
                        List<String> operationTypeTextList = new ArrayList<>();
                        for (ProcessTaskOperationType operationType : value) {
                            operationTypeTextList.add(operationType.getText());
                        }
                        throw new ProcessTaskNoPermissionException(String.join("、", operationTypeTextList));
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
