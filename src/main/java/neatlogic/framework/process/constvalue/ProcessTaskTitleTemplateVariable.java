/*
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package neatlogic.framework.process.constvalue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.IEnum;

public enum ProcessTaskTitleTemplateVariable implements IEnum {
    USER_ID("userId", "${userId}", "当前用户ID"),
    USER_NAME("userName", "${userName}", "当前用户名称"),
    REGION_NAME("regionName", "${regionName}", "地域"),
    YYYYMMDD("yyyyMMdd", "${yyyyMMdd}", "当前日期"),
    ;
    private final String value;
    private final String expression;
    private final String text;

    ProcessTaskTitleTemplateVariable(String value, String expression, String text) {
        this.value = value;
        this.expression = expression;
        this.text = text;
    }

    @Override
    public JSONArray getValueTextList() {
        JSONArray array = new JSONArray();
        for (ProcessTaskTitleTemplateVariable type : values()) {
            array.add(new JSONObject().fluentPut("value", type.getExpression()).fluentPut("text", type.getText()));
        }
        return array;
    }

    public String getValue() {
        return value;
    }

    public String getExpression() {
        return expression;
    }

    public String getText() {
        return text;
    }
}
