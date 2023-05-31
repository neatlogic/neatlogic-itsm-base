package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.process.workcenter.table.constvalue.ProcessSqlTypeEnum;
import neatlogic.framework.util.I18n;
import org.springframework.stereotype.Component;

/**
 * @Title: ProcessTaskStepTable
 * @Package: neatlogic.module.process.workcenter.core.table
 * @Description: TODO
 * @Author: 89770
 * @Date: 2021/1/15 16:37
 **/

@Component
public class ProcessTaskStepSqlTable implements ISqlTable {
    @Override
    public String getName() {
        return "processtask_step";
    }

    @Override
    public String getShortName() {
        return "pts";
    }

    public enum FieldEnum {
        ID("id", new I18n("common.stepid"), "processTaskStepId"),
        NAME("name", new I18n("enum.process.processtaskstepsqltable.fieldenum.name"), "processTaskStepName"),
        PROCESSTASK_ID("processtask_id", new I18n("common.workorderid"), "processTaskId"),
        TYPE("type", new I18n("enum.process.channeltypesqltable.fieldenum.type"), "processTaskStepType"),
        HANDLER("handler", new I18n("enum.process.fieldenum.handler"), "processTaskStepHandler"),
        STATUS("status", new I18n("common.stepstatus"), "processTaskStepStatus"),
        CONFIG_HASH("config_hash", new I18n("enum.process.fieldenum.config_hash"), "processTaskStepConfigHash"),
        IS_ACTIVE("is_active", new I18n("common.stepstatus"), "processTaskStepIsActive"),
        ACTIVE_TIME("active_time", new I18n("common.activetime"), "processTaskStepActiveTime");
        private final String name;
        private final I18n text;
        private final String proName;

        private FieldEnum(String _value, I18n _text, String _proName) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
        }

        private FieldEnum(String _value, I18n _text) {
            this.name = _value;
            this.text = _text;
            this.proName = _value;
        }

        public String getValue() {
            return name;
        }

        public String getText() {
            return text.toString();
        }

        public String getProName() {
            return proName;
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
