/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.condition.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.constvalue.ConditionProcessTaskOptions;
import neatlogic.framework.process.constvalue.ProcessFieldType;
import neatlogic.framework.process.dto.ProcessTaskStepVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RootComponent
public class ProcessTaskConditionFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskCondition> conditionComponentMap = new HashMap<>();

    public static IProcessTaskCondition getHandler(String name) {
        return conditionComponentMap.get(name);
    }

    private static final List<IProcessTaskCondition> conditionHandlerList = new ArrayList<>();

    public static List<IProcessTaskCondition> getConditionHandlerList() {
        return conditionHandlerList;
    }

    public static JSONObject getConditionParamData(List<String> options, ProcessTaskStepVo processTaskStepVo) {
        JSONObject resultObj = new JSONObject();
        for (String option : options) {
            IProcessTaskCondition handler = conditionComponentMap.get(option);
            if (handler != null) {
                resultObj.put(option, handler.getConditionParamData(processTaskStepVo));
            }
        }
        IProcessTaskCondition handler = conditionComponentMap.get(ProcessFieldType.FORM.getValue());
        if (handler != null) {
            Object formObj = handler.getConditionParamData(processTaskStepVo);
            if (formObj != null) {
                if (formObj instanceof JSONObject) {
                    resultObj.putAll((JSONObject) formObj);
                }
            }
        }
        return resultObj;
    }

    public static JSONObject getConditionParamData(List<String> options, ProcessTaskStepVo processTaskStepVo, String formTag) {
        JSONObject resultObj = new JSONObject();
        for (String option : options) {
            IProcessTaskCondition handler = conditionComponentMap.get(option);
            if (handler != null) {
                resultObj.put(option, handler.getConditionParamDataNew(processTaskStepVo, formTag));
            }
        }
        IProcessTaskCondition handler = conditionComponentMap.get(ProcessFieldType.FORM.getValue());
        if (handler != null) {
            Object formObj = handler.getConditionParamDataNew(processTaskStepVo, formTag);
            if (formObj != null) {
                if (formObj instanceof JSONObject) {
                    resultObj.putAll((JSONObject) formObj);
                }
            }
        }
        return resultObj;
    }

    public static JSONObject getConditionParamData(ConditionProcessTaskOptions[] options, ProcessTaskStepVo processTaskStepVo) {
        JSONObject resultObj = new JSONObject();
        for (ConditionProcessTaskOptions option : options) {
            IProcessTaskCondition handler = conditionComponentMap.get(option.getValue());
            if (handler != null) {
                Object object = handler.getConditionParamData(processTaskStepVo);
                if (object != null) {
                    resultObj.put(option.getValue(), object);
                    resultObj.put(option.getText(), handler.getConditionParamDataForHumanization(processTaskStepVo));
                }
            }
        }
        IProcessTaskCondition handler = conditionComponentMap.get(ProcessFieldType.FORM.getValue());
        if (handler != null) {
            Object formObj = handler.getConditionParamData(processTaskStepVo);
            if (formObj != null) {
                if (formObj instanceof JSONObject) {
                    resultObj.putAll((JSONObject) formObj);
                }
                Object formObjForHumanization = handler.getConditionParamDataForHumanization(processTaskStepVo);
                if (formObjForHumanization instanceof JSONObject) {
                    resultObj.putAll((JSONObject) formObjForHumanization);
                }
            }
        }
        return resultObj;
    }

    public static JSONObject getConditionParamData(ConditionProcessTaskOptions[] options, ProcessTaskStepVo processTaskStepVo, String formTag) {
        JSONObject resultObj = new JSONObject();
        for (ConditionProcessTaskOptions option : options) {
            IProcessTaskCondition handler = conditionComponentMap.get(option.getValue());
            if (handler != null) {
                Object object = handler.getConditionParamDataNew(processTaskStepVo, formTag);
                if (object != null) {
                    resultObj.put(option.getValue(), object);
                    resultObj.put(option.getText(), handler.getConditionParamDataForHumanizationNew(processTaskStepVo, formTag));
                }
            }
        }
        IProcessTaskCondition handler = conditionComponentMap.get(ProcessFieldType.FORM.getValue());
        if (handler != null) {
            Object formObj = handler.getConditionParamDataNew(processTaskStepVo, formTag);
            if (formObj != null) {
                if (formObj instanceof JSONObject) {
                    resultObj.putAll((JSONObject) formObj);
                }
                Object formObjForHumanization = handler.getConditionParamDataForHumanizationNew(processTaskStepVo, formTag);
                if (formObjForHumanization instanceof JSONObject) {
                    resultObj.putAll((JSONObject) formObjForHumanization);
                }
            }
        }
        return resultObj;
    }

    @Override
    public void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IProcessTaskCondition> myMap = context.getBeansOfType(IProcessTaskCondition.class);
        for (Map.Entry<String, IProcessTaskCondition> entry : myMap.entrySet()) {
            IProcessTaskCondition column = entry.getValue();
            conditionComponentMap.put(column.getName(), column);
            conditionHandlerList.add(column);
        }
    }

    public static Map<String, IProcessTaskCondition> getConditionComponentMap() {
        return conditionComponentMap;
    }

    @Override
    protected void myInit() {

    }

}
