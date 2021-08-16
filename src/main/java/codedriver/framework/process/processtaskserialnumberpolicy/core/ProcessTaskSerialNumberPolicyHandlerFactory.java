/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.processtaskserialnumberpolicy.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;
import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RootComponent
public class ProcessTaskSerialNumberPolicyHandlerFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskSerialNumberPolicyHandler> policyMap = new HashMap<>();
    private static final List<ProcessTaskSerialNumberPolicyVo> policyList = new ArrayList<>();

    public static IProcessTaskSerialNumberPolicyHandler getHandler(String handler) {
        return policyMap.get(handler);
    }

    public static List<ProcessTaskSerialNumberPolicyVo> getPolicyHandlerList() {
        return policyList;
    }

    @Override
    public void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IProcessTaskSerialNumberPolicyHandler> map =
                context.getBeansOfType(IProcessTaskSerialNumberPolicyHandler.class);
        for (Map.Entry<String, IProcessTaskSerialNumberPolicyHandler> entry : map.entrySet()) {
            IProcessTaskSerialNumberPolicyHandler numberPolicy = entry.getValue();
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
