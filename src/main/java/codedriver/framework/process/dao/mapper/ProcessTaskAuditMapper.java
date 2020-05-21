package codedriver.framework.process.dao.mapper;

import java.util.List;

import codedriver.framework.process.dto.ProcessTaskStepAuditVo;

public interface ProcessTaskAuditMapper {
	
	public List<ProcessTaskStepAuditVo> getProcessTaskAuditList(ProcessTaskStepAuditVo processTaskStepAuditVo);

}
