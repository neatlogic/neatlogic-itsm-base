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

public enum ProcessWorkcenterType {
    FACTORY("factory", "nfpc.processworkcentertype.text.factory"), SYSTEM("system", "nfpc.processworkcentertype.text.system"), CUSTOM("custom", "nfpc.processworkcentertype.text.custom");
    private final String value;
    private final String name;

    ProcessWorkcenterType(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return $.t(name);
    }

    public static String getValue(String _value) {
        for (ProcessWorkcenterType s : ProcessWorkcenterType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (ProcessWorkcenterType s : ProcessWorkcenterType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }

}
