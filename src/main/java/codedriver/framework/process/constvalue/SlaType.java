/*
 * Copyright(c) 2022 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.constvalue;

/**
 * @author linbq
 * @since 2022/2/22 16:27
 **/
public enum SlaType {
    RESPONSE("response", "响应"),
    HANDLE("handle", "处理");
    private final String value;
    private final String text;

    SlaType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
