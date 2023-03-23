package neatlogic.framework.process.workcenter.table.constvalue;

import neatlogic.framework.util.I18nUtils;

/**
 * @Title: FieldTypeEnum
 * @Package: neatlogic.framework.process.workcenter.table
 * @Description: TODO
 * @Author: 89770
 * @Date: 2021/1/25 18:18
 **/
public enum ProcessSqlTypeEnum {
    DISTINCT_ID("distinctId", "enum.process.processsqltypeenum.distinct_id"),
    FIELD("field", "enum.process.processsqltypeenum.field"),
    TOTAL_COUNT("totalCount", "enum.process.processsqltypeenum.total_count"),
    LIMIT_COUNT("limitCount", "enum.process.processsqltypeenum.limit_count"),
    FULL_TEXT("fullText", "enum.process.processsqltypeenum.full_text"),
    GROUP_COUNT("groupCount", "enum.process.processsqltypeenum.group_count"),//目前暂用于dashboard
    SUB_GROUP_COUNT("subGroupCount", "enum.process.processsqltypeenum.sub_group_count"),//目前暂用于dashboard
    GROUP_SUM("groupSum", "enum.process.processsqltypeenum.group_sum"),
    GROUP_AVG_COST_TIME("group_avg_cost_time","enum.process.processsqltypeenum.group_avg_cost_time"),
    GROUP_AVG_RESPONSE_COST_TIME("group_avg_response_cost_time","enum.process.processsqltypeenum.group_avg_response_cost_time"),
    GROUP_RESPONSE_PUNCTUALITY("group_response_punctuality","enum.process.processsqltypeenum.group_response_punctuality"),
    GROUP_HANDLE_PUNCTUALITY("group_handle_punctuality","enum.process.processsqltypeenum.group_handle_punctuality")
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
        return I18nUtils.getMessage(text);
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
