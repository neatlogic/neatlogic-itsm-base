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

package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.operationauth.core.IOperationType;

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
