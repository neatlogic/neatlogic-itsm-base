/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.workerdispatcher.core;

import java.util.Objects;

/**
 * @author lvzk
 * @since 2021/8/19 11:30
 **/
public enum WorkerDispatcherForm {
    TEXT("text", "文本框组件"),
    USER_SELECT("userselect", "选择用户组件"),
    FORM_SELECT("formselect", "选择表单组件"),
    SELECT("select", "下拉框组件");
    private final String value;
    private final String text;

    private WorkerDispatcherForm(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public static WorkerDispatcherForm getParamMode(String _value) {
        for (WorkerDispatcherForm e : values()) {
            if (Objects.equals(e.getValue(), _value)) {
                return e;
            }
        }
        return null;
    }
}
