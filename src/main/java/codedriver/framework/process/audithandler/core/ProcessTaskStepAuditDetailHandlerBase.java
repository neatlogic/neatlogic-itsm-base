package codedriver.framework.process.audithandler.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import codedriver.framework.process.dao.mapper.SelectContentByHashMapper;
import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;

public abstract class ProcessTaskStepAuditDetailHandlerBase implements IProcessTaskStepAuditDetailHandler {
	
	protected static SelectContentByHashMapper selectContentByHashMapper;
	
	@Autowired
	private void setSelectContentByHashMapper(SelectContentByHashMapper _selectContentByHashMapper) {
	    selectContentByHashMapper = _selectContentByHashMapper;
	}

	@Override
	public int handle(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo) {
		String oldContent = processTaskStepAuditDetailVo.getOldContent();
		if(StringUtils.isNotBlank(oldContent)) {
			processTaskStepAuditDetailVo.setOldContent(selectContentByHashMapper.getProcessTaskContentStringByHash(oldContent));
		}
		String newContent = processTaskStepAuditDetailVo.getNewContent();
		if(StringUtils.isNotBlank(newContent)) {
			processTaskStepAuditDetailVo.setNewContent(selectContentByHashMapper.getProcessTaskContentStringByHash(newContent));
		}
		return myHandle(processTaskStepAuditDetailVo);		
	}

	protected abstract int myHandle(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo);
}
