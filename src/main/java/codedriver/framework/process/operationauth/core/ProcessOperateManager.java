package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.exception.operationauth.OperationAuthHandlerNotFoundException;

public class ProcessOperateManager {
//	private OperationAuthHandlerBase handler;

	private List<IOperationAuthHandlerType> list;
	public static class Builder {
//		private OperationAuthHandlerBase handler;
//
//		public Builder setNext(IOperationAuthHandlerType type) {
//			if (handler == null) {
//				handler = OperationAuthHandlerFactory.getHandler(type);
//			} else {
//				handler.setNext(OperationAuthHandlerFactory.getHandler(type));
//			}
//			return this;
//		}
	    private List<IOperationAuthHandlerType> list = new ArrayList<>();
	    public Builder setNext(IOperationAuthHandlerType type) {
	        if(OperationAuthHandlerFactory.getHandler(type) == null) {
	            throw new OperationAuthHandlerNotFoundException(type.getText());
	        }
	        list.add(type);
	        return this;
	    }
		public ProcessOperateManager build() {
			return new ProcessOperateManager(this);
		}
	}

	private ProcessOperateManager(Builder builder) {
//		this.handler = builder.handler;
		this.list = builder.list;
	}

	public List<ProcessTaskOperationType> getOperateList(Long processTaskId, Long processTaskStepId) {
		//开始执行区
		
		//开始执行区
//      List<ProcessTaskOperationType> resultList = new ArrayList<>();
//		if (this.handler != null) {
//			Map<ProcessTaskOperationType, Boolean> operateMap = this.handler.getFinalOperateMap(processTaskId, processTaskStepId);
//			if (MapUtils.isNotEmpty(operateMap)) {
//			    for(Entry<ProcessTaskOperationType, Boolean> entry : operateMap.entrySet()) {
//			        if(entry.getValue() == Boolean.TRUE) {
//			            resultList.add(entry.getKey());
//			        }
//			    }
//				return resultList;
//			}
//		}
	    List<ProcessTaskOperationType> resultList = new ArrayList<>();
	  //系统用户拥有所有权限
        if(SystemUser.SYSTEM.getUserUuid().equals(UserContext.get().getUserUuid())) {
            for(IOperationAuthHandlerType type : list) {
                for(ProcessTaskOperationType operationType : type.getOperationTypeList()) {
                    if(!resultList.contains(operationType)) {
                        resultList.add(operationType);
                    }
                }
            }
        }else {
            Map<ProcessTaskOperationType, Boolean> operateMap = new HashMap<>();
            for(IOperationAuthHandlerType type : list) {
                IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(type);
                Map<ProcessTaskOperationType, Boolean> nextOperateMap = handler.getOperateMap(processTaskId, processTaskStepId);
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
        
		//结束操作区
		
		
		//结束操作区
		return resultList;
	}

	public List<ProcessTaskOperationType> getOperateList(Long processTaskId, Long processTaskStepId, List<ProcessTaskOperationType> operationTypeList) {
	    //系统用户拥有所有权限
	    if(SystemUser.SYSTEM.getUserUuid().equals(UserContext.get().getUserUuid())) {
	        return operationTypeList;
	    }
	    List<ProcessTaskOperationType> resultList = new ArrayList<>();
        Map<ProcessTaskOperationType, Boolean> operateMap = new HashMap<>();
        for(IOperationAuthHandlerType type : list) {
            IOperationAuthHandler handler = OperationAuthHandlerFactory.getHandler(type);
            Map<ProcessTaskOperationType, Boolean> nextOperateMap = handler.getOperateMap(processTaskId, processTaskStepId, operationTypeList);
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
