package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.process.workcenter.table.constvalue.ProcessSqlTypeEnum;
import org.springframework.stereotype.Component;

import neatlogic.framework.util.$;
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
        UUID("uuid", "nfpwt.usertable.text.uuid"),
        USER_ID("user_id","nfpwt.usertable.text.user_id"),
        USER_NAME("user_name","nfpwt.usertable.text.user_name"),
        USER_INFO("user_info", "nfpwt.usertable.text.user_info"),
        VIP_LEVEL("vip_level","nfpwt.usertable.text.vip_level"),
        IS_ACTIVE("is_active", "nfpwt.usertable.text.is_active"),
        IS_DELETE("is_delete","nfpwt.usertable.text.is_delete"),
        PINYIN("pinyin", "nfpwt.usertable.text.pinyin");
        private final String name;
        private final String text;
        private final String proName;
        private String handlerName;

        private FieldEnum(String _value, String _text) {
            this.name = _value;
            this.text = _text;
            this.handlerName = _value;
            this.proName = _value;
        }

        private FieldEnum(String _value, String _text, String _proName, String _handlerName) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.handlerName = _handlerName;
        }

        public String getValue() {
            return name;
        }

        public String getText() {
            return $.t(text);
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
