package codedriver.framework.process.util;

import codedriver.framework.form.constvalue.FormAttributeAction;
import codedriver.framework.form.constvalue.FormAttributeAuthRange;
import codedriver.framework.form.constvalue.FormAttributeAuthType;
import codedriver.framework.notify.core.INotifyPolicyHandler;
import codedriver.framework.process.constvalue.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class ProcessConfigUtil {
    /** 获取第一个步骤的UUID
     * @param configObj 流程配置
     * @return
     */
    public static String getFirstStepUuid(JSONObject configObj){
        String firstStepUuid = null;
        if(MapUtils.isNotEmpty(configObj)){
            JSONObject processConfig = configObj.getJSONObject("process");
            JSONArray stepList = processConfig.getJSONArray("stepList");
            if(MapUtils.isNotEmpty(processConfig) && CollectionUtils.isNotEmpty(stepList)){
                /** 获取开始节点UUID */
                String startUuid = "";
                for(int i = 0;i < stepList.size();i++){
                    if(ProcessStepHandlerType.START.getHandler().equals(stepList.getJSONObject(i).getString("handler"))){
                        startUuid = stepList.getJSONObject(i).getString("uuid");
                        break;
                    }
                }
                JSONArray connectionList = processConfig.getJSONArray("connectionList");
                /** 获取开始节点后的第一个节点UUID */
                if(CollectionUtils.isNotEmpty(connectionList)){
                    for(int i = 0;i < connectionList.size();i++){
                        if(connectionList.getJSONObject(i).getString("fromStepUuid").equals(startUuid)){
                            firstStepUuid = connectionList.getJSONObject(i).getString("toStepUuid");
                            break;
                        }
                    }
                }
            }
        }
        return firstStepUuid;
    }

    /** 判断第一步是否需要描述框
     * 1、从stepList获取开始节点
     * 2、从connectionList获取开始节点后的第一个节点
     * 3、从stepList获取开始节点后的第一个节点是否启用描述框
     */
    public static int getIsNeedContent(JSONObject configObj){
        Integer isNeedContent = 0;
        String firstStepUuid = getFirstStepUuid(configObj);
        JSONArray stepList = (JSONArray) JSONPath.read(configObj.toJSONString(), "process.stepList");
        if(StringUtils.isNotBlank(firstStepUuid) && CollectionUtils.isNotEmpty(stepList)){
            for(int i = 0;i < stepList.size();i++){
                if(stepList.getJSONObject(i).getString("uuid").equals(firstStepUuid)){
                    isNeedContent = (Integer)JSONPath.read(stepList.getJSONObject(i).toJSONString(),"stepConfig.workerPolicyConfig.isNeedContent");
                    break;
                }
            }
        }
        return isNeedContent != null ? isNeedContent : 0;
    }

    /**
     * 判断是否全部表单属性可编辑&获取可编辑的表单属性UUID与可编辑的行号
     * @param configObj 流程配置
     * @param editableAttrs 可编辑的表单属性UUID集合
     * @param editableAttrRows 可编辑的表单属性行号
     * @return
     */
    public static boolean getEditableFormAttr(JSONObject configObj, Set<String> editableAttrs,Set<Integer> editableAttrRows){
        boolean allAttrCanEdit = false;
        String firstStepUuid = getFirstStepUuid(configObj);
        JSONArray authorityList = (JSONArray) JSONPath.read(configObj.toJSONString(), "process.formConfig.authorityList");
        if(StringUtils.isNotBlank(firstStepUuid) && CollectionUtils.isNotEmpty(authorityList)){
            for(int i = 0;i < authorityList.size();i++){
                JSONObject object = authorityList.getJSONObject(i);
                String action = object.getString("action");
                JSONArray attributeUuidList = object.getJSONArray("attributeUuidList");
                JSONArray processStepUuidList = object.getJSONArray("processStepUuidList");
                String type = object.getString("type");
                /** authorityList中存在可编辑与隐藏的表单属性配置
                 * 取可编辑的配置，如果以组件为单位，则直接记录属性UUID
                 * 如果以行为单位，则记录下可编辑的行号
                 * 如果发现有attributeUuidList为"all"的配置项，则退出循环
                 */
                if(CollectionUtils.isNotEmpty(processStepUuidList) && processStepUuidList.contains(firstStepUuid)
                        && StringUtils.isNotBlank(action) && FormAttributeAction.EDIT.getValue().equals(action) && CollectionUtils.isNotEmpty(attributeUuidList)
                        && StringUtils.isNotBlank(type)){
                    if(FormAttributeAuthType.COMPONENT.getValue().equals(type)){
                        if(FormAttributeAuthRange.ALL.getValue().equals(attributeUuidList.get(0).toString())){
                            allAttrCanEdit = true;
                            editableAttrs.clear();
                            break;
                        }else{
                            editableAttrs.addAll(attributeUuidList.toJavaList(String.class));
                        }
                    }else if(FormAttributeAuthType.ROW.getValue().equals(type)){
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
    public static JSONArray makeupAuthorityList(JSONArray authorityList, ProcessTaskOperationType[] stepActions) {
        JSONArray authorityArray = new JSONArray();
        for (ProcessTaskOperationType stepAction : stepActions) {
            authorityArray.add(new JSONObject() {{
                this.put("action", stepAction.getValue());
                this.put("text", stepAction.getText());
                this.put("acceptList", stepAction.getAcceptList());
                this.put("groupList", stepAction.getGroupList());
            }});
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
    public static JSONArray makeupCustomButtonList(JSONArray customButtonList, IOperationType[] stepButtons) {
        return makeupCustomButtonList(customButtonList, stepButtons, null);
    }

    /**
     * 按钮映射
     *
     * @param customButtonList 用户配置的数据
     * @param stepButtons
     * @param remark
     * @return
     */
    public static JSONArray makeupCustomButtonList(JSONArray customButtonList, IOperationType[] stepButtons, String remark) {
        JSONArray customButtonArray = new JSONArray();
        for (IOperationType stepButton : stepButtons) {
            customButtonArray.add(new JSONObject() {{
                this.put("name", stepButton.getValue());
                if(StringUtils.isNotBlank(remark)){
                    this.put("customText", stepButton.getText() + "(" + remark + ")");
                }else {
                    this.put("customText", stepButton.getText());
                }
                this.put("value", "");
            }});
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
    public static JSONArray makeupCustomStatusList(JSONArray customStatusList) {
        JSONArray customStatusArray = new JSONArray();
        for (ProcessTaskStatus status : ProcessTaskStatus.values()) {
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
     * 通知设置
     *
     * @param notifyPolicyConfig 用户配置的数据
     * @param handlerClass       通知策略类型
     * @return
     */
//    public static JSONObject makeupNotifyPolicyConfig(JSONObject notifyPolicyConfig, Class<? extends INotifyPolicyHandler> handlerClass) {
//        JSONObject notifyPolicyObj = new JSONObject();
//        if (MapUtils.isNotEmpty(notifyPolicyConfig)) {
//            notifyPolicyObj.putAll(notifyPolicyConfig);
//        }
////        notifyPolicyObj.put("handler", handlerClass.getName());
//        return notifyPolicyObj;
//    }

    /**
     * 动作设置
     *
     * @param actionConfig 用户配置的数据
     * @param handlerClass 通知策略类型
     * @return
     */
//    public static JSONObject makeupActionConfig(JSONObject actionConfig, Class<? extends INotifyPolicyHandler> handlerClass) {
//        JSONObject actionObj = new JSONObject();
//        if (MapUtils.isNotEmpty(actionConfig)) {
//            actionObj.putAll(actionConfig);
//        }
//        actionObj.put("handler", handlerClass.getName());
//        actionObj.put("integrationHandler", "");
//        return actionObj;
//    }

    /**
     * 分配处理人
     *
     * @param workerPolicyConfig 用户配置的数据
     * @return
     */
    public static JSONObject makeupWorkerPolicyConfig(JSONObject workerPolicyConfig) {
        JSONObject workerPolicyObj = new JSONObject();
        if (MapUtils.isNotEmpty(workerPolicyConfig)) {
            JSONObject simpleSettings = makeupSimpleSettings(workerPolicyConfig);
            workerPolicyObj.putAll(simpleSettings);
            String executeMode = workerPolicyConfig.getString("executeMode");
            if (StringUtils.isBlank(executeMode)) {
                executeMode = "batch";
            }
            workerPolicyObj.put("executeMode", executeMode);
            JSONArray policyList = workerPolicyConfig.getJSONArray("policyList");
            if (CollectionUtils.isNotEmpty(policyList)) {
                Map<WorkerPolicy, JSONObject> policyMap = new HashMap<>();
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
                    policyObj.put("config", config);
                    policyMap.put(WorkerPolicy.PRESTEPASSIGN, policyObj);
                    policyArray.add(policyObj);
                }
                /** 复制前置步骤处理人 **/
                {
                    JSONObject policyObj = new JSONObject();
                    policyObj.put("name", WorkerPolicy.COPY.getText());
                    policyObj.put("type", WorkerPolicy.COPY.getValue());
                    policyObj.put("isChecked", 0);
                    JSONObject config = new JSONObject();
                    config.put("processStepUuidList", "");//TODO 这里是单选，应该改成processStepUuid
                    policyObj.put("config", config);
                    policyMap.put(WorkerPolicy.COPY, policyObj);
                    policyArray.add(policyObj);
                }
                /** 表单值 **/
                {
                    JSONObject policyObj = new JSONObject();
                    policyObj.put("name", WorkerPolicy.FORM.getText());
                    policyObj.put("type", WorkerPolicy.FORM.getValue());
                    policyObj.put("isChecked", 0);
                    JSONObject config = new JSONObject();
                    config.put("attributeUuid", "");
                    policyObj.put("config", config);
                    policyMap.put(WorkerPolicy.FORM, policyObj);
                    policyArray.add(policyObj);
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
                    policyArray.add(policyObj);
                }
                /** 自定义 **/
                {
                    JSONObject policyObj = new JSONObject();
                    policyObj.put("name", WorkerPolicy.ASSIGN.getText());
                    policyObj.put("type", WorkerPolicy.ASSIGN.getValue());
                    policyObj.put("isChecked", 0);
                    JSONObject config = new JSONObject();
//                    config.put("workerList", new JSONArray());
                    policyObj.put("config", config);
                    policyMap.put(WorkerPolicy.ASSIGN, policyObj);
                    policyArray.add(policyObj);
                }
                for (int i = 0; i < policyList.size(); i++) {
                    JSONObject policyObj = policyList.getJSONObject(i);
                    if (MapUtils.isNotEmpty(policyObj)) {
                        Integer isChecked = policyObj.getInteger("isChecked");
                        if (Objects.equals(isChecked, 1)) {
                            WorkerPolicy type = WorkerPolicy.getWorkerPolicy(policyObj.getString("type"));
                            if (type == null) {
                                continue;
                            }
                            JSONObject configObj = policyObj.getJSONObject("config");
                            if (MapUtils.isEmpty(configObj)) {
                                continue;
                            }
                            JSONObject policyObject = policyMap.get(type);
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
                                        configObject.put("processStepUuidList", processStepUuidList);
                                    }
                                    break;
                                case COPY:
                                    String processStepUuid = configObj.getString("processStepUuidList");
                                    if (StringUtils.isNotBlank(processStepUuid)) {
                                        configObject.put("processStepUuidList", processStepUuid);
                                    }
                                    break;
                                case FORM:
                                    String attributeUuid = configObj.getString("attributeUuid");
                                    if (StringUtils.isNotBlank(attributeUuid)) {
                                        configObject.put("attributeUuid", attributeUuid);
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
        }
        return workerPolicyObj;
    }

    /**
     * 简单配置项（自动开始、启用描述、描述必填、回复模板、异常处理人）
     *
     * @param configObj 用户配置的数据
     * @return
     */
    private static JSONObject makeupSimpleSettings(JSONObject configObj) {
        // TODO linbq 数据结构有问题，下面这些字段放在workerPolicyConfig里面了，应该放在与workerPolicyConfig同级
        if (configObj == null) {
            configObj = new JSONObject();
        }
        JSONObject resultObj = new JSONObject();
        /** 自动开始 **/
        Integer autoStart = configObj.getInteger("autoStart");
        autoStart = autoStart == null ? 1 : autoStart;
        resultObj.put("autoStart", autoStart);
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
//        commentTemplateId = commentTemplateId == null ? -1 : commentTemplateId;
        if(commentTemplateId != null){
            resultObj.put("commentTemplateId", commentTemplateId);
        }
        /** 异常处理人 **/
        String defaultWorker = configObj.getString("defaultWorker");
        resultObj.put("defaultWorker", defaultWorker);
        return resultObj;
    }
}
