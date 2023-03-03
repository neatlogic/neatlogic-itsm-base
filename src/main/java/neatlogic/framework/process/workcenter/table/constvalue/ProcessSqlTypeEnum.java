package neatlogic.framework.process.workcenter.table.constvalue;

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
    GROUP_COUNT("groupCount", "分组计数"),//目前暂用于dashboard
    SUB_GROUP_COUNT("subGroupCount", "二级分组计数"),//目前暂用于dashboard
    GROUP_SUM("groupSum", "分组累积总数"),
    GROUP_AVG_COST_TIME("group_avg_cost_time","平均处理耗时"),
    GROUP_AVG_RESPONSE_COST_TIME("group_avg_response_cost_time","平均响应耗时"),
    GROUP_RESPONSE_PUNCTUALITY("group_response_punctuality","响应准时率"),
    GROUP_HANDLE_PUNCTUALITY("group_handle_punctuality","处理准时率")
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
        return text;
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
