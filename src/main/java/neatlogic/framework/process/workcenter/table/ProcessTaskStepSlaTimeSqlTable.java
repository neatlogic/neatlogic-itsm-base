package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.process.workcenter.table.constvalue.ProcessSqlTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Title: ProcessTaskStepTable
 * @Package: neatlogic.module.process.workcenter.core.table
 * @Description: TODO
 * @Author: 89770
 * @Date: 2022/2/28 16:37
 **/

@Component
public class ProcessTaskStepSlaTimeSqlTable implements ISqlTable {
    @Override
    public String getName() {
        return "processtask_step_sla_time";
    }

    @Override
    public String getShortName() {
        return "ptsst";
    }

    public enum FieldEnum {
        PROCESSTASK_STEP_ID("processtask_step_id", "步骤ID"),
        TYPE("type", "类型"),
        PROCESSTASK_ID("processtask_id","工单id"),
        TIME_COST("time_cost","耗时"),
        IS_TIMEOUT("is_timeout","耗时")
        ;
        private final String name;
        private final String text;

        private FieldEnum(String _value, String _text) {
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
}
