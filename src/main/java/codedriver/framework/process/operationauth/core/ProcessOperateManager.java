package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.operationauth.OperationAuthHandlerNotFoundException;

public class ProcessOperateManager {

	private List<IOperationAuthHandlerType> typeQueue;
	private List<Long> processTaskIdList;
	private Map<Long, List<Long>> processTaskStepIdListMap;
	private List<ProcessTaskOperationType> operationTypeList;
	private List<String> userUuidList;
	private ProcessTaskMapper processTaskMapper;
	public static class Builder {
	    private List<IOperationAuthHandlerType> typeQueue = new LinkedList<>();
	    private List<Long> processTaskIdList = new ArrayList<>();
	    private Map<Long, List<Long>> processTaskStepIdListMap = new HashMap<>();
	    private List<ProcessTaskOperationType> operationTypeList = new ArrayList<>();
	    private List<String> userUuidList = new ArrayList<>();
	    private ProcessTaskMapper processTaskMapper;
	    public Builder setNext(IOperationAuthHandlerType type) {
	        if(OperationAuthHandlerFactory.getHandler(type.getValue()) == null) {
	            throw new OperationAuthHandlerNotFoundException(type.getText());
	        }
	        typeQueue.add(type);
	        return this;
	    }
	    public Builder addProcessTaskId(Long processTaskId) {
	        if(!processTaskIdList.contains(processTaskId)) {
	            processTaskIdList.add(processTaskId);
	        }
	        return this;
	    }
	    public Builder addProcessTaskStepId(Long processTaskId, Long processTaskStepId) {
	        processTaskStepIdListMap.computeIfAbsent(processTaskId, k -> new ArrayList<>()).add(processTaskStepId);
	        if(!processTaskIdList.contains(processTaskId)) {
                processTaskIdList.add(processTaskId);
            }
	        return this;
	    }
	    public Builder addOperationType(ProcessTaskOperationType operationType) {
	        if(!operationTypeList.contains(operationType)) {
	            operationTypeList.add(operationType);
	        }
	        return this;
	    }
	    public Builder addUserUuid(String userUuid) {
	        if(!userUuidList.contains(userUuid)) {
	            userUuidList.add(userUuid);
	        }
	        return this;
	    }
		public Builder setProcessTaskMapper(ProcessTaskMapper processTaskMapper) {
            this.processTaskMapper = processTaskMapper;
            return this;
        }
        public ProcessOperateManager build() {
			return new ProcessOperateManager(this);
		}
	}

	private ProcessOperateManager(Builder builder) {
		this.typeQueue = builder.typeQueue;
		this.processTaskIdList = builder.processTaskIdList;
		this.processTaskStepIdListMap = builder.processTaskStepIdListMap;
		this.operationTypeList = builder.operationTypeList;
		this.processTaskMapper = builder.processTaskMapper;
		this.userUuidList = builder.userUuidList;
	}
	
