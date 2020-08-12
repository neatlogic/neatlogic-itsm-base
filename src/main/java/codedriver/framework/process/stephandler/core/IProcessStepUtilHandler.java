package codedriver.framework.process.stephandler.core;

public interface IProcessStepUtilHandler {

	public String getHandler();
	
	/**
	 * 
	 * @Description: 活动审计
	 * @param currentProcessTaskStepVo
	 * @param action
	 * @return void
	 */
	//public void activityAudit(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskStepAction action);

	/**
	 * 
	* @Time:2020年7月27日
	* @Description: 处理器特有的步骤信息
	* @param @return 
	* @return Object
	 */
	public Object getHandlerStepInfo(Long processTaskStepId);
}
