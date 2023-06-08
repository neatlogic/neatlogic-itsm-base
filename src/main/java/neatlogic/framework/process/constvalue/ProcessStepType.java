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
