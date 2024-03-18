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

package neatlogic.framework.process.workerdispatcher.core;

import java.util.Objects;

/**
 * @author lvzk
 * @since 2021/8/19 11:30
 **/
public enum WorkerDispatcherForm {
    TEXT("text", "文本框组件"),
    USER_SELECT("userselect", "选择用户组件"),
    FORM_SELECT("formselect", "选择表单组件"),
    SELECT("select", "下拉框组件");
    private final String value;
    private final String text;

    private WorkerDispatcherForm(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public static WorkerDispatcherForm getParamMode(String _value) {
        for (WorkerDispatcherForm e : values()) {
            if (Objects.equals(e.getValue(), _value)) {
                return e;
            }
        }
        return null;
    }
}
