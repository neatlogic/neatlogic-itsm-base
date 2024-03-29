package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.process.workcenter.table.constvalue.ProcessSqlTypeEnum;
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
        UUID("uuid", "用户UUID"),
        USER_ID("user_id","用户ID"),
        USER_NAME("user_name","用户名"),
        USER_INFO("user_info", "用户信息"),
        VIP_LEVEL("vip_level","vip等级"),
        IS_ACTIVE("is_active", "是否激活"),
        IS_DELETE("is_delete","是否删除"),
        PINYIN("pinyin", "拼音");
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
            return text;
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
