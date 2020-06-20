package codedriver.framework.process.operate.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.operate.core.OperateHandlerType;
import codedriver.framework.process.operate.core.ProcessOperateHandlerBase;

@Component
public class SubTaskOperateHandler extends ProcessOperateHandlerBase {

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
	public OperateHandlerType getHandler() {
		return OperateHandlerType.SUBTASK;
	}

}