	public Map<Long, Set<ProcessTaskOperationType>> check() {
	    Map<Long, Set<ProcessTaskOperationType>> resultMap = new HashMap<>();
	    if(CollectionUtils.isEmpty(processTaskIdList)) {
	        return resultMap;
	    }
	    if(CollectionUtils.isEmpty(userUuidList)) {
	        return resultMap;
	    }
	    if(processTaskMapper == null) {
	        return resultMap;
	    }
	    List<ProcessTaskVo> processTaskList = new ArrayList<>();//processTaskMapper.getProcessTaskListByIdList(processTaskIdList);
	    for(ProcessTaskVo processTaskVo : processTaskList) {
	        if (OperationAuthHandlerType.TASK.getOperationTypeList().removeAll(operationTypeList)) {
	            IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(OperationAuthHandlerType.TASK.getValue());
	            Set<ProcessTaskOperationType> resultSet = new HashSet<>();
	            for(String userUuid : userUuidList) {
	                Map<ProcessTaskOperationType, Boolean> operateMap = handler.getOperateMap(processTaskVo, userUuid, operationTypeList);
                    for(Entry<ProcessTaskOperationType, Boolean> entry : operateMap.entrySet()) {
                        if(entry.getValue() == Boolean.TRUE) {
                            resultSet.add(entry.getKey());
                        }
                    }
                    resultMap.put(processTaskVo.getId(), resultSet);
	            }
	        }
	        if (OperationAuthHandlerType.STEP.getOperationTypeList().removeAll(operationTypeList)) {
	            List<Long> processTaskStepIdList = processTaskStepIdListMap.get(processTaskVo.getId());
	            if(CollectionUtils.isNotEmpty(processTaskStepIdList)) {
	                IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(OperationAuthHandlerType.STEP.getValue());
	                Map<Long, ProcessTaskStepVo> processTaskStepMap = processTaskVo.getProcessTaskStepList().stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
	                for(Long processTaskStepId : processTaskStepIdList) {
	                    ProcessTaskStepVo processTaskStepVo = processTaskStepMap.get(processTaskStepId);
	                    if(processTaskStepVo != null) {
	                        Set<ProcessTaskOperationType> resultSet = new HashSet<>();
	                        for(String userUuid : userUuidList) {
	                            Map<ProcessTaskOperationType, Boolean> operateMap = handler.getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationTypeList);
	                            IOperationAuthHandler handler2 = OperationAuthHandlerFactory.getHandler(processTaskStepVo.getHandler());
	                            Map<ProcessTaskOperationType, Boolean> nextOperateMap = handler2.getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationTypeList);
	                            if(MapUtils.isNotEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
	                                operateMap.putAll(nextOperateMap);
	                            }else if(MapUtils.isEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
	                                operateMap = nextOperateMap;
	                            }
	                            for(Entry<ProcessTaskOperationType, Boolean> entry : operateMap.entrySet()) {
	                                if(entry.getValue() == Boolean.TRUE) {
	                                    resultSet.add(entry.getKey());
	                                }
	                            }
	                            resultMap.put(processTaskVo.getId(), resultSet);
	                        }
	                    }
	                }
	            }
	        }
	        
	    }
	    return resultMap;
	}
	public List<ProcessTaskOperationType> getOperateList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, String userUuid) {
        List<ProcessTaskOperationType> resultList = new ArrayList<>();
        //系统用户拥有所有权限
        if(SystemUser.SYSTEM.getUserUuid().equals(userUuid)) {
            for(IOperationAuthHandlerType type : typeQueue) {
                for(ProcessTaskOperationType operationType : type.getOperationTypeList()) {
                    if(!resultList.contains(operationType)) {
                        resultList.add(operationType);
                    }
                }
            }
        }else {
            Map<ProcessTaskOperationType, Boolean> operateMap = new HashMap<>();
            for(IOperationAuthHandlerType type : typeQueue) {
                IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(type.getValue());
                Map<ProcessTaskOperationType, Boolean> nextOperateMap = handler.getOperateMap(processTaskVo, processTaskStepVo, userUuid);
                if(MapUtils.isNotEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
                    operateMap.putAll(nextOperateMap);
                }else if(MapUtils.isEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
                    operateMap = nextOperateMap;
                }
            }
            if (MapUtils.isNotEmpty(operateMap)) {
                for(Entry<ProcessTaskOperationType, Boolean> entry : operateMap.entrySet()) {
                    if(entry.getValue() == Boolean.TRUE) {
                        resultList.add(entry.getKey());
                    }
                }
                return resultList;
            }
        }
        return resultList;
    }

    public List<ProcessTaskOperationType> getOperateList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, String userUuid, List<ProcessTaskOperationType> operationTypeList) {
        //系统用户拥有所有权限
        if(SystemUser.SYSTEM.getUserUuid().equals(userUuid)) {
            return operationTypeList;
        }
        List<ProcessTaskOperationType> resultList = new ArrayList<>();
        Map<ProcessTaskOperationType, Boolean> operateMap = new HashMap<>();
        for(IOperationAuthHandlerType type : typeQueue) {
            IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(type.getValue());
            Map<ProcessTaskOperationType, Boolean> nextOperateMap = handler.getOperateMap(processTaskVo, processTaskStepVo, userUuid, operationTypeList);
            if(MapUtils.isNotEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
                operateMap.putAll(nextOperateMap);
            }else if(MapUtils.isEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
                operateMap = nextOperateMap;
            }
        }
        if (MapUtils.isNotEmpty(operateMap)) {
            for(Entry<ProcessTaskOperationType, Boolean> entry : operateMap.entrySet()) {
                if(entry.getValue() == Boolean.TRUE) {
                    resultList.add(entry.getKey());
                }
            }
            return resultList;
        }
        return resultList;
    }
}
