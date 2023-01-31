/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.sla.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.CodedriverWebApplicationContext;
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
    protected void onInitialized(CodedriverWebApplicationContext context) {
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
