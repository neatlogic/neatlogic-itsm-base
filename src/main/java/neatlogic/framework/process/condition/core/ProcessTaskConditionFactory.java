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

package neatlogic.framework.process.condition.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
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
    public void onInitialized(NeatLogicWebApplicationContext context) {
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
