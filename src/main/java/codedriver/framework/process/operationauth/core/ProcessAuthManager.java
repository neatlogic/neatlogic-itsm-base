package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import codedriver.framework.process.dao.mapper.SelectContentByHashMapper;
import codedriver.framework.process.dto.*;
import codedriver.framework.process.service.ProcessTaskAgentService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
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
    private static ProcessTaskAgentService processTaskAgentService;
    private static SelectContentByHashMapper selectContentByHashMapper;
    @Autowired
    private ProcessAuthManager(ProcessTaskAgentService _processTaskAgentService, ProcessTaskMapper _processTaskMapper, SelectContentByHashMapper _selectContentByHashMapper) {
        processTaskAgentService = _processTaskAgentService;
        processTaskMapper = _processTaskMapper;
        selectContentByHashMapper = _selectContentByHashMapper;
    }

    private Set<Long> processTaskIdSet;
    private Set<Long> processTaskStepIdSet;
    private Map<Long, Set<Long>> processTaskStepIdSetMap;
    private Set<ProcessTaskOperationType> operationTypeSet;
    private Map<Long, ProcessTaskOperationType> checkOperationTypeMap;

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

        public TaskOperationChecker(Long processTaskId, ProcessTaskOperationType operationType) {
            this.processTaskId = processTaskId;
            this.operationType = operationType;
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

        public StepOperationChecker(Long processTaskStepId, ProcessTaskOperationType operationType) {
            this.processTaskStepId = processTaskStepId;
            this.operationType = operationType;
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
    }

    private ProcessAuthManager(StepOperationChecker checker) {
        this.processTaskStepIdSet = new HashSet<>();
        processTaskStepIdSet.add(checker.processTaskStepId);
        this.operationTypeSet = new HashSet<>();
        operationTypeSet.add(checker.operationType);
        this.checkOperationTypeMap = new HashMap<>();
        checkOperationTypeMap.put(checker.processTaskStepId, checker.operationType);
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

            List<String> userUuidList = new ArrayList<>();
            userUuidList.add(UserContext.get().getUserUuid(true));
            List<ProcessTaskVo> processTaskList = processTaskMapper.getProcessTaskListByIdList(processTaskIdList);
            Set<String> hashSet = processTaskList.stream().map(ProcessTaskVo::getConfigHash).collect(Collectors.toSet());
//            long startTime3 = System.currentTimeMillis();
            List<ProcessTaskConfigVo> processTaskConfigList = selectContentByHashMapper.getProcessTaskConfigListByHashList(new ArrayList<>(hashSet));
//            logger.error("D:" + (System.currentTimeMillis() - startTime3));
            Map<String, String> processTaskConfigMap = processTaskConfigList.stream().collect(Collectors.toMap(e->e.getHash(), e -> e.getConfig()));
//            logger.error("A:" + (System.currentTimeMillis() - startTime));
            Map<String, List<String>> channelUuidFromUserUuidList = new HashMap<>();
            for (ProcessTaskVo processTaskVo : processTaskList) {
                processTaskVo.setConfig(processTaskConfigMap.get(processTaskVo.getConfigHash()));
//                startTime = System.currentTimeMillis();
                processTaskVo.setStepList(processTaskStepListMap.computeIfAbsent(processTaskVo.getId(), k -> new ArrayList<>()));
                processTaskVo.setStepRelList(processTaskStepRelListMap.computeIfAbsent(processTaskVo.getId(), k -> new ArrayList<>()));
                /** 如果当前用户接受了其他用户的授权，查出其他用户拥有的权限，叠加当前用户权限里 **/
                if (!SystemUser.SYSTEM.getUserUuid().equals(UserContext.get().getUserUuid(true))) {
//                    String uuid = userMapper.getUserUuidByAgentUuidAndFunc(UserContext.get().getUserUuid(true), "processtask");
//                    if (StringUtils.isNotBlank(uuid)) {
//                        userUuidList.add(uuid);
//                    }
                    String channelUuid = processTaskVo.getChannelUuid();
                    List<String> fromUserUuidList = channelUuidFromUserUuidList.get(channelUuid);
                    if (fromUserUuidList == null) {
                        long startTime2 = System.currentTimeMillis();
                        fromUserUuidList = processTaskAgentService.getFromUserUuidListByToUserUuidAndChannelUuid(UserContext.get().getUserUuid(true), channelUuid);
                        logger.error("c(" + channelUuid + "):" + (System.currentTimeMillis() - startTime2));
                        channelUuidFromUserUuidList.put(channelUuid, fromUserUuidList);
                    }
                    userUuidList.addAll(fromUserUuidList);
                }
                resultMap.putAll(getOperateMap(processTaskVo, userUuidList, operationTypeSet));
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
    private Map<Long, Set<ProcessTaskOperationType>> getOperateMap(ProcessTaskVo processTaskVo, List<String> userUuidList,
        Set<ProcessTaskOperationType> operationTypeSet) {
        Map<Long, Set<ProcessTaskOperationType>> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(operationTypeSet)
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
        if (CollectionUtils.isEmpty(operationTypeSet)
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
                                if (handler != null) {
                                    Map<ProcessTaskOperationType, Boolean> nextOperateMap = handler
                                        .getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationTypeSet);
                                    if (MapUtils.isNotEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
                                        operateMap.putAll(nextOperateMap);
                                    } else if (MapUtils.isEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
                                        operateMap = nextOperateMap;
                                    }
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
        return resultMap;
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
                throw new ProcessTaskNoPermissionException(entry.getValue().getText());
            }
        }
        return true;
    }
}
