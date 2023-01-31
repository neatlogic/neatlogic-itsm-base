package neatlogic.framework.process.dao.mapper;

import java.util.List;

import neatlogic.framework.process.dto.ProcessStepHandlerVo;

public interface ProcessStepHandlerMapper {

    public List<ProcessStepHandlerVo> getProcessStepHandlerConfig();

	public String getProcessStepHandlerConfigByHandler(String handler);

    public int replaceProcessStepHandlerConfig(ProcessStepHandlerVo stepHandlerVo);
}
