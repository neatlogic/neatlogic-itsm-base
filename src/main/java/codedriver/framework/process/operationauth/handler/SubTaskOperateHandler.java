package codedriver.framework.process.operationauth.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.operationauth.core.OperationAuthHandlerBase;
import codedriver.framework.process.operationauth.core.OperationAuthHandlerType;

@Component
public class SubTaskOperateHandler extends OperationAuthHandlerBase {

	@Autowired
	private ProcessTaskMapper processTaskMapper;

	@Override
	public Set<String> getOperateList(Long processTaskId, Long processTaskStepId) {
		// TODO Auto-generated method stub
		Set<String> list = new HashSet<>();
		processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
		list.add("SUBTSSK");
		return list;
	}

	@Override
	public OperationAuthHandlerType getHandler() {
		return OperationAuthHandlerType.SUBTASK;
	}

}
