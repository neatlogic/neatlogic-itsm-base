package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.process.dto.ProcessStepVo;
import neatlogic.framework.process.dto.ProcessTaskStepInOperationVo;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Title: IProcessStepInternalHandler
 * @Package neatlogic.framework.process.stephandler.core
 * @Description: 处理流程组件内部业务逻辑
 * @Author: chenqiwei
 * @Date: 2021/1/20 15:55
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 **/
public interface IProcessStepInternalHandler {

    public String getHandler();

    /**
     * @param @return
     * @return Object
     * @Time: 2020年7月27日
     * @Description: 处理器特有的步骤信息
     */
    public Object getHandlerStepInfo(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     *
     * @Time: 2020年8月12日
     * @Description: 处理器特有的步骤初始化信息
     * @param @return
     * @return Object
     */
    public Object getHandlerStepInitInfo(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * @Author: chenqiwei
     * @Time: Feb 10, 2020
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
     * @Time: 2020年6月30日
     * @Description: 构造节点管理配置数据
     * @param configObj
     * @return JSONObject
     */
    public default JSONObject makeupConfig(JSONObject configObj) {
        if (configObj == null) {
            configObj = new JSONObject();
        }
        return configObj;
    }

    /**
     * 校正流程步骤配置数据，对配置数据中没用的字段删除，对缺失的字段用默认值补全。
     * @param configObj 配置数据
     * @return
     */
    public default JSONObject regulateProcessStepConfig(JSONObject configObj) {
        return configObj;
    }

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

    /**
     * 获取步骤配置信息中isNeedUploadFile(是否启用上传文件)字段值
     * @param configHash
     * @return
     */
    Integer getIsNeedUploadFileByConfigHash(String configHash);

    /**
     * 获取步骤配置信息中enableReapproval(启用重审)字段值
     * @param configHash
     * @return
     */
    public Integer getEnableReapprovalByConfigHash(String configHash);

    /**
     * 获取步骤配置信息中formSceneUuid(表单场景)字段值
     * @param configHash
     * @return
     */
    String getFormSceneUuidByConfigHash(String configHash);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    int insertProcessTaskStepInOperation(ProcessTaskStepInOperationVo processTaskStepInOperationVo);
}
