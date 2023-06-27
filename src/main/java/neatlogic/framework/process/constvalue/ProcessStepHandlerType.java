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

package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stephandler.core.IProcessStepHandlerType;
import neatlogic.framework.util.I18n;

public enum ProcessStepHandlerType implements IProcessStepHandlerType {
    START("start", "start", new I18n("开始")),
    OMNIPOTENT("omnipotent", "process", new I18n("通用节点")),
    END("end", "end", new I18n("结束")),
    CONDITION("condition", "converge", new I18n("条件")),
    DISTRIBUTARY("distributary", "converge", new I18n("分流")),
    OCTOPUS("octopus", "process", new I18n("自动化")),
    AUTOMATIC("automatic", "process", new I18n("自动处理")),
    TIMER("timer", "process", new I18n("定时节点"));

    private final String handler;
    private final I18n name;
    private final String type;

    ProcessStepHandlerType(String _handler, String _type, I18n _name) {
        this.handler = _handler;
        this.type = _type;
        this.name = _name;
    }

    public String getHandler() {
        return handler;
    }

    public String getName() {
        return name.toString();
    }

    public String getType() {
        return type;
    }

}
