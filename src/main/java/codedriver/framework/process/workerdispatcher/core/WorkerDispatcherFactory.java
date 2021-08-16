/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.workerdispatcher.core;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;
import codedriver.framework.dto.ModuleVo;
import codedriver.framework.process.dto.WorkerDispatcherVo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RootComponent
public class WorkerDispatcherFactory extends ModuleInitializedListenerBase {

	private static final Map<String, IWorkerDispatcher> componentMap = new HashMap<String, IWorkerDispatcher>();
	private static final List<WorkerDispatcherVo> workerDispatcherList = new ArrayList<>();

	public static IWorkerDispatcher getDispatcher(String name) {
		if (!componentMap.containsKey(name) || componentMap.get(name) == null) {
			throw new RuntimeException("找不到类型为：" + name + "的处理人分派组件");
		}
		return componentMap.get(name);
	}

	public static List<WorkerDispatcherVo> getAllActiveWorkerDispatcher() {
		TenantContext tenantContext = TenantContext.get();
		List<ModuleVo> moduleList = tenantContext.getActiveModuleList();
		List<WorkerDispatcherVo> returnWorkerDispatcherList = new ArrayList<>();
		for (WorkerDispatcherVo workerDispatcherVo : workerDispatcherList) {
			for (ModuleVo moduleVo : moduleList) {
				if (moduleVo.getId().equalsIgnoreCase(workerDispatcherVo.getModuleId())) {
					returnWorkerDispatcherList.add(workerDispatcherVo);
					break;
				}
			}
		}
		return returnWorkerDispatcherList;
	}

	@Override
	public void onInitialized(CodedriverWebApplicationContext context) {
		Map<String, IWorkerDispatcher> myMap = context.getBeansOfType(IWorkerDispatcher.class);
		for (Map.Entry<String, IWorkerDispatcher> entry : myMap.entrySet()) {
			IWorkerDispatcher component = entry.getValue();
			if (StringUtils.isNotBlank(component.getClassName())) {
				componentMap.put(component.getClassName(), component);
				WorkerDispatcherVo workerDispatcherVo = new WorkerDispatcherVo();
				workerDispatcherVo.setHandler(component.getClassName());
				workerDispatcherVo.setName(component.getName());
				workerDispatcherVo.setIsActive(1);
				workerDispatcherVo.setHelp(component.getHelp());
				workerDispatcherVo.setConfig(component.getConfig());
				workerDispatcherVo.setModuleId(context.getId());
				workerDispatcherList.add(workerDispatcherVo);
			}
		}
	}

    @Override
    protected void myInit() {
        
    }
}
