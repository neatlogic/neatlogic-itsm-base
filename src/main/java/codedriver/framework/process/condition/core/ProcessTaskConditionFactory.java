/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.condition.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;
import codedriver.framework.process.constvalue.ConditionProcessTaskOptions;
import codedriver.framework.process.constvalue.ProcessFieldType;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class ProcessTaskConditionFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskCondition> conditionComponentMap = new HashMap<>();

    public static IProcessTaskCondition getHandler(String name) {
        return conditionComponentMap.get(name);
    }

    public static JSONObject getConditionParamData(ConditionProcessTaskOptions[] options, ProcessTaskStepVo processTaskStepVo) {
        JSONObject resultObj = new JSONObject();
        for (ConditionProcessTaskOptions option : options) {
            IProcessTaskCondition handler = conditionComponentMap.get(option.getValue());
            if (handler != null) {
                resultObj.put(option.getValue(), handler.getConditionParamData(processTaskStepVo));
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

    @Override
    public void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IProcessTaskCondition> myMap = context.getBeansOfType(IProcessTaskCondition.class);
        for (Map.Entry<String, IProcessTaskCondition> entry : myMap.entrySet()) {
            IProcessTaskCondition column = entry.getValue();
            conditionComponentMap.put(column.getName(), column);
        }
    }

    public static Map<String, IProcessTaskCondition> getConditionComponentMap() {
        return conditionComponentMap;
    }

    @Override
    protected void myInit() {
        // TODO Auto-generated method stub

    }

}
