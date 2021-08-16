/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.workerpolicy.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;
import codedriver.framework.process.dto.WorkerPolicyVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RootComponent
public class WorkerPolicyHandlerFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IWorkerPolicyHandler> componentMap = new HashMap<String, IWorkerPolicyHandler>();

    private static final List<WorkerPolicyVo> workerPolicyList = new ArrayList<>();

    public static IWorkerPolicyHandler getHandler(String name) {
        if (!componentMap.containsKey(name) || componentMap.get(name) == null) {
            throw new RuntimeException("找不到类型为：" + name + "的处理人分配策略");
        }
        return componentMap.get(name);
    }

    public static List<WorkerPolicyVo> getAllActiveWorkerPolicy() {
        return workerPolicyList;
    }

    @Override
    public void onInitialized(CodedriverWebApplicationContext context) {
        Map<String, IWorkerPolicyHandler> myMap = context.getBeansOfType(IWorkerPolicyHandler.class);
        for (Map.Entry<String, IWorkerPolicyHandler> entry : myMap.entrySet()) {
            IWorkerPolicyHandler component = entry.getValue();
            if (component.getType() != null) {
                componentMap.put(component.getType(), component);
                WorkerPolicyVo workerPolicy = new WorkerPolicyVo();
                workerPolicy.setType(component.getType());
                workerPolicy.setName(component.getName());
                workerPolicy.setModuleId(context.getId());
                workerPolicyList.add(workerPolicy);
            }
        }
    }

    @Override
    protected void myInit() {

    }
}
