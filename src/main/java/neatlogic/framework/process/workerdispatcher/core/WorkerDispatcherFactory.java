/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
		if (!componentMap.containsKey(name) || componentMap.get(name) == null) {
            throw new HandlerDispatchComponentTypeNotFoundException(name);
		}
		return componentMap.get(name);
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
			if (StringUtils.isNotBlank(component.getClassName())) {
				componentMap.put(component.getClassName(), component);
				className2ModuleIdMap.put(component.getClassName(), context.getId());
			}
		}
	}

    @Override
    protected void myInit() {

    }
}
