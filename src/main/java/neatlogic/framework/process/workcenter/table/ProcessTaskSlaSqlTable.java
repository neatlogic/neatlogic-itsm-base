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
public class ProcessTaskSlaSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask_sla";
    }

    @Override
    public String getShortName() {
        return "ptsl";
    }

    public enum FieldEnum {
        PROCESSTASK_ID("processtask_id", new I18n("工单ID")),
        ID("id", new I18n("sla ID")),
        NAME("name", new I18n("sla 名")),
        CONFIG("config", new I18n("sla 配置"));
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
