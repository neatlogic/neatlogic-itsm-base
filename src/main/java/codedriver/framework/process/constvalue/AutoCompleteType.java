/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.constvalue;

import codedriver.framework.common.constvalue.IEnum;
import codedriver.framework.common.dto.ValueTextVo;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * @author linbq
 * @since 2021/10/29 16:29
 **/
public enum AutoCompleteType implements IEnum {

    AUTOCOMPLETE("autocomplete", "自动流转"),
    AUTOAPPROVAL("autoapproval", "自动审批");

    private String value;
    private String text;

    AutoCompleteType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public List getValueTextList() {
        JSONArray array = new JSONArray();
        for (AutoCompleteType type : values()) {
            array.add(new ValueTextVo(type.value, type.text));
        }
        return array;
    }
}
