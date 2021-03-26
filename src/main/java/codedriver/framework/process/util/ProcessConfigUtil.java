package codedriver.framework.process.util;

import codedriver.framework.form.constvalue.FormAttributeAction;
import codedriver.framework.form.constvalue.FormAttributeAuthRange;
import codedriver.framework.form.constvalue.FormAttributeAuthType;
import codedriver.framework.process.constvalue.ProcessStepHandlerType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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
}
