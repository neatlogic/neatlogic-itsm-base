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

package neatlogic.framework.process.workerdispatcher.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.dto.module.ModuleVo;
import neatlogic.framework.process.dto.WorkerDispatcherVo;
import neatlogic.framework.process.exception.workcenter.HandlerDispatchComponentTypeNotFoundException;
import neatlogic.framework.util.$;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@RootComponent
public class WorkerDispatcherFactory extends ModuleInitializedListenerBase {

	private static final Map<String, IWorkerDispatcher> componentMap = new HashMap<String, IWorkerDispatcher>();
	private static final Map<String, String> className2ModuleIdMap = new HashMap<>();

	public static IWorkerDispatcher getDispatcher(String name) {
		IWorkerDispatcher workerDispatcher = componentMap.get(name);
		if (workerDispatcher == null) {
			int index = name.lastIndexOf(".");
			workerDispatcher = componentMap.get(name.substring(index + 1));
		}
		if (workerDispatcher == null) {
            throw new HandlerDispatchComponentTypeNotFoundException(name);
		}
		return workerDispatcher;
	}

	public static List<WorkerDispatcherVo> getAllActiveWorkerDispatcher() {
		TenantContext tenantContext = TenantContext.get();
		List<ModuleVo> moduleList = tenantContext.getActiveModuleList();
		List<WorkerDispatcherVo> returnWorkerDispatcherList = new ArrayList<>();
		for (Map.Entry<String, IWorkerDispatcher> entry : componentMap.entrySet()) {
			String moduleId = className2ModuleIdMap.get(entry.getKey());
			for (ModuleVo moduleVo : moduleList) {
				if (moduleVo.getId().equalsIgnoreCase(moduleId)) {
					IWorkerDispatcher component = entry.getValue();
					WorkerDispatcherVo workerDispatcherVo = new WorkerDispatcherVo();
					workerDispatcherVo.setHandler(component.getClassName());
					workerDispatcherVo.setName($.t(component.getName()));
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
					workerDispatcherVo.setModuleId(moduleId);
					returnWorkerDispatcherList.add(workerDispatcherVo);
					break;
				}
			}
		}
		return returnWorkerDispatcherList;
	}

	@Override
	public void onInitialized(NeatLogicWebApplicationContext context) {
		Map<String, IWorkerDispatcher> myMap = context.getBeansOfType(IWorkerDispatcher.class);
		for (Map.Entry<String, IWorkerDispatcher> entry : myMap.entrySet()) {
			IWorkerDispatcher component = entry.getValue();
			String className = component.getClassName();
			if (StringUtils.isNotBlank(className)) {
				componentMap.put(className, component);
				int index = className.lastIndexOf(".");
				componentMap.put(className.substring(index + 1), component);
				className2ModuleIdMap.put(className, context.getId());
			}
		}
	}

    @Override
    protected void myInit() {

    }
}
