package codedriver.framework.process.stephandler.core;

import codedriver.framework.process.constvalue.ProcessTaskStepAction;
import codedriver.framework.process.dto.ProcessTaskStepVo;

public interface IProcessStepUtilHandler {

	public String getHandler();
	
	public String getName();
	
	/**
	 * 
	 * @Description: 活动审计
	 * @param currentProcessTaskStepVo
	 * @param action
	 * @return void
	 */
	public void activityAudit(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskStepAction action);
}
