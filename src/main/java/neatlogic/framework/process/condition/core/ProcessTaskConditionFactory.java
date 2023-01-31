/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.condition.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.CodedriverWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.constvalue.ProcessFieldType;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RootComponent
public class ProcessTaskConditionFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskCondition> conditionComponentMap = new HashMap<>();

    public static IProcessTaskCondition getHandler(String name) {
        return conditionComponentMap.get(name);
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
