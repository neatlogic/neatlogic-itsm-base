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

package neatlogic.framework.process.constvalue.task;

import neatlogic.framework.common.constvalue.IEnum;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.util.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/1 16:20
 **/
public enum TaskConfigPolicy implements IEnum {
    ANY("any", new I18n("任意一人完成")), ALL("all", new I18n("所有人完成"));
    private final String value;
    private final I18n name;

    TaskConfigPolicy(String _value, I18n _name) {
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
        for (TaskConfigPolicy s : TaskConfigPolicy.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (TaskConfigPolicy s : TaskConfigPolicy.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }

    @Override
    public List getValueTextList() {
        List<Object> list = new ArrayList<>();
        for (TaskConfigPolicy type : TaskConfigPolicy.values()) {
            list.add(new JSONObject() {
                {
                    this.put("value", type.getValue());
                    this.put("text", type.getName());
                }
            });
        }
        return list;
    }
}
