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

package neatlogic.framework.process.audithandler.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@RootComponent
public class ProcessTaskStepAuditDetailHandlerFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskStepAuditDetailHandler> handlerMap = new HashMap<>();

    public static IProcessTaskStepAuditDetailHandler getHandler(String type) {
        IProcessTaskStepAuditDetailHandler handler = handlerMap.get(type);
        return handler;
    }

    @Override
    public void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IProcessTaskStepAuditDetailHandler> map = context.getBeansOfType(IProcessTaskStepAuditDetailHandler.class);
        for (Entry<String, IProcessTaskStepAuditDetailHandler> entry : map.entrySet()) {
            IProcessTaskStepAuditDetailHandler handler = entry.getValue();
            handlerMap.put(handler.getType(), handler);
        }
    }

    @Override
    protected void myInit() {

    }

}
