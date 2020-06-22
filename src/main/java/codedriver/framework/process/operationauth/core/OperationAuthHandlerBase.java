package codedriver.framework.process.operationauth.core;

import java.util.Set;

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
	public final Set<String> getFinalOperateList(Long processTaskId, Long processTaskStepId) {
		Set<String> operateList = getOperateList(processTaskId, processTaskStepId);
		if (nextHandler != null) {
			Set<String> nextOperateList = nextHandler.getFinalOperateList(processTaskId, processTaskStepId);
			if (operateList != null && nextOperateList != null) {
				operateList.addAll(nextOperateList);
				return operateList;
			} else if (operateList == null && nextOperateList != null) {
				return nextOperateList;
			} else {
				return null;
			}
		} else {
			return operateList;
		}
	}

	public abstract Set<String> getOperateList(Long processTaskId, Long processTaskStepId);

}
