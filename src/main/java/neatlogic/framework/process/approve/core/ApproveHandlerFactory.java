package neatlogic.framework.process.approve.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class ApproveHandlerFactory extends ModuleInitializedListenerBase {

    private static Logger logger = LoggerFactory.getLogger(ApproveHandlerFactory.class);

    private static Map<String, IApproveHandler> map = new HashMap<>();

    public static IApproveHandler getHandler(String handler) {
        return map.get(handler);
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IApproveHandler> beanMap = context.getBeansOfType(IApproveHandler.class);
        for (Map.Entry<String, IApproveHandler> entry : beanMap.entrySet()) {
            IApproveHandler bean = entry.getValue();
            String handler = bean.getHandler();
            if (StringUtils.isBlank(handler)) {
                logger.error("审批处理器：'" + ClassUtils.getUserClass(bean.getClass()).getName() + "'的handler为空");
                System.exit(1);
            }
            if (map.containsKey(handler)) {
                logger.error("审批处理器：'" + handler + "'已存在");
                System.exit(1);
            }
            map.put(bean.getHandler(), bean);
        }
    }

    @Override
    protected void myInit() {

    }
}
