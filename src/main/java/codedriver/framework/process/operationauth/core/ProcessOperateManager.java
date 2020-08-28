package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;

import codedriver.framework.process.constvalue.ProcessTaskOperationType;

public class ProcessOperateManager {
	private OperationAuthHandlerBase handler;

	public static class Builder {
		private OperationAuthHandlerBase handler;

		public Builder setNext(IOperationAuthHandlerType type) {
			if (handler == null) {
				handler = OperationAuthHandlerFactory.getHandler(type);
			} else {
				handler.setNext(OperationAuthHandlerFactory.getHandler(type));
			}
			return this;
		}

		public ProcessOperateManager build() {
			return new ProcessOperateManager(this);
		}
	}

	private ProcessOperateManager(Builder builder) {
		this.handler = builder.handler;
	}

	public List<ProcessTaskOperationType> getOperateList(Long processTaskId, Long processTaskStepId) {
		//开始执行区
		
		//开始执行区
        List<ProcessTaskOperationType> resultList = new ArrayList<>();
		if (this.handler != null) {
			Map<ProcessTaskOperationType, Boolean> operateMap = this.handler.getFinalOperateMap(processTaskId, processTaskStepId);
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
	    List<ProcessTaskOperationType> resultList = new ArrayList<>();
        if (this.handler != null) {
            Map<ProcessTaskOperationType, Boolean> operateMap = this.handler.getFinalOperateMap(processTaskId, processTaskStepId, operationTypeList);
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
}
