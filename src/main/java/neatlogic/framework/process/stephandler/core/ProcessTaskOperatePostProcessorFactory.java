/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.dto.ProcessTaskStepVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RootComponent
public class ProcessTaskOperatePostProcessorFactory extends ModuleInitializedListenerBase {

    private static final List<IProcessTaskOperatePostProcessor> postProcessorList = new ArrayList<>();

    public static void invokePostProcessorsAfterProcessTaskStepOperate(ProcessTaskStepVo currentProcessTaskStepVo, IOperationType operationType) {
        for (IProcessTaskOperatePostProcessor postProcessor : postProcessorList) {
            postProcessor.postProcessAfterProcessTaskStepOperate(currentProcessTaskStepVo, operationType);
        }
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IProcessTaskOperatePostProcessor> map = context.getBeansOfType(IProcessTaskOperatePostProcessor.class);
        for (Map.Entry<String, IProcessTaskOperatePostProcessor> entry : map.entrySet()) {
            postProcessorList.add(entry.getValue());
        }
    }

    @Override
    protected void myInit() {

    }
}
