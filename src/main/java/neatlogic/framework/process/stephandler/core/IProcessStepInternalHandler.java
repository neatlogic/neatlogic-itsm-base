package neatlogic.framework.process.stephandler.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.notify.core.INotifyPolicyHandler;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.constvalue.ProcessFlowDirection;
import neatlogic.framework.process.dto.ProcessStepVo;
import neatlogic.framework.process.dto.ProcessTaskStepDataVo;
import neatlogic.framework.process.dto.ProcessTaskStepInOperationVo;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import org.apache.commons.collections4.MapUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Title: IProcessStepInternalHandler
 * @Package neatlogic.framework.process.stephandler.core
 * @Description: 处理流程组件内部业务逻辑
 * @Author: chenqiwei
 * @Date: 2021/1/20 15:55
 **/
public interface IProcessStepInternalHandler {

    String getHandler();

    /**
     * @return Object
     * @Time: 2020年7月27日
     * @Description: 该步骤特有的步骤信息（当该步骤是开始节点时调用该方法）
     */
    default Object getStartStepInfo(ProcessTaskStepVo currentProcessTaskStepVo) {
        return null;
    }

    /**
     * @return Object
     * @Time: 2020年8月12日
     * @Description: 该步骤特有的步骤初始化信息 （当该步骤不是开始节点时调用该方法）
     */
    default Object getNonStartStepInfo(ProcessTaskStepVo currentProcessTaskStepVo) {
        return null;
    }

    /**
     * 如果步骤数据需要保存到自定义地方，扩展此方法
     * @param currentProcessTaskStepDataVo 当前步骤数据
     */
    default void saveData(ProcessTaskStepDataVo currentProcessTaskStepDataVo) {

    }

    /**
     * @return void
     * @Description: 组装步骤节点信息，将步骤stepConfig配置信息中的字段值写入到ProcessStepVo对象对应属性中
     */
    default void makeupProcessStep(ProcessStepVo processStepVo, JSONObject stepConfigObj) {
        if (MapUtils.isNotEmpty(stepConfigObj)) {
            for (Map.Entry<String, Object> entry : stepConfigObj.entrySet()) {
                IProcessStepMakeupHandler handler = ProcessStepMakeupHandlerFactory.getHandlers(entry.getKey());
                if (handler != null) {
                    handler.makeup(this, processStepVo, stepConfigObj);
                }
            }
        }
    }

    /**
     * @return void
     * @Description: 子任务状态发生变化后，对子任务处理人的在 processtask_step_worker表和processtask_step_user表的数据做对应的变化
     */
    void updateProcessTaskStepUserAndWorker(Long processTaskId, Long processTaskStepId);

    /**
     * @return JSONObject
     * @Time: 2020年6月30日
     * @Description: 构造节点管理配置数据，初始化节点管理中各个节点的全局配置信息，设置默认值，校正节点的全局配置数据，对配置数据中没用的字段删除，对缺失的字段用默认值补充。
     */
    default JSONObject makeupConfig(JSONObject configObj) {
       /* if (configObj == null) {
            configObj = new JSONObject();
        }
        return configObj;*/
        //TODO 暂时直接使用regulateProcessStepConfig，后续再看是否需要优化
        return this.regulateProcessStepConfig(configObj);

    }

    /**
     * 返回步骤动作，校验时用
     */
    default IOperationType[] getStepActions() {
        //TODO 加default只是为了不报错，重构完所有代码后删掉这个default函数
        return null;
    }

    /**
     * 返回步骤按钮列表
     */
    default IOperationType[] getStepButtons() {
        //TODO 加default只是为了不报错，重构完所有代码后删掉这个default函数
        return null;
    }

    /*
    返回需要校验的键列表，因为原配置里某些键可能不存在却需要补充，只需要定义必须要补充的键即可，取余的按照config数据来校验
     */
    default String[] getRegulateKeyList() {
        //TODO 加default只是为了不报错，重构完所有代码后删掉这个default函数
        return null;
    }

