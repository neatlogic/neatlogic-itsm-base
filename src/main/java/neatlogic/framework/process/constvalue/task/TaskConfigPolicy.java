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
    ANY("any", new I18n("enum.process.taskconfigpolicy.any")), ALL("all", new I18n("enum.process.taskconfigpolicy.all"));
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
