package codedriver.framework.process.stephandler.core;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.process.audithandler.core.IProcessTaskAuditType;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.dto.ProcessStepVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.notify.core.NotifyTriggerType;
import codedriver.framework.process.stepremind.core.IProcessTaskStepRemindType;

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
	* @Time:2020年8月13日
	* @Description: 通知 
	* @param currentProcessTaskStepVo
	* @param  trigger 
	* @return void
	 */
	public void notify(ProcessTaskStepVo currentProcessTaskStepVo, NotifyTriggerType trigger);
	/**
	 * 
	* @Time:2020年11月11日
	* @Description: 触发计算sla 
	* @param currentProcessTaskVo 
	* @return void
	 */
	public void calculateSla(ProcessTaskVo currentProcessTaskVo, boolean isAsync);
	/**
     * 
    * @Time:2020年11月11日
    * @Description: 触发计算sla 
    * @param currentProcessTaskStepVo 
    * @return void
     */
    public void calculateSla(ProcessTaskStepVo currentProcessTaskStepVo);
    /**
     * 
     * @Time:2020年3月30日
     * @Description: 获取当前用户对当前步骤的所有操作权限列表
     * @param processTaskId
     * @param processTaskStepId
     * @return List<String>
     */
    public List<ProcessTaskOperationType> getOperateList(Long processTaskId, Long processTaskStepId);
    /**
     * 
     * @Time:2020年3月30日
     * @Description: 获取当前用户对当前步骤的所有操作权限列表
     * @param processTaskId
     * @param processTaskStepId
     * @return List<String>
     */
    public List<ProcessTaskOperationType> getOperateList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo);
    /**
     * 
     * @Time:2020年3月30日
     * @Description: 获取当前用户对当前步骤的部分操作权限列表（operationTypeList包含的那部分）
     * @param processTaskId
     * @param processTaskStepId
     * @return List<String>
     */
    public List<ProcessTaskOperationType> getOperateList(Long processTaskId, Long processTaskStepId, List<ProcessTaskOperationType> operationTypeList);
    /**
     * 
     * @Time:2020年3月30日
     * @Description: 获取当前用户对当前步骤的部分操作权限列表（operationTypeList包含的那部分）
     * @param processTaskId
     * @param processTaskStepId
     * @return List<String>
     */
    public List<ProcessTaskOperationType> getOperateList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, List<ProcessTaskOperationType> operationTypeList);
	/**
	 * 
	 * @Time:2020年3月30日
	 * @Description: 判断当前用户对当前步骤的某个操作是否有权限
	 * @param processTaskId
	 * @param processTaskStepId
	 * @param action
	 * @return boolean
	 */
	public boolean verifyOperationAuthoriy(Long processTaskId, Long processTaskStepId, ProcessTaskOperationType operationType, boolean isThrowException);
	/**
     * 
     * @Time:2020年3月30日
     * @Description: 判断当前用户对当前步骤的某个操作是否有权限
     * @param processTaskId
     * @param processTaskStepId
     * @param action
     * @return boolean
     */
    public boolean verifyOperationAuthoriy(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, ProcessTaskOperationType operationType, boolean isThrowException);
	/**
     * 
     * @Time:2020年3月30日
     * @Description: 判断当前用户对当前步骤的某个操作是否有权限
     * @param processTaskId
     * @param processTaskStepId
     * @param action
     * @return boolean
     */
    public boolean verifyOperationAuthoriy(Long processTaskId, ProcessTaskOperationType operationType, boolean isThrowException);
    /**
     * 
     * @Time:2020年3月30日
     * @Description: 判断当前用户对当前步骤的某个操作是否有权限
     * @param processTaskId
     * @param processTaskStepId
     * @param action
     * @return boolean
     */
    public boolean verifyOperationAuthoriy(ProcessTaskVo processTaskVo, ProcessTaskOperationType operationType, boolean isThrowException);
    
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
    * @Time:2020年8月21日
    * @Description: 获取工单信息 
    * @param processTaskId 工单id
    * @return ProcessTaskVo
     */
    public ProcessTaskVo getProcessTaskDetailById(Long processTaskId);   
    /**
     * 
    * @Time:2020年7月3日
    * @Description: 获取所有工单干系人信息，用于通知接收人
    * @param processTaskId 工单id
    * @param processTaskStepId 步骤id
    * @param receiverMap 工单干系人信息
    * @return void
     */
    public void getReceiverMap(Long processTaskId, Long processTaskStepId, Map<String, List<NotifyReceiverVo>> receiverMap);
    
    /**
     * 
    * @Time:2020年9月15日
    * @Description: 根据工单步骤id获取自定义按钮文案映射 
    * @param processTaskStepId 
    * @return void
     */
    public Map<String, String> getCustomButtonMapByProcessTaskStepId(Long processTaskStepId);
    
    /**
     * 
    * @Time:2020年9月22日
    * @Description: 保存步骤提醒 
    * @param @param currentProcessTaskStepVo
    * @param @param content
    * @param @param type 
    * @return void
     */
    public int saveStepRemind(ProcessTaskStepVo currentProcessTaskStepVo,Long targerProcessTaskStepId, String reason, IProcessTaskStepRemindType ation);
    /**
     * 
     * @Time:2020年4月3日
     * @Description: 获取当前用户在当前步骤中工单干系人列表
     * @param processTaskVo     工单信息
     * @param processTaskStepId 步骤id
     * @return List<String>
     */
    public void setCurrentUserProcessUserTypeList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo);
    /**
     * 
    * @Author: linbq
    * @Time:2020年8月24日
    * @Description: 设置步骤配置、处理器全局配置信息 
    * @param processTaskStepVo 
    * @return void
     */
    public void setProcessTaskStepConfig(ProcessTaskStepVo processTaskStepVo);
}
