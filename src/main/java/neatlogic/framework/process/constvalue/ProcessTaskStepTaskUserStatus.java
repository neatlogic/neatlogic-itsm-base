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

package neatlogic.framework.process.constvalue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.IEnum;
import neatlogic.framework.util.I18n;

import java.util.List;

public enum ProcessTaskStepTaskUserStatus implements IEnum {
    SUCCEED("succeed", new I18n("enum.process.processtasksteptaskuserstatus.succeed"), "#25b865"),
    PENDING("pending", new I18n("enum.process.processtasksteptaskuserstatus.pending"), "#8E949F")
    ;

    private final String status;
    private final I18n text;
    private final String color;

    ProcessTaskStepTaskUserStatus(String _status, I18n _text, String _color) {
        this.status = _status;
        this.text = _text;
        this.color = _color;
    }

    public String getValue() {
        return status;
    }

    public String getText() {
        return text.toString();
    }

    public String getColor() {
        return color;
    }

    public static String getValue(String _status) {
        for (ProcessTaskStepTaskUserStatus s : ProcessTaskStepTaskUserStatus.values()) {
            if (s.getValue().equals(_status)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _status) {
        for (ProcessTaskStepTaskUserStatus s : ProcessTaskStepTaskUserStatus.values()) {
            if (s.getValue().equals(_status)) {
                return s.getText();
            }
        }
        return "";
    }

    public static String getColor(String _status) {
        for (ProcessTaskStepTaskUserStatus s : ProcessTaskStepTaskUserStatus.values()) {
            if (s.getValue().equals(_status)) {
                return s.getColor();
            }
        }
        return "";
    }

    @Override
    public List getValueTextList() {
        JSONArray array = new JSONArray();
        for (ProcessTaskStepTaskUserStatus type : values()) {
            array.add(new JSONObject() {
                {
                    this.put("value", type.getValue());
                    this.put("text", type.getText());
                }
            });
        }
        return array;
    }

    @Override
    public String getEnumName() {
        return "工单状态";
    }
}