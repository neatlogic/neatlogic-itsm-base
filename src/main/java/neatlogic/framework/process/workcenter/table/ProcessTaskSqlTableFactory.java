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
