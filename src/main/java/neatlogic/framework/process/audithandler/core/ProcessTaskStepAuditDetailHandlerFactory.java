/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
