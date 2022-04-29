/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.workcenter.dto;

import com.alibaba.fastjson.JSONArray;

public class WorkcenterConditionVo {
    private String uuid;
    private String name;
    private String type;
    private String expression;
    private JSONArray valueList;
    private JSONArray textList;
    private String expressionText;
    private String label;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public JSONArray getValueList() {
        return valueList;
    }

    public void setValueList(JSONArray valueList) {
        this.valueList = valueList;
    }

    public JSONArray getTextList() {
        return textList;
    }

    public void setTextList(JSONArray textList) {
        this.textList = textList;
    }

    public String getExpressionText() {
        return expressionText;
    }

    public void setExpressionText(String expressionText) {
        this.expressionText = expressionText;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
