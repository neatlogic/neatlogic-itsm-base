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

package neatlogic.framework.process.constvalue;

import neatlogic.framework.common.constvalue.IEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.util.I18n;

import java.util.List;

public enum ProcessTaskStatus implements IEnum {
    RUNNING("running", new I18n("处理中"), "#2d84fb"),
    ABORTED("aborted", new I18n("已取消"), "#F9A825"),
    SUCCEED("succeed", new I18n("已完成"), "#25b865"),
    FAILED("failed", new I18n("异常"), "#f71010"),
    HANG("hang", new I18n("已挂起"), "#ffba5a"),
    SCORED("scored", new I18n("已评分"), "#25b865"),
    DRAFT("draft", new I18n("未提交"), "#8E949F");

    private final String status;
    private final I18n text;
    private final String color;

    ProcessTaskStatus(String _status, I18n _text, String _color) {
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
        for (ProcessTaskStatus s : ProcessTaskStatus.values()) {
            if (s.getValue().equals(_status)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _status) {
        for (ProcessTaskStatus s : ProcessTaskStatus.values()) {
            if (s.getValue().equals(_status)) {
                return s.getText();
            }
        }
        return "";
    }

    public static String getColor(String _status) {
        for (ProcessTaskStatus s : ProcessTaskStatus.values()) {
            if (s.getValue().equals(_status)) {
                return s.getColor();
            }
        }
        return "";
    }

    @Override
    public List getValueTextList() {
        JSONArray array = new JSONArray();
        for (ProcessTaskStatus type : values()) {
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
