package neatlogic.framework.process.workcenter.table.constvalue;

import neatlogic.framework.util.$;

/**
 * @Title: FieldTypeEnum
 * @Package: neatlogic.framework.process.workcenter.table
 * @Description: TODO
 * @Author: 89770
 * @Date: 2021/1/25 18:18
 **/
public enum ProcessSqlTypeEnum {
    DISTINCT_ID("distinctId", "去重工单ID"),
    FIELD("field", "选择字段"),
    TOTAL_COUNT("totalCount", "总个数"),
    LIMIT_COUNT("limitCount", "部分个数"),
    FULL_TEXT("fullText", "全文检索"),
    ;//目前暂用于dashboard
    private final String name;
    private final String text;

    private ProcessSqlTypeEnum(String _value, String _text) {
        this.name = _value;
        this.text = _text;
    }

    public String getValue() {
        return name;
    }

    public String getText() {
        return $.t(text);
    }

    public static String getText(String value) {
        for (ProcessSqlTypeEnum f : ProcessSqlTypeEnum.values()) {
            if (f.getValue().equals(value)) {
                return f.getText();
            }
        }
        return "";
    }
}
