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

package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stephandler.core.IProcessStepHandlerType;
import neatlogic.framework.util.I18n;

/**
 * @author linbq
 * @since 2021/9/2 14:40
 **/
public enum AutoexecProcessStepHandlerType implements IProcessStepHandlerType {
    AUTOEXEC("autoexec", "process", new I18n("自动化"));
    private final String handler;
    private final I18n name;
    private final String type;

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
