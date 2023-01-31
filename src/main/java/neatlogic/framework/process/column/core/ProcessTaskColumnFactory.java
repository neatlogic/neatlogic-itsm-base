/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.column.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.CodedriverWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class ProcessTaskColumnFactory extends ModuleInitializedListenerBase {

	public static Map<String, IProcessTaskColumn> columnComponentMap = new HashMap<>();

	public static IProcessTaskColumn getHandler(String name) {
		return columnComponentMap.get(name);
	}

	@Override
	public void onInitialized(CodedriverWebApplicationContext context) {
		Map<String, IProcessTaskColumn> myMap = context.getBeansOfType(IProcessTaskColumn.class);
		for (Map.Entry<String, IProcessTaskColumn> entry : myMap.entrySet()) {
			IProcessTaskColumn column = entry.getValue();
			columnComponentMap.put(column.getName(), column);
		}
	}

	@Override
	protected void myInit() {
		// TODO Auto-generated method stub
		
	}

}
