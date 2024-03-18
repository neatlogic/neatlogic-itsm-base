/*Copyright (C) 2023  深圳极向量科技有限公司 All Rights Reserved.

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
 * @author linbq
 * @since 2021/7/15 11:15
 **/
public enum ReplaceableText {
    CONTENT_DETAILS("replaceableContentDetails", new I18n("内容详情")),
    STEP_LIST("replaceableSteplist", new I18n("步骤日志")),
    TIME_LINE("replaceableTimeLine", new I18n("时间线")),
    RELATION_LIST("replaceableRelationlist", new I18n("关联工单"));
    private final String value;
    private final I18n text;

    ReplaceableText(String value, I18n text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text.toString();
    }
}
