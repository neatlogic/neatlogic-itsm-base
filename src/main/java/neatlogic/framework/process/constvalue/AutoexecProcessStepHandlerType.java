/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stephandler.core.IProcessStepHandlerType;
import neatlogic.framework.util.I18n;

/**
 * @author linbq
 * @since 2021/9/2 14:40
 **/
public enum AutoexecProcessStepHandlerType implements IProcessStepHandlerType {
    AUTOEXEC("autoexec", "process", new I18n("common.autoexec"));
    private String handler;
    private I18n name;
    private String type;

    AutoexecProcessStepHandlerType(String handler, String type, I18n name) {
        this.handler = handler;
        this.name = name;
        this.type = type;
    }
    @Override
    public String getHandler() {
        return handler;
    }

    @Override
    public String getName() {
        return name.toString();
    }

    @Override
    public String getType() {
        return type;
    }
}
