/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.constvalue;

/**
 * @author lvzk
 * @since 2021/9/6 11:55
 **/
public enum ProcessWorkcenterInitType {
    ALL_PROCESSTASK("allProcessTask", "所有工单"), DRAFT_PROCESSTASK("draftProcessTask", "我的草稿");
    private final String value;
    private final String name;

    private ProcessWorkcenterInitType(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getValue(String _value) {
        for (ProcessWorkcenterInitType s : ProcessWorkcenterInitType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (ProcessWorkcenterInitType s : ProcessWorkcenterInitType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }
}
