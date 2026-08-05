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

import neatlogic.framework.util.$;

public enum ProcessStepType {
    START("start", "nfpc.processsteptype.text.start"),
    PROCESS("process", "nfpc.processsteptype.text.process"),
    END("end", "nfpc.processsteptype.text.end");

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
