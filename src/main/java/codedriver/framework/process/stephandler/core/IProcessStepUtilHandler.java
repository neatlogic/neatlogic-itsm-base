package codedriver.framework.process.stephandler.core;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.process.dto.ProcessStepVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
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
     * @param @param
     *            processStepVo
     * @param @param
     *            stepConfigObj
     * @return void
     */
    public void makeupProcessStep(ProcessStepVo processStepVo, JSONObject stepConfigObj);

    /**
     * 
     * @Description: 子任务状态发生变化后，对子任务处理人的在 processtask_step_worker表和processtask_step_user表的数据做对应的变化
     * @param processTaskId
     * @param processTaskStepId
     * @return void
     */
    public void updateProcessTaskStepUserAndWorker(Long processTaskId, Long processTaskStepId);

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
     * @Time:2020年9月15日
     * @Description: 根据工单步骤id获取自定义按钮文案映射
     * @param processTaskStepId
     * @return void
     */
    public Map<String, String> getCustomButtonMapByProcessTaskStepId(Long processTaskStepId);

    /**
     * @Description: 根据步骤configHash和handler获取自定义按钮文案映射
     * @Author: linbq
     * @Date: 2020/9/15 12:17
     * @Params:[configHash, handler]
     * @Returns:java.util.Map<java.lang.String,java.lang.String>
     **/
    public Map<String, String> getCustomButtonMapByConfigHashAndHandler(String configHash, String handler);

    /**
     * @Description: 根据步骤configHash和handler、status获取自定义按钮文案
     * @Author: linbq
     * @Date: 2020/9/15 12:17
     * @Params:[configHash, handler, status]
     * @Returns:java.lang.String
     **/
    public String getStatusTextByConfigHashAndHandler(String configHash, String handler, String status);

    /**
     * 
     * @Time:2020年11月23日
     * @Description: 获取步骤配置信息中isRequired(回复是否必填)字段值
     * @param configHash
     * @return Integer
     */
    public Integer getIsRequiredByConfigHash(String configHash);

    /**
     * 
     * @Time:2020年11月23日
     * @Description: 获取步骤配置信息中isNeedContent(回复是否启用)字段值
     * @param configHash
     * @return Integer
     */
    public Integer getIsNeedContentByConfigHash(String configHash);
}
