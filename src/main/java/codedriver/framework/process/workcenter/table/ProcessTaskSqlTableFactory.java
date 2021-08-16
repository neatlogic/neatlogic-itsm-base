/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.workcenter.table;

import codedriver.framework.applicationlistener.core.ModuleInitializedListenerBase;
import codedriver.framework.bootstrap.CodedriverWebApplicationContext;
import codedriver.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class ProcessTaskSqlTableFactory extends ModuleInitializedListenerBase {

	public static Map<String, ISqlTable> tableComponentMap = new HashMap<>();

	public static ISqlTable getHandler(String name) {
		return tableComponentMap.get(name);
	}

	@Override
	public void onInitialized(CodedriverWebApplicationContext context) {
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
