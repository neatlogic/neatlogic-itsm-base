/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import neatlogic.framework.form.constvalue.FormAttributeAction;
import neatlogic.framework.form.constvalue.FormAttributeAuthRange;
import neatlogic.framework.form.constvalue.FormAttributeAuthType;
import neatlogic.framework.process.constvalue.*;
import neatlogic.framework.process.exception.process.ProcessConfigException;
import neatlogic.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.stephandler.core.IProcessStepInternalHandler;
import neatlogic.framework.process.stephandler.core.ProcessMessageManager;
import neatlogic.framework.process.stephandler.core.ProcessStepInternalHandlerFactory;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProcessConfigUtil {
    /**
     * 获取第一个步骤的UUID
     *
     * @param configObj 流程配置
     * @return
     */
    public static String getFirstStepUuid(JSONObject configObj) {
        String firstStepUuid = null;
        if (MapUtils.isNotEmpty(configObj)) {
            JSONObject processConfig = configObj.getJSONObject("process");
            JSONArray stepList = processConfig.getJSONArray("stepList");
            if (MapUtils.isNotEmpty(processConfig) && CollectionUtils.isNotEmpty(stepList)) {
                /** 获取开始节点UUID */
                String startUuid = "";
                for (int i = 0; i < stepList.size(); i++) {
                    if (ProcessStepHandlerType.START.getHandler().equals(stepList.getJSONObject(i).getString("handler"))) {
                        startUuid = stepList.getJSONObject(i).getString("uuid");
                        break;
                    }
                }
                JSONArray connectionList = processConfig.getJSONArray("connectionList");
                /** 获取开始节点后的第一个节点UUID */
                if (CollectionUtils.isNotEmpty(connectionList)) {
                    for (int i = 0; i < connectionList.size(); i++) {
                        if (connectionList.getJSONObject(i).getString("fromStepUuid").equals(startUuid)) {
                            firstStepUuid = connectionList.getJSONObject(i).getString("toStepUuid");
                            break;
                        }
                    }
                }
            }
        }
        return firstStepUuid;
    }

    /**
     * 判断第一步是否需要描述框
     * 1、从stepList获取开始节点
     * 2、从connectionList获取开始节点后的第一个节点
     * 3、从stepList获取开始节点后的第一个节点是否启用描述框
     */
    public static int getIsNeedContent(JSONObject configObj) {
        Integer isNeedContent = 0;
        String firstStepUuid = getFirstStepUuid(configObj);
        JSONArray stepList = (JSONArray) JSONPath.read(configObj.toJSONString(), "process.stepList");
        if (StringUtils.isNotBlank(firstStepUuid) && CollectionUtils.isNotEmpty(stepList)) {
            for (int i = 0; i < stepList.size(); i++) {
                if (stepList.getJSONObject(i).getString("uuid").equals(firstStepUuid)) {
                    isNeedContent = (Integer) JSONPath.read(stepList.getJSONObject(i).toJSONString(), "stepConfig.isNeedContent");
                    break;
                }
            }
        }
        return isNeedContent != null ? isNeedContent : 0;
    }

    /**
     * 判断是否全部表单属性可编辑&获取可编辑的表单属性UUID与可编辑的行号
     *
     * @param configObj        流程配置
     * @param editableAttrs    可编辑的表单属性UUID集合
     * @param editableAttrRows 可编辑的表单属性行号
     * @return
     */
    public static boolean getEditableFormAttr(JSONObject configObj, Set<String> editableAttrs, Set<Integer> editableAttrRows) {
        boolean allAttrCanEdit = true;
        String firstStepUuid = getFirstStepUuid(configObj);
        JSONArray authorityList = (JSONArray) JSONPath.read(configObj.toJSONString(), "process.formConfig.authorityList");
        if (StringUtils.isNotBlank(firstStepUuid) && CollectionUtils.isNotEmpty(authorityList)) {
            allAttrCanEdit = false;
            for (int i = 0; i < authorityList.size(); i++) {
                JSONObject object = authorityList.getJSONObject(i);
                String action = object.getString("action");
                JSONArray attributeUuidList = object.getJSONArray("attributeUuidList");
                JSONArray processStepUuidList = object.getJSONArray("processStepUuidList");
                String type = object.getString("type");
                /* authorityList中存在可编辑与隐藏的表单属性配置
                  取可编辑的配置，如果以组件为单位，则直接记录属性UUID
                  如果以行为单位，则记录下可编辑的行号
                  如果发现有attributeUuidList为"all"的配置项，则退出循环
                 */
                if (CollectionUtils.isNotEmpty(processStepUuidList) && processStepUuidList.contains(firstStepUuid)
                        && StringUtils.isNotBlank(action) && FormAttributeAction.EDIT.getValue().equals(action) && CollectionUtils.isNotEmpty(attributeUuidList)
                        && StringUtils.isNotBlank(type)) {
                    if (FormAttributeAuthType.COMPONENT.getValue().equals(type)) {
                        if (FormAttributeAuthRange.ALL.getValue().equals(attributeUuidList.get(0).toString())) {
                            allAttrCanEdit = true;
                            editableAttrs.clear();
                            break;
                        } else {
                            editableAttrs.addAll(attributeUuidList.toJavaList(String.class));
                        }
                    } else if (FormAttributeAuthType.ROW.getValue().equals(type)) {
                        editableAttrRows.addAll(attributeUuidList.toJavaList(Integer.class));
                    }
                }
            }
        }
        return allAttrCanEdit;
    }

    /**
     * 权限设置
     *
     * @param authorityList 用户配置的数据
     * @param stepActions   权限集合
     * @return
     */
    public static JSONArray regulateAuthorityList(JSONArray authorityList, IOperationType[] stepActions) {
        JSONArray authorityArray = new JSONArray();
        if (stepActions != null) {
            for (IOperationType stepAction : stepActions) {
                authorityArray.add(new JSONObject() {{
                    this.put("action", stepAction.getValue());
                    this.put("text", stepAction.getText());
                    this.put("defaultValue", stepAction.getDefaultValue());
                    this.put("acceptList", null);
                    this.put("groupList", stepAction.getGroupList());
                }});
            }
        }

        if (CollectionUtils.isNotEmpty(authorityList)) {
            Map<String, JSONArray> authorityMap = new HashMap<>();
            for (int i = 0; i < authorityList.size(); i++) {
                JSONObject authority = authorityList.getJSONObject(i);
                authorityMap.put(authority.getString("action"), authority.getJSONArray("acceptList"));
            }
            for (int i = 0; i < authorityArray.size(); i++) {
                JSONObject authority = authorityArray.getJSONObject(i);
                JSONArray acceptList = authorityMap.get(authority.getString("action"));
                if (acceptList != null) {
                    authority.put("acceptList", acceptList);
                }
            }
        }
        return authorityArray;
    }

    /**
     * 按钮映射
     *
     * @param customButtonList 用户配置的数据
     * @param stepButtons
     * @return
     */
    public static JSONArray regulateCustomButtonList(JSONArray customButtonList, IOperationType[] stepButtons) {
        return regulateCustomButtonList(customButtonList, stepButtons, null);
    }

    /**
     * 按钮映射
     *
     * @param customButtonList 用户配置的数据
     * @param stepButtons
     * @param remark
     * @return
     */
    public static JSONArray regulateCustomButtonList(JSONArray customButtonList, IOperationType[] stepButtons, String remark) {
        JSONArray customButtonArray = new JSONArray();
        if(stepButtons != null) {
            for (IOperationType stepButton : stepButtons) {
                customButtonArray.add(new JSONObject() {{
                    this.put("name", stepButton.getValue());
                    if (StringUtils.isNotBlank(remark)) {
                        this.put("text", stepButton.getText() + "(" + remark + ")");
                    } else {
                        this.put("text", stepButton.getText());
                    }
                    this.put("value", "");
                }});
            }
        }

        if (CollectionUtils.isNotEmpty(customButtonList)) {
            Map<String, String> customButtonMap = new HashMap<>();
            for (int i = 0; i < customButtonList.size(); i++) {
                JSONObject customButton = customButtonList.getJSONObject(i);
                customButtonMap.put(customButton.getString("name"), customButton.getString("value"));
            }
            for (int i = 0; i < customButtonArray.size(); i++) {
                JSONObject customButton = customButtonArray.getJSONObject(i);
                String value = customButtonMap.get(customButton.getString("name"));
                if (StringUtils.isNotBlank(value)) {
                    customButton.put("value", value);
                }
            }
        }
        return customButtonArray;
    }

    /**
     * 状态映射
     *
     * @param customStatusList 用户配置的数据
     * @return
     */
    public static JSONArray regulateCustomStatusList(JSONArray customStatusList) {
        JSONArray customStatusArray = new JSONArray();
        for (ProcessTaskStepStatus status : ProcessTaskStepStatus.values()) {
            customStatusArray.add(new JSONObject() {{
                this.put("name", status.getValue());
                this.put("text", status.getText());
                this.put("value", "");
            }});
        }
        if (CollectionUtils.isNotEmpty(customStatusList)) {
            Map<String, String> customStatusMap = new HashMap<>();
            for (int i = 0; i < customStatusList.size(); i++) {
                JSONObject customStatus = customStatusList.getJSONObject(i);
                customStatusMap.put(customStatus.getString("name"), customStatus.getString("value"));
            }
            for (int i = 0; i < customStatusArray.size(); i++) {
                JSONObject customStatus = customStatusArray.getJSONObject(i);
                String value = customStatusMap.get(customStatus.getString("name"));
                if (StringUtils.isNotBlank(value)) {
                    customStatus.put("value", value);
                }
            }
        }
        return customStatusArray;
    }

    /**
     * 可替换文本列表
     *
     * @param replaceableTextList 用户配置的数据
     * @return
     */
    public static JSONArray regulateReplaceableTextList(JSONArray replaceableTextList) {
        JSONArray replaceableTextArray = new JSONArray();
        for (ReplaceableText replaceableText : ReplaceableText.values()) {
            replaceableTextArray.add(new JSONObject() {{
                this.put("name", replaceableText.getValue());
                this.put("text", replaceableText.getText());
                this.put("value", "");
            }});
        }
        if (CollectionUtils.isNotEmpty(replaceableTextList)) {
            Map<String, String> replaceableTextMap = new HashMap<>();
            for (int i = 0; i < replaceableTextList.size(); i++) {
                JSONObject replaceableText = replaceableTextList.getJSONObject(i);
                replaceableTextMap.put(replaceableText.getString("name"), replaceableText.getString("value"));
            }
            for (int i = 0; i < replaceableTextArray.size(); i++) {
                JSONObject replaceableText = replaceableTextArray.getJSONObject(i);
                String value = replaceableTextMap.get(replaceableText.getString("name"));
                if (StringUtils.isNotBlank(value)) {
                    replaceableText.put("value", value);
                }
            }
        }
        return replaceableTextArray;
    }

    /**
     * 分配处理人
     *
     * @param workerPolicyConfig 用户配置的数据
     * @return
     */
    public static JSONObject regulateWorkerPolicyConfig(JSONObject workerPolicyConfig) {
        JSONObject workerPolicyObj = new JSONObject();
        if (workerPolicyConfig == null) {
            workerPolicyConfig = new JSONObject();
        }
        String executeMode = workerPolicyConfig.getString("executeMode");
        if (StringUtils.isBlank(executeMode)) {
            executeMode = "batch";
        }
        workerPolicyObj.put("executeMode", executeMode);
        Map<WorkerPolicy, JSONObject> policyMap = new LinkedHashMap<>();
        JSONArray policyArray = new JSONArray();
        workerPolicyObj.put("policyList", policyArray);
        /** 由前置步骤处理人指定 **/
        {
            JSONObject policyObj = new JSONObject();
            policyObj.put("name", WorkerPolicy.PRESTEPASSIGN.getText());
            policyObj.put("type", WorkerPolicy.PRESTEPASSIGN.getValue());
            policyObj.put("isChecked", 0);
            JSONObject config = new JSONObject();
            config.put("isRequired", 0);
            config.put("processStepUuidList", new JSONArray());
            config.put("processStepList", new JSONArray());
            config.put("rangeList", new JSONArray());
            config.put("groupList", new JSONArray());
            policyObj.put("config", config);
            policyMap.put(WorkerPolicy.PRESTEPASSIGN, policyObj);
        }
        /** 复制前置步骤处理人 **/
        {
            JSONObject policyObj = new JSONObject();
            policyObj.put("name", WorkerPolicy.COPY.getText());
            policyObj.put("type", WorkerPolicy.COPY.getValue());
            policyObj.put("isChecked", 0);
            JSONObject config = new JSONObject();
            config.put("processStepUuid", "");
            policyObj.put("config", config);
            policyMap.put(WorkerPolicy.COPY, policyObj);
        }
        /** 表单值 **/
        {
            JSONObject policyObj = new JSONObject();
            policyObj.put("name", WorkerPolicy.FORM.getText());
            policyObj.put("type", WorkerPolicy.FORM.getValue());
            policyObj.put("isChecked", 0);
            JSONObject config = new JSONObject();
            config.put("attributeUuidList", new JSONArray());
            policyObj.put("config", config);
            policyMap.put(WorkerPolicy.FORM, policyObj);
        }
        /** 分派器 **/
        {
            JSONObject policyObj = new JSONObject();
            policyObj.put("name", WorkerPolicy.AUTOMATIC.getText());
            policyObj.put("type", WorkerPolicy.AUTOMATIC.getValue());
            policyObj.put("isChecked", 0);
            JSONObject config = new JSONObject();
            config.put("handler", "");
            config.put("handlerConfig", new JSONObject());
            policyObj.put("config", config);
            policyMap.put(WorkerPolicy.AUTOMATIC, policyObj);
        }
        /* 自定义 **/
        {
            JSONObject policyObj = new JSONObject();
            policyObj.put("name", WorkerPolicy.ASSIGN.getText());
            policyObj.put("type", WorkerPolicy.ASSIGN.getValue());
            policyObj.put("isChecked", 0);
            JSONObject config = new JSONObject();
//                    config.put("workerList", new JSONArray());
            policyObj.put("config", config);
            policyMap.put(WorkerPolicy.ASSIGN, policyObj);
        }
        JSONArray policyList = workerPolicyConfig.getJSONArray("policyList");
        if (CollectionUtils.isNotEmpty(policyList)) {
            List<String> effectiveStepUuidList = ProcessMessageManager.getEffectiveStepUuidList();
            for (int i = 0; i < policyList.size(); i++) {
                JSONObject policyObj = policyList.getJSONObject(i);
                if (MapUtils.isNotEmpty(policyObj)) {
                    WorkerPolicy type = WorkerPolicy.getWorkerPolicy(policyObj.getString("type"));
                    if (type == null) {
                        continue;
                    }
                    JSONObject configObj = policyObj.getJSONObject("config");
                    if (MapUtils.isEmpty(configObj)) {
                        continue;
                    }
                    JSONObject policyObject = policyMap.remove(type);
                    policyArray.add(policyObject);
                    Integer isChecked = policyObj.getInteger("isChecked");
                    if (Objects.equals(isChecked, 1)) {
                        policyObject.put("isChecked", 1);
                        JSONObject configObject = policyObject.getJSONObject("config");
                        switch (type) {
                            case PRESTEPASSIGN:
                                Integer isRequired = configObj.getInteger("isRequired");
                                if (Objects.equals(isRequired, 1)) {
                                    configObject.put("isRequired", 1);
                                }
                                JSONArray processStepUuidList = configObj.getJSONArray("processStepUuidList");
                                if (CollectionUtils.isNotEmpty(processStepUuidList)) {
                                    processStepUuidList.retainAll(effectiveStepUuidList);
                                    configObject.put("processStepUuidList", processStepUuidList);
                                }
                                JSONArray processStepList = new JSONArray();
                                JSONArray processStepArray = configObj.getJSONArray("processStepList");
                                if (CollectionUtils.isNotEmpty(processStepArray)) {
                                    for (int j = 0; j < processStepArray.size(); j++) {
                                        JSONObject processStepObj = processStepArray.getJSONObject(j);
                                        if (MapUtils.isEmpty(processStepObj)) {
                                            continue;
                                        }
                                        String uuid = processStepObj.getString("uuid");
                                        if (StringUtils.isBlank(uuid)) {
                                            continue;
                                        }
                                        if (!effectiveStepUuidList.contains(uuid) && ProcessMessageManager.getOperationType() == OperationTypeEnum.UPDATE) {
                                            throw new ProcessConfigException(ProcessConfigException.Type.PRE_STEP_ASSIGN, ProcessMessageManager.getStepName());
                                        }
                                        JSONArray conditionUuidArray = processStepObj.getJSONArray("condition");
                                        if (CollectionUtils.isNotEmpty(conditionUuidArray)) {
                                            List<String> conditionUuidList = conditionUuidArray.toJavaList(String.class);
                                            List<String> list = ListUtils.removeAll(conditionUuidList, effectiveStepUuidList);
                                            if (CollectionUtils.isNotEmpty(list) && ProcessMessageManager.getOperationType() == OperationTypeEnum.UPDATE) {
                                                throw new ProcessConfigException(ProcessConfigException.Type.PRE_STEP_ASSIGN_CONDITION_STEP, ProcessMessageManager.getStepName());
                                            }
                                        }
                                        processStepList.add(processStepObj);
                                    }
                                    configObject.put("processStepList", processStepList);
                                }
                                JSONArray rangeList = configObj.getJSONArray("rangeList");
                                if (CollectionUtils.isNotEmpty(rangeList)) {
                                    configObject.put("rangeList", rangeList);
                                }
                                JSONArray groupList = configObj.getJSONArray("groupList");
                                if (CollectionUtils.isNotEmpty(groupList)) {
                                    configObject.put("groupList", groupList);
                                }
                                break;
                            case COPY:
                                String processStepUuid = configObj.getString("processStepUuid");
                                if (StringUtils.isNotBlank(processStepUuid)) {
                                    if (!effectiveStepUuidList.contains(processStepUuid) && ProcessMessageManager.getOperationType() == OperationTypeEnum.UPDATE) {
                                        throw new ProcessConfigException(ProcessConfigException.Type.COPY, ProcessMessageManager.getStepName());
                                    }
                                    configObject.put("processStepUuid", processStepUuid);
                                }
                                break;
                            case FORM:
                                JSONArray attributeUuidList = configObj.getJSONArray("attributeUuidList");
                                if (CollectionUtils.isNotEmpty(attributeUuidList)) {
                                    configObject.put("attributeUuidList", attributeUuidList);
                                }
                                break;
                            case AUTOMATIC:
                                String handler = configObj.getString("handler");
                                if (StringUtils.isNotBlank(handler)) {
                                    configObject.put("handler", handler);
                                }
                                JSONObject handlerConfig = configObj.getJSONObject("handlerConfig");
                                if (MapUtils.isNotEmpty(handlerConfig)) {
                                    configObject.put("handlerConfig", handlerConfig);
                                }
                                break;
                            case ASSIGN:
                                JSONArray workerList = configObj.getJSONArray("workerList");
                                if (CollectionUtils.isNotEmpty(workerList)) {
                                    configObject.put("workerList", workerList);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        if (MapUtils.isNotEmpty(policyMap)) {
            for (Map.Entry<WorkerPolicy, JSONObject> entry : policyMap.entrySet()) {
                policyArray.add(entry.getValue());
            }
        }

        /* 异常处理人 **/
        String defaultWorker = workerPolicyConfig.getString("defaultWorker");
        workerPolicyObj.put("defaultWorker", defaultWorker);
        return workerPolicyObj;
    }

    /**
     * 简单配置项（自动开始、启用描述、描述必填、回复模板、异常处理人）
     *
     * @param configObj 用户配置的数据
     * @return
     */
    public static JSONObject regulateSimpleSettings(JSONObject configObj) {
        if (configObj == null) {
            configObj = new JSONObject();
        }
        JSONObject resultObj = new JSONObject();
        /** 自动开始 **/
        Integer autoStart = configObj.getInteger("autoStart");
        autoStart = autoStart == null ? 1 : autoStart;
        resultObj.put("autoStart", autoStart);
        /** 启用上传附件 **/
        Integer isNeedUploadFile = configObj.getInteger("isNeedUploadFile");
        isNeedUploadFile = isNeedUploadFile == null ? 1 : isNeedUploadFile;
        resultObj.put("isNeedUploadFile", isNeedUploadFile);
        /** 启用描述 **/
        Integer isNeedContent = configObj.getInteger("isNeedContent");
        isNeedContent = isNeedContent == null ? 1 : isNeedContent;
        resultObj.put("isNeedContent", isNeedContent);
        /** 描述必填 **/
        Integer isRequired = configObj.getInteger("isRequired");
        isRequired = isRequired == null ? 0 : isRequired;
        resultObj.put("isRequired", isRequired);
        /** 回复模板 **/
        Long commentTemplateId = configObj.getLong("commentTemplateId");
        if (commentTemplateId != null) {
            resultObj.put("commentTemplateId", commentTemplateId);
        }
        /** 标签列表 **/
        JSONArray tagList = configObj.getJSONArray("tagList");
        if (tagList == null) {
            tagList = new JSONArray();
        }
        resultObj.put("tagList", tagList);
        return resultObj;
    }

    /**
     * 校正流程图配置数据
     *
     * @param configObj 流程图配置数据
     * @return
     */
    public static String regulateProcessConfig(JSONObject configObj) {
        if (configObj == null) {
            return null;
        }
        if (configObj.isEmpty()) {
            return "{}";
        }
        JSONObject process = configObj.getJSONObject("process");
        if (MapUtils.isNotEmpty(process)) {
            try {
                ProcessMessageManager.setConfig(process);
                IProcessStepInternalHandler processStepInternalHandler = ProcessStepInternalHandlerFactory.getHandler(ProcessStepHandlerType.END.getHandler());
                if (processStepInternalHandler == null) {
                    throw new ProcessStepUtilHandlerNotFoundException(ProcessStepHandlerType.END.getHandler());
                }
                JSONObject processObj = processStepInternalHandler.regulateProcessStepConfig(process);
                JSONArray stepList = process.getJSONArray("stepList");
                if (CollectionUtils.isNotEmpty(stepList)) {
                    stepList.removeIf(Objects::isNull);
                    List<String> effectiveStepUuidList = ProcessMessageManager.getEffectiveStepUuidList();
                    for (int i = stepList.size() - 1; i >= 0; i--) {
                        JSONObject step = stepList.getJSONObject(i);
                        String uuid = step.getString("uuid");
                        if (!effectiveStepUuidList.contains(uuid)) {
                            stepList.remove(i);
                            continue;
                        }
                        ProcessMessageManager.setStepName(step.getString("name"));
                        String handler = step.getString("handler");
                        if (!Objects.equals(handler, ProcessStepHandlerType.END.getHandler())) {
                            processStepInternalHandler = ProcessStepInternalHandlerFactory.getHandler(handler);
                            if (processStepInternalHandler == null) {
                                throw new ProcessStepUtilHandlerNotFoundException(handler);
                            }
                            JSONObject stepConfig = step.getJSONObject("stepConfig");
                            JSONObject stepConfigObj = processStepInternalHandler.regulateProcessStepConfig(stepConfig);
                            step.put("stepConfig", stepConfigObj);
                        }
                    }
                }
                processObj.put("stepList", stepList);
                processObj.put("connectionList", ProcessMessageManager.getProcessStepRelList());
                configObj.put("process", processObj);
            } finally {
                ProcessMessageManager.release();
            }
        }
        return configObj.toJSONString();
    }
}
