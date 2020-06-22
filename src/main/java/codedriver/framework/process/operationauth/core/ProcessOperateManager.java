package codedriver.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	public List<String> getOperateList(Long processTaskId, Long processTaskStepId) {
		//开始执行区
		
		//开始执行区
		if (this.handler != null) {
			Set<String> set = this.handler.getFinalOperateList(processTaskId, processTaskStepId);
			if (set != null) {
				return new ArrayList<String>(set);
			}
		}
		//结束操作区
		
		
		//结束操作区
		return new ArrayList<>();
	}

}
