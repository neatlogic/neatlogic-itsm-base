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
public class UserTable implements ISqlTable {

    @Override
    public String getName() {
        return "user";
    }

    @Override
    public String getShortName() {
        return "u";
    }

    public enum FieldEnum {
        UUID("uuid", new I18n("common.useruuid")),
        USER_ID("user_id", new I18n("enum.process.fieldenum.user_id")),
        USER_NAME("user_name", new I18n("enum.process.fieldenum.user_name")),
        USER_INFO("user_info", new I18n("enum.process.fieldenum.user_info")),
        VIP_LEVEL("vip_level", new I18n("enum.process.fieldenum.vip_level")),
        PINYIN("pinyin", new I18n("enum.process.fieldenum.pinyin"));
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

        public String getProValue() {
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
