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

import neatlogic.framework.util.I18n;

/**
 * @author lvzk
 * @since 2021/9/6 11:55
 **/
public enum ProcessWorkcenterInitType {
    ALL_PROCESSTASK("allProcessTask", new I18n("nfpc.processworkcenterinittype.text.all_processtask")),
    DRAFT_PROCESSTASK("draftProcessTask", new I18n("nfpc.processworkcenterinittype.text.draft_processtask")),
    DONE_OF_MINE_PROCESSTASK("doneOfMineProcessTask", new I18n("nfpc.processworkcenterinittype.text.done_of_mine_processtask")),
    PROCESSING_OF_MINE_PROCESSTASK("processingOfMineProcessTask", new I18n("nfpc.processworkcenterinittype.text.processing_of_mine_processtask"));
    private final String value;
    private final I18n name;

    private ProcessWorkcenterInitType(String _value, I18n _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name.toString();
    }

    public static String getValue(String _value) {
        for (ProcessWorkcenterInitType s : ProcessWorkcenterInitType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (ProcessWorkcenterInitType s : ProcessWorkcenterInitType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }
}
