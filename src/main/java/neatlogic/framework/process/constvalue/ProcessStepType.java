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

import neatlogic.framework.util.$;

public enum ProcessStepType {
    START("start", "开始"),
    PROCESS("process", "处理节点"),
    END("end", "结束");

    private final String type;
    private final String name;

    ProcessStepType(String _type, String _name) {
        this.type = _type;
        this.name = _name;
    }

    public String getValue() {
        return type;
    }

    public String getName() {
        return $.t(name);
    }

    public static String getValue(String _type) {
        for (ProcessStepType s : ProcessStepType.values()) {
            if (s.getValue().equals(_type)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _type) {
        for (ProcessStepType s : ProcessStepType.values()) {
            if (s.getValue().equals(_type)) {
                return s.getName();
            }
        }
        return "";
    }

}
