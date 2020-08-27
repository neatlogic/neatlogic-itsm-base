package codedriver.framework.process.operationauth.core;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;

public abstract class OperationAuthHandlerBase implements IOperationAuthHandler {

	private OperationAuthHandlerBase nextHandler;
	private OperationAuthHandlerBase prevHandler;

	public OperationAuthHandlerBase setNext(OperationAuthHandlerBase next) {
		if (next != null) {
			OperationAuthHandlerBase temp = moveToLast();
			temp.nextHandler = next;
			next.prevHandler = temp;
		}
		return next;
	}

	public OperationAuthHandlerBase moveToLast() {
		OperationAuthHandlerBase firstHandler = this;
		while (firstHandler.nextHandler != null) {
			firstHandler = firstHandler.nextHandler;
		}
		return firstHandler;
	}

	public OperationAuthHandlerBase moveToFirst() {
		OperationAuthHandlerBase firstHandler = this;
		while (firstHandler.prevHandler != null) {
			firstHandler = firstHandler.prevHandler;
		}
		return firstHandler;
	}

	@Override
	public final Map<String, Boolean> getFinalOperateMap(Long processTaskId, Long processTaskStepId) {
		Map<String, Boolean> operateMap = getOperateMap(processTaskId, processTaskStepId);
		if (nextHandler != null) {
		    Map<String, Boolean> nextOperateMap = nextHandler.getFinalOperateMap(processTaskId, processTaskStepId);
		    if(MapUtils.isNotEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
	            operateMap.putAll(nextOperateMap);
		    }else if(MapUtils.isEmpty(operateMap) && MapUtils.isNotEmpty(nextOperateMap)) {
		        operateMap = nextOperateMap;
		    }
		}
		return operateMap;
	}

	public abstract Map<String, Boolean> getOperateMap(Long processTaskId, Long processTaskStepId);

}
