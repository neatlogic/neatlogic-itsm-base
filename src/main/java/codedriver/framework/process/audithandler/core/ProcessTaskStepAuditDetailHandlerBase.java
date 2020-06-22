package codedriver.framework.process.audithandler.core;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;

public abstract class ProcessTaskStepAuditDetailHandlerBase implements IProcessTaskStepAuditDetailHandler {
	
	protected static ProcessTaskMapper processTaskMapper;
	
	@Autowired
	private void setProcessTaskMapper(ProcessTaskMapper _processTaskMapper) {
		processTaskMapper = _processTaskMapper;
	}

	@Override
	public void handle(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo) {
		String oldContent = processTaskStepAuditDetailVo.getOldContent();
		if(StringUtils.isNotBlank(oldContent)) {
			ProcessTaskContentVo processTaskContentVo = processTaskMapper.getProcessTaskContentByHash(oldContent);
			if(processTaskContentVo != null) {
				processTaskStepAuditDetailVo.setOldContent(processTaskContentVo.getContent());
			}
		}
		String newContent = processTaskStepAuditDetailVo.getNewContent();
		if(StringUtils.isNotBlank(newContent)) {
			ProcessTaskContentVo processTaskContentVo = processTaskMapper.getProcessTaskContentByHash(newContent);
			if(processTaskContentVo != null) {
				processTaskStepAuditDetailVo.setNewContent(processTaskContentVo.getContent());
			}
		}
		myHandle(processTaskStepAuditDetailVo);		
	}

	protected abstract void myHandle(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);
}
