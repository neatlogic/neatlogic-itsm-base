package codedriver.framework.process.util;

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
                for(Object obj : stepList){
                    JSONObject jsonObject = JSONObject.parseObject(obj.toString());
                    if(ProcessStepHandlerType.START.getHandler().equals(jsonObject.getString("handler"))){
                        startUuid = jsonObject.getString("uuid");
                        break;
                    }
                }
                JSONArray connectionList = processConfig.getJSONArray("connectionList");
                /** 获取开始节点后的第一个节点UUID */
                if(CollectionUtils.isNotEmpty(connectionList)){
                    for(Object obj : connectionList){
                        JSONObject jsonObject = JSONObject.parseObject(obj.toString());
                        if(jsonObject.getString("fromStepUuid").equals(startUuid)){
                            firstStepUuid = jsonObject.getString("toStepUuid");
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
        int isNeedContent = 0;
        String firstStepUuid = getFirstStepUuid(configObj);
        JSONArray stepList = (JSONArray) JSONPath.read(configObj.toJSONString(), "process.stepList");
        if(StringUtils.isNotBlank(firstStepUuid) && CollectionUtils.isNotEmpty(stepList)){
            for(Object obj : stepList){
                JSONObject jsonObject = JSONObject.parseObject(obj.toString());
                if(jsonObject.getString("uuid").equals(firstStepUuid)){
                    isNeedContent = jsonObject.getJSONObject("stepConfig").getJSONObject("workerPolicyConfig").getIntValue("isNeedContent");
                    break;
                }
            }
        }
        return isNeedContent;
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
            for(Object o : authorityList){
                JSONObject object = JSONObject.parseObject(o.toString());
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
                        && StringUtils.isNotBlank(action) && "edit".equals(action) && CollectionUtils.isNotEmpty(attributeUuidList)
                        && StringUtils.isNotBlank(type)){
                    if("component".equals(type)){
                        if("all".equals(attributeUuidList.get(0).toString())){
                            allAttrCanEdit = true;
                            editableAttrs.clear();
                            break;
                        }else{
                            editableAttrs.addAll(attributeUuidList.toJavaList(String.class));
                        }
                    }else if("row".equals(type)){
                        editableAttrRows.addAll(attributeUuidList.toJavaList(Integer.class));
                    }
                }
            }
        }
        return allAttrCanEdit;
    }
}
