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

package neatlogic.framework.process.processtaskserialnumberpolicy.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RootComponent
public class ProcessTaskSerialNumberPolicyHandlerFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskSerialNumberPolicyHandler> policyMap = new HashMap<>();
    private static final List<IProcessTaskSerialNumberPolicyHandler> policyHandlerList = new ArrayList<>();

    public static IProcessTaskSerialNumberPolicyHandler getHandler(String handler) {
        IProcessTaskSerialNumberPolicyHandler processTaskSerialNumberPolicyHandler = policyMap.get(handler);
        if (processTaskSerialNumberPolicyHandler == null) {
            int index = handler.lastIndexOf(".");
            processTaskSerialNumberPolicyHandler = policyMap.get(handler.substring(index + 1));
        }
        return processTaskSerialNumberPolicyHandler;
    }

    public static List<ProcessTaskSerialNumberPolicyVo> getPolicyHandlerList() {
        List<ProcessTaskSerialNumberPolicyVo> policyList = new ArrayList<>();
        // 名称和表单属性可能包含当前语言文案，不能在模块启动时缓存，必须按请求语言重新生成。
        for (IProcessTaskSerialNumberPolicyHandler policyHandler : policyHandlerList) {
            ProcessTaskSerialNumberPolicyVo policy = new ProcessTaskSerialNumberPolicyVo();
            policy.setHandler(policyHandler.getHandler());
            policy.setName(policyHandler.getName());
            policy.setFormAttributeList(policyHandler.makeupFormAttributeList());
            policyList.add(policy);
        }
        return policyList;
    }

    @Override
    public void onInitialized(NeatLogicWebApplicationContext context) {
        // 模块重新初始化时先清理旧注册项，避免策略重复返回或继续引用旧 Spring Bean。
        policyMap.clear();
        policyHandlerList.clear();
        Map<String, IProcessTaskSerialNumberPolicyHandler> map =
                context.getBeansOfType(IProcessTaskSerialNumberPolicyHandler.class);
        for (Map.Entry<String, IProcessTaskSerialNumberPolicyHandler> entry : map.entrySet()) {
            IProcessTaskSerialNumberPolicyHandler numberPolicy = entry.getValue();
            String handler = numberPolicy.getHandler();
            int index = handler.lastIndexOf(".");
            policyMap.put(handler, numberPolicy);
            policyMap.put(handler.substring(index + 1), numberPolicy);
            policyHandlerList.add(numberPolicy);
        }
    }

    @Override
    protected void myInit() {

    }

}
