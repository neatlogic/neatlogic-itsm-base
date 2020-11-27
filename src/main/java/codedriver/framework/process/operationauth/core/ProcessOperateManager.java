package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;

import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.operationauth.OperationAuthHandlerNotFoundException;

public class ProcessOperateManager {

	private List<IOperationAuthHandlerType> typeQueue;
	public static class Builder {
	    private List<IOperationAuthHandlerType> typeQueue = new LinkedList<>();
	    public Builder setNext(IOperationAuthHandlerType type) {
	        if(OperationAuthHandlerFactory.getHandler(type) == null) {
	            throw new OperationAuthHandlerNotFoundException(type.getText());
	        }
	        typeQueue.add(type);
	        return this;
	    }
		public ProcessOperateManager build() {
			return new ProcessOperateManager(this);
		}
	}

	private ProcessOperateManager(Builder builder) {
		this.typeQueue = builder.typeQueue;
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
                IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(type);
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
            IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(type);
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
