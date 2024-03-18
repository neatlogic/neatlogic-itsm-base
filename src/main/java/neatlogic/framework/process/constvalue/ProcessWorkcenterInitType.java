/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

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

import neatlogic.framework.util.I18n;

/**
 * @author lvzk
 * @since 2021/9/6 11:55
 **/
public enum ProcessWorkcenterInitType {
    ALL_PROCESSTASK("allProcessTask", new I18n("所有工单")),
    DRAFT_PROCESSTASK("draftProcessTask", new I18n("我的草稿")),
    DONE_OF_MINE_PROCESSTASK("doneOfMineProcessTask", new I18n("我的已办")),
    PROCESSING_OF_MINE_PROCESSTASK("processingOfMineProcessTask", new I18n("我的待办"));
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
