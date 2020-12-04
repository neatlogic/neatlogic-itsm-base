package codedriver.framework.process.processtaskserialnumberpolicy.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import codedriver.framework.applicationlistener.core.ApplicationListenerBase;
import codedriver.framework.common.RootComponent;
import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

@RootComponent
public class ProcessTaskSerialNumberPolicyFactory extends ApplicationListenerBase {

    private static Map<String, IProcessTaskSerialNumberPolicy> policyMap = new HashMap<>();
    private static List<ProcessTaskSerialNumberPolicyVo> policyList = new ArrayList<>();
    public static IProcessTaskSerialNumberPolicy getHandler(String handler) {
        return policyMap.get(handler);
    }
    
    public static List<ProcessTaskSerialNumberPolicyVo> getPolicyHandlerList(){
        return policyList;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, IProcessTaskSerialNumberPolicy> map = context.getBeansOfType(IProcessTaskSerialNumberPolicy.class);
        for(Map.Entry<String, IProcessTaskSerialNumberPolicy> entry : map.entrySet()) {
            IProcessTaskSerialNumberPolicy numberPolicy = entry.getValue();
            policyMap.put(numberPolicy.getHandler(), numberPolicy);
            ProcessTaskSerialNumberPolicyVo policy = new ProcessTaskSerialNumberPolicyVo();
            policy.setHandler(numberPolicy.getHandler());
            policy.setName(numberPolicy.getName());
            policy.setFormAttributeList(numberPolicy.makeupFormAttributeList());
            policyList.add(policy);
        }
    }

    @Override
    protected void myInit() {
        
    }

}
