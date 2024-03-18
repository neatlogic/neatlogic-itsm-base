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

package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stephandler.core.IProcessStepHandlerType;
import neatlogic.framework.util.$;

public enum ProcessStepHandlerType implements IProcessStepHandlerType {
    START("start", "start", "term.itsm.start"),
    OMNIPOTENT("omnipotent", "process", "term.itsm.omnipotent"),
    END("end", "end", "term.itsm.end"),
    CONDITION("condition", "converge", "term.itsm.condition"),
    DISTRIBUTARY("distributary", "converge", "term.itsm.distributary"),
    AUTOMATIC("automatic", "process", "term.itsm.automatic"),
    TIMER("timer", "process", "term.itsm.timer");

    private final String handler;
    private final String name;
    private final String type;

    ProcessStepHandlerType(String _handler, String _type, String _name) {
        this.handler = _handler;
        this.type = _type;
        this.name = _name;
    }

    public String getHandler() {
        return handler;
    }

    public String getName() {
        return $.t(name);
    }

    public String getType() {
        return type;
    }

}
