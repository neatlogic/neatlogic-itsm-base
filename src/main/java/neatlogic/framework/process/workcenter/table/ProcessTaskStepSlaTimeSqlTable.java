package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.process.workcenter.table.constvalue.ProcessSqlTypeEnum;
import neatlogic.framework.util.I18n;
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
        PROCESSTASK_STEP_ID("processtask_step_id", new I18n("common.stepid")),
        TYPE("type", new I18n("common.type")),
        PROCESSTASK_ID("processtask_id", new I18n("common.workorderid")),
        TIME_COST("time_cost", new I18n("common.costtime")),
        IS_TIMEOUT("is_timeout", new I18n("common.costtime"));
        private final String name;
        private final I18n text;

        private FieldEnum(String _value, I18n _text) {
            this.name = _value;
            this.text = _text;
        }

        public String getValue() {
            return name;
        }

        public String getText() {
            return text.toString();
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
