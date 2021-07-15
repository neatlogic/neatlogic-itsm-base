/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.constvalue;

/**
 * @author linbq
 * @since 2021/7/15 11:15
 **/
public enum ReplaceableText {
    SUBTASK("subtask", "子任务");
    private final String value;
    private final String Text;

    ReplaceableText(String value, String text) {
        this.value = value;
        Text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return Text;
    }
}
