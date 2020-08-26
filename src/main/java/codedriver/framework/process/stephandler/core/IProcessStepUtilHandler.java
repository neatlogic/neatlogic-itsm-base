package codedriver.framework.process.stephandler.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.process.audithandler.core.IProcessTaskAuditType;
import codedriver.framework.process.constvalue.OperationType;
import codedriver.framework.process.constvalue.ProcessTaskStepAction;
import codedriver.framework.process.dto.ProcessStepVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.notify.core.NotifyTriggerType;

public interface IProcessStepUtilHandler {

	public String getHandler();

	/**
	 * 
	* @Time:2020年7月27日
	* @Description: 处理器特有的步骤信息
	* @param @return 
	* @return Object
	 */
	public Object getHandlerStepInfo(ProcessTaskStepVo currentProcessTaskStepVo);
	/**
	 * 
	* @Time:2020年8月12日
	* @Description: 处理器特有的步骤初始化信息
	* @param @return 
	* @return Object
	 */
	public Object getHandlerStepInitInfo(ProcessTaskStepVo currentProcessTaskStepVo);

	/**
	 * @Author: chenqiwei
	 * @Time:Feb 10, 2020
	 * @Description: 组装步骤节点信息
	 * @param @param processStepVo
	 * @param @param stepConfigObj
	 * @return void
	 */
	public void makeupProcessStep(ProcessStepVo processStepVo, JSONObject stepConfigObj);
	
	/**
	 * 
	 * @Description: 子任务状态发生变化后，对子任务处理人的在
	 *               processtask_step_worker表和processtask_step_user表的数据做对应的变化
	 * @param processTaskId
	 * @param processTaskStepId
	 * @return void
	 */
	public void updateProcessTaskStepUserAndWorker(Long processTaskId, Long processTaskStepId);

	/**
	 * 
	 * @Description: 活动审计
	 * @param currentProcessTaskStepVo
	 * @param action
	 * @return void
	 */
	public void activityAudit(ProcessTaskStepVo currentProcessTaskStepVo, IProcessTaskAuditType action);

	/**
	 * 
	 * @Time:2020年3月30日
	 * @Description: 获取当前用户对当前步骤的所有操作权限列表
	 * @param processTaskId
	 * @param processTaskStepId
	 * @return List<String>
	 */
	public List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId);

	/**
	 * 
	 * @Author: 14378
	 * @Time:2020年4月1日
	 * @Description: 获取当前用户对当前步骤的部分操作权限列表（verifyActionList包含的那部分）
	 * @param processTaskId
	 * @param processTaskStepId
	 * @param verifyActionList
	 * @return List<String>
	 */
	public List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId, List<String> verifyActionList);

	/**
	 * 
	 * @Time:2020年3月30日
	 * @Description: 判断当前用户对当前步骤的某个操作是否有权限
	 * @param processTaskId
	 * @param processTaskStepId
	 * @param action
	 * @return boolean
	 */
	public boolean verifyActionAuthoriy(Long processTaskId, Long processTaskStepId, ProcessTaskStepAction action);

	/**
	 * 
	 * @Time:2020年3月30日
	 * @Description: 当前用户可撤回的步骤列表
	 * @param processTaskId
	 * @return Set<ProcessTaskStepVo>
	 */
	public Set<ProcessTaskStepVo> getRetractableStepList(Long processTaskId);

	/**
	 * 
	 * @Time:2020年3月30日
	 * @Description: 当前用户可处理的步骤列表
	 * @param processTaskId
	 * @return List<ProcessTaskStepVo>
	 */
	public List<ProcessTaskStepVo> getProcessableStepList(Long processTaskId);

	/**
	 * 
	 * @Time:2020年4月18日
	 * @Description: 获取工单中当前用户能催办的步骤列表
	 * @param processTaskId
	 * @return List<ProcessTaskStepVo>
	 */
	public List<ProcessTaskStepVo> getUrgeableStepList(Long processTaskId);

	/**
	 * 
	* @Time:2020年8月13日
	* @Description: 通知 
	* @param currentProcessTaskStepVo
	* @param  trigger 
	* @return void
	 */
	public void notify(ProcessTaskStepVo currentProcessTaskStepVo, NotifyTriggerType trigger);

	/**
	 * 
	 * @Time:2020年3月30日
	 * @Description: 判断当前用户对当前步骤的某个操作是否有权限
	 * @param processTaskId
	 * @param processTaskStepId
	 * @param action
	 * @return boolean
	 */
	public boolean verifyOperationAuthoriy(Long processTaskId, Long processTaskStepId, OperationType operation);
	
	/**
	 * 
	* @Time:2020年6月30日
	* @Description: 构造节点管理配置数据
	* @param configObj
	* @return JSONObject
	 */
	public JSONObject makeupConfig(JSONObject configObj);
	/**
     * 
    * @Author: linbq
    * @Time:2020年8月21日
    * @Description: 获取工单信息 
    * @param processTaskId 工单id
    * @return ProcessTaskVo
     */
    public ProcessTaskVo getProcessTaskDetailById(Long processTaskId);   
    /**
     * 
    * @Author: 14378
    * @Time:2020年7月3日
    * @Description: 获取所有工单干系人信息，用于通知接收人
    * @param processTaskId 工单id
    * @param processTaskStepId 步骤id
    * @param receiverMap 工单干系人信息
    * @return void
     */
    public void getReceiverMap(Long processTaskId, Long processTaskStepId, Map<String, List<NotifyReceiverVo>> receiverMap);
}
