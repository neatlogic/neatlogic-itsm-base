package codedriver.framework.process.dao.mapper;

import java.util.List;

import codedriver.framework.process.dto.ProcessStepHandlerVo;

public interface ProcessStepHandlerMapper {

    public List<ProcessStepHandlerVo> getProcessStepHandlerConfig();

    public void updateProcessStepHandlerConfig(ProcessStepHandlerVo stepHandlerVo);

    public void deleteProcessStepHandlerConfigByHandler(String handler);

    public void insertProcessStepHandlerConfig(ProcessStepHandlerVo stepHandlerVo);
}
