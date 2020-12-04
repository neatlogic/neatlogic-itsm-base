package codedriver.framework.process.processtaskserialnumberpolicy.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import codedriver.framework.applicationlistener.core.ApplicationListenerBase;
import codedriver.framework.common.RootComponent;
import codedriver.framework.common.dto.ValueTextVo;

@RootComponent
public class ProcessTaskSerialNumberPolicyFactory extends ApplicationListenerBase {

    private static Map<String, IProcessTaskSerialNumberPolicy> policyMap = new HashMap<>();
    
    public static IProcessTaskSerialNumberPolicy getHandler(String handler) {
        return policyMap.get(handler);
    }
    
    public static List<ValueTextVo> getPolicyHandlerList(){
        List<ValueTextVo> resultList = new ArrayList<>();
        for(Map.Entry<String, IProcessTaskSerialNumberPolicy> entry : policyMap.entrySet()) {
            resultList.add(new ValueTextVo(entry.getValue().getHandler(), entry.getValue().getName()));
        }
        return resultList;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, IProcessTaskSerialNumberPolicy> map = context.getBeansOfType(IProcessTaskSerialNumberPolicy.class);
        for(Map.Entry<String, IProcessTaskSerialNumberPolicy> entry : map.entrySet()) {
            policyMap.put(entry.getValue().getHandler(), entry.getValue());
        }
    }

    @Override
    protected void myInit() {
        
    }

}
