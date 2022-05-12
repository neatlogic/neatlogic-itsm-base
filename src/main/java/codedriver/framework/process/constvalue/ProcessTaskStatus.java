/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.constvalue;

import codedriver.framework.common.constvalue.IEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public enum ProcessTaskStatus implements IEnum {
    RUNNING("running", "处理中", "#2d84fb"),
    ABORTED("aborted", "已取消", "#F9A825"),
    SUCCEED("succeed", "已完成", "#25b865"),
    PENDING("pending", "待处理", "#8E949F"),
    FAILED("failed", "异常", "#f71010"),
    //	ABORTING("aborting", "终止中","#8E949F"),
    BACK("back", "已回退", "#8E949F"),
    HANG("hang", "已挂起", "#ffba5a"),
    SCORED("scored", "已评分", "#25b865"),
    DRAFT("draft", "未提交", "#8E949F");

    private final String status;
    private final String text;
    private final String color;

    ProcessTaskStatus(String _status, String _text, String _color) {
        this.status = _status;
        this.text = _text;
        this.color = _color;
    }

    public String getValue() {
        return status;
    }

    public String getText() {
        return text;
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

    public static JSONObject getJson(String _status) {
        JSONObject statusJson = new JSONObject();
        for (ProcessTaskStatus s : ProcessTaskStatus.values()) {
            if (s.getValue().equals(_status)) {
                statusJson.put("value", s.getValue());
                statusJson.put("color", s.getColor());
                statusJson.put("text", s.getText());
                break;
            }
        }
        return statusJson;
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
