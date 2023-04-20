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
public class ProcessTaskSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask";
    }

    @Override
    public String getShortName() {
        return "pt";
    }

    public enum FieldEnum {
        ID("id", new I18n("enum.process.fieldenum.id")),
        SERIAL_NUMBER("serial_number", new I18n("enum.process.fieldenum.serial_number"), "serialNumber", "serialnumber"),
        START_TIME("start_time", new I18n("enum.process.fieldenum.start_time")),
        END_TIME("end_time", new I18n("enum.process.fieldenum.end_time")),
        OWNER("owner", new I18n("enum.process.fieldenum.owner")),
        REPORTER("reporter", new I18n("enum.process.fieldenum.reporter")),
        STATUS("status", new I18n("enum.process.fieldenum.status"), "processTaskStatus", "status"),
        PRIORITY_UUID("priority_uuid", new I18n("enum.process.fieldenum.priority_uuid")),
        CHANNEL_UUID("channel_uuid", new I18n("enum.process.fieldenum.channel_uuid")),
        IS_SHOW("is_show", new I18n("enum.process.fieldenum.is_show")),
        NEED_SCORE("need_score", new I18n("enum.process.fieldenum.need_score")),
        TITLE("title", new I18n("enum.process.fieldenum.title"));
        private final String name;
        private final I18n text;
        private final String proName;
        private String handlerName;

        private FieldEnum(String _value, I18n _text) {
            this.name = _value;
            this.text = _text;
            this.handlerName = _value;
            this.proName = _value;
        }

        private FieldEnum(String _value, I18n _text, String _proName, String _handlerName) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.handlerName = _handlerName;
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

        public String getHandlerName() {
            if (handlerName == null) {
                handlerName = name;
            }
            return handlerName;
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
