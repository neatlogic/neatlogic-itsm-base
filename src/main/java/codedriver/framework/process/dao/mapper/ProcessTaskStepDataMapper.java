package codedriver.framework.process.dao.mapper;

import java.util.List;

import codedriver.framework.process.dto.ProcessTaskStepDataVo;

public interface ProcessTaskStepDataMapper {
	public ProcessTaskStepDataVo getProcessTaskStepData(ProcessTaskStepDataVo processTaskStepDataVo);
	
	public List<ProcessTaskStepDataVo> searchProcessTaskStepData(ProcessTaskStepDataVo processTaskStepDataVo);

	public int replaceProcessTaskStepData(ProcessTaskStepDataVo processTaskStepDataVo);
	
	public int deleteProcessTaskStepData(ProcessTaskStepDataVo processTaskStepDataVo);
	
	public int deleteProcessTaskStepDataById(Long id);
}
