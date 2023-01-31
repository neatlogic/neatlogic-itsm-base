/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.workerdispatcher.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.bootstrap.CodedriverWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.dto.module.ModuleVo;
import neatlogic.framework.process.dto.WorkerDispatcherVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

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
				JSONArray configArray = component.getConfig();
				workerDispatcherVo.setConfig(configArray);
				//判断是否有form组件，提高前端性能
				for(int i=0;i< configArray.size();i++){
					JSONObject config = configArray.getJSONObject(i);
					if(Objects.equals(config.getString("type"),WorkerDispatcherForm.FORM_SELECT.getValue())){
						workerDispatcherVo.setIsHasForm(1);
					}
				}
				workerDispatcherVo.setModuleId(context.getId());
				workerDispatcherList.add(workerDispatcherVo);
			}
		}
	}

    @Override
    protected void myInit() {
        
    }
}
