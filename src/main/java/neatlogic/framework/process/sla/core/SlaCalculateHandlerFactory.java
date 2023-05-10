/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.sla.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.common.constvalue.IEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linbq
 * @since 2021/11/22 14:11
 **/
@RootComponent
public class SlaCalculateHandlerFactory extends ModuleInitializedListenerBase implements IEnum {

    private static Logger logger = LoggerFactory.getLogger(SlaCalculateHandlerFactory.class);

    private static Map<String, ISlaCalculateHandler> map = new HashMap<>();

    public static ISlaCalculateHandler getHandler(String handler) {
        return map.get(handler);
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, ISlaCalculateHandler> beanMap = context.getBeansOfType(ISlaCalculateHandler.class);
        for (Map.Entry<String, ISlaCalculateHandler> entry : beanMap.entrySet()) {
            ISlaCalculateHandler bean = entry.getValue();
            String handler = bean.getHandler();
            if (StringUtils.isBlank(handler)) {
                continue;
            }
            if (bean.getType() == null) {
                continue;
            }
            if (map.containsKey(handler)) {
                logger.error("sla计算规则处理器：'" + handler + "'已存在");
                System.exit(1);
            }
            map.put(bean.getHandler(), bean);
        }
    }

    @Override
    protected void myInit() {

    }

    @Override
    public List getValueTextList() {
        JSONArray list = new JSONArray();
        for (Map.Entry<String, ISlaCalculateHandler> entry : map.entrySet()) {
            ISlaCalculateHandler handler = entry.getValue();
            JSONObject obj = new JSONObject();
            obj.put("value", handler.getHandler());
            obj.put("text", handler.getName());
            obj.put("multiple", handler.getMultiple());
            obj.put("description", handler.getDescription());
            list.add(obj);
        }
        return list;
    }
}
