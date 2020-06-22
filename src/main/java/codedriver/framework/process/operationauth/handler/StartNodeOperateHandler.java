package codedriver.framework.process.operationauth.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import codedriver.framework.process.operationauth.core.OperationAuthHandlerBase;
import codedriver.framework.process.operationauth.core.OperationAuthHandlerType;

@Component
public class StartNodeOperateHandler extends OperationAuthHandlerBase {

	@Override
	public Set<String> getOperateList(Long processTaskId, Long processTaskStepId) {
		// TODO Auto-generated method stub
		Set<String> list = new HashSet<>();
		list.add("ACTION1");
		return list;
	}

	@Override
	public OperationAuthHandlerType getHandler() {
		return OperationAuthHandlerType.START;
	}

}
