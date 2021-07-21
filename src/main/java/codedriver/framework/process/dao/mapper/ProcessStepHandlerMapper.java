package codedriver.framework.process.dao.mapper;

import java.util.List;

import codedriver.framework.process.dto.ProcessStepHandlerVo;

public interface ProcessStepHandlerMapper {

    public List<ProcessStepHandlerVo> getProcessStepHandlerConfig();

	public String getProcessStepHandlerConfigByHandler(String handler);

    public int replaceProcessStepHandlerConfig(ProcessStepHandlerVo stepHandlerVo);
}
