package codedriver.framework.process.operate.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import codedriver.framework.process.operate.core.OperateHandlerType;
import codedriver.framework.process.operate.core.ProcessOperateHandlerBase;

@Component
public class StartNodeOperateHandler extends ProcessOperateHandlerBase {

	@Override
	public Set<String> getOperateList(Long processTaskId, Long processTaskStepId) {
		// TODO Auto-generated method stub
		Set<String> list = new HashSet<>();
		list.add("ACTION1");
		return list;
	}

	@Override
	public OperateHandlerType getHandler() {
		return OperateHandlerType.START;
	}

}
