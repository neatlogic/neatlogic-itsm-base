package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.process.workcenter.table.constvalue.ProcessSqlTypeEnum;
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
        ID("id", "步骤ID", "processTaskStepId"),
        NAME("name", "步骤名称", "processTaskStepName"),
        PROCESSTASK_ID("processtask_id","工单id", "processTaskId"),
        TYPE("type","节点类型", "processTaskStepType"),
        HANDLER("handler","步骤处理器", "processTaskStepHandler"),
        STATUS("status", "步骤状态", "processTaskStepStatus"),
        CONFIG_HASH("config_hash","配置hash", "processTaskStepConfigHash"),
        IS_ACTIVE("is_active", "步骤状态", "processTaskStepIsActive"),
        ACTIVE_TIME("active_time","激活时间", "processTaskStepActiveTime")
        ;
        private final String name;
        private final String text;
        private final String proName;

        private FieldEnum(String _value, String _text,String _proName) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
        }

        private FieldEnum(String _value, String _text) {
            this.name = _value;
            this.text = _text;
            this.proName = _value;
        }

        public String getValue() {
            return name;
        }

        public String getText() {
            return text;
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