    /**
     * 初始化流程步骤的默认配置信息，校正流程步骤配置数据，对配置数据中没用的字段删除，对缺失的字段用默认值补充。
     *
     * @param configObj 配置数据
     */
    default JSONObject regulateProcessStepConfig(JSONObject configObj) {
        JSONObject newConfig = new JSONObject();
        if (MapUtils.isNotEmpty(configObj) || this.getRegulateKeyList() != null) {
            Set<String> validatedSet = new HashSet<>();
            //先检查config中存在的数据
            if (MapUtils.isNotEmpty(configObj)) {
                for (Map.Entry<String, Object> entry : configObj.entrySet()) {
                    validatedSet.add(entry.getKey());
                    IRegulateHandler handler = RegulateHandlerFactory.getHandlers(entry.getKey());
                    if (handler != null) {
                        handler.regulateConfig(this, configObj, newConfig);
                    }
                }
            }
            //再补充不存在的数据
            if (this.getRegulateKeyList() != null) {
                if (configObj == null) {
                    configObj = new JSONObject();
                }
                for (String key : this.getRegulateKeyList()) {
                    if (!validatedSet.contains(key)) {
                        IRegulateHandler handler = RegulateHandlerFactory.getHandlers(key);
                        if (handler != null) {
                            handler.regulateConfig(this, configObj, newConfig);
                        }
                    }
                }
            }
        }
        return newConfig;
    }

    /**
     * 返回该步骤的通知策略处理器类
     * @return
     */
    default Class<? extends INotifyPolicyHandler> getNotifyPolicyHandlerClass() {
        return null;
    }

    /**
     * @return void
     * @Description: 根据工单步骤id获取自定义按钮文案映射
     */
    Map<String, String> getCustomButtonMapByProcessTaskStepId(Long processTaskStepId);

    /**
     * @Description: 根据步骤configHash和handler获取自定义按钮文案映射
     * @Author: linbq
     * @Date: 2020/9/15 12:17
     **/
    Map<String, String> getCustomButtonMapByConfigHashAndHandler(String configHash, String handler);

    /**
     * @Description: 根据步骤configHash和handler、status获取状态自定义按钮文案
     * @Author: linbq
     * @Date: 2020/9/15 12:17
     **/
    String getStatusTextByConfigHashAndHandler(String configHash, String handler, String status);

    /**
     * @return Integer
     * @Description: 获取步骤配置信息中isRequired(回复是否必填)字段值
     */
    Integer getIsRequiredByConfigHash(String configHash);

    /**
     * @return Integer
     * @Description: 获取步骤配置信息中isNeedContent(回复是否启用)字段值
     */
    Integer getIsNeedContentByConfigHash(String configHash);

    /**
     * 获取步骤配置信息中isNeedUploadFile(是否启用上传文件)字段值
     *
     * @param configHash 配置Hash值
     * @return Integer
     */
    Integer getIsNeedUploadFileByConfigHash(String configHash);

    /**
     * 获取步骤配置信息中enableReapproval(启用重审)字段值
     *
     * @param configHash 配置Hash值
     * @return Integer
     */
    Integer getEnableReapprovalByConfigHash(String configHash);

    /**
     * 获取步骤配置信息中formSceneUuid(表单场景)字段值
     *
     * @param configHash 配置Hash值
     * @return String
     */
    String getFormSceneUuidByConfigHash(String configHash);

    /**
     * 向processtask_step_in_operation表中插入步骤正在操作记录，等到该步骤操作完成时会删除这条记录
     *
     * @param processTaskStepInOperationVo 当前步骤
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    int insertProcessTaskStepInOperation(ProcessTaskStepInOperationVo processTaskStepInOperationVo);

    /**
     * 获取该步骤中的附件id列表
     *
     * @param currentProcessTaskStepVo 当前步骤
     * @return
     */
    default List<Long> getFileIdList(ProcessTaskStepVo currentProcessTaskStepVo) {
        return new ArrayList<>();
    }

    /**
     * 获取可流转步骤列表
     *
     * @param currentProcessTaskStepVo 当前步骤
     * @return
     */
    List<ProcessTaskStepVo> getNextStepList(ProcessTaskStepVo currentProcessTaskStepVo, ProcessFlowDirection processFlowDirection);
}
