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

package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class ProcessTaskSqlTableFactory extends ModuleInitializedListenerBase {

	public static Map<String, ISqlTable> tableComponentMap = new HashMap<>();

	public static ISqlTable getHandler(String name) {
		return tableComponentMap.get(name);
	}

	@Override
	public void onInitialized(NeatLogicWebApplicationContext context) {
		Map<String, ISqlTable> myMap = context.getBeansOfType(ISqlTable.class);
		for (Map.Entry<String, ISqlTable> entry : myMap.entrySet()) {
			ISqlTable table = entry.getValue();
			tableComponentMap.put(table.getName(), table);
		}
	}

	@Override
	protected void myInit() {
		// TODO Auto-generated method stub
		
	}

}
