package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.process.workcenter.table.constvalue.ProcessSqlTypeEnum;
import neatlogic.framework.util.I18n;
import org.springframework.stereotype.Component;

/**
 * @Title: ProcessTaskTable
 * @Package: neatlogic.module.process.workcenter.core.table
 * @Description: 工单表
 * @Author: 89770
 * @Date: 2021/1/15 16:02
 **/
@Component
public class ProcessTaskSlaTimeSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask_sla_time";
    }

    @Override
    public String getShortName() {
        return "ptslt";
    }

    public enum FieldEnum {
        SLA_ID("sla_id",  new I18n("enum.process.processtaskslatimesqltable.fieldenum.sla_id")),
        EXPIRE_TIME("expire_time", new I18n("enum.process.fieldenum.expire_time")),
        REALEXPIRE_TIME("realexpire_time", new I18n("enum.process.fieldenum.realexpire_time")),
        TIME_LEFT("time_left", new I18n("enum.process.fieldenum.time_left")),
        STATUS("status", new I18n("enum.process.processtaskslatimesqltable.fieldenum.status")),
        REALTIME_LEFT("realtime_left", new I18n("enum.process.fieldenum.realtime_left")),
        CALCULATION_TIME("calculation_time", new I18n("enum.process.fieldenum.calculation_time"));
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
