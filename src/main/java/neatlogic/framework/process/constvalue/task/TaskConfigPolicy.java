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

package neatlogic.framework.process.constvalue.task;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.IEnum;
import neatlogic.framework.util.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/1 16:20
 **/
public enum TaskConfigPolicy implements IEnum {
    ANY("any", new I18n("nfpct.taskconfigpolicy.text.any")), ALL("all", new I18n("nfpct.taskconfigpolicy.text.any_2"));
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
