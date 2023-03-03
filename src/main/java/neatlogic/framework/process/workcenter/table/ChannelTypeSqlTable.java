package neatlogic.framework.process.workcenter.table;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: ProcessTaskTable
 * @Package: neatlogic.module.process.workcenter.core.table
 * @Description: 工单表
 * @Author: 89770
 * @Date: 2021/1/15 16:02
 **/
@Component
public class ChannelTypeSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "channel_type";
    }

    @Override
    public String getShortName() {
        return "ct";
    }

    public enum FieldEnum {
        UUID("uuid", "服务类型UUID","channelTypeUuid"),
        NAME("name","服务类型名","channelTypeName"),
        COLOR("color","服务类型颜色","channelTypeColor")
        ;
        private final String name;
        private final String text;
        private final String proName;
        private final Boolean isPrimary;


        private FieldEnum(String _value, String _text) {
            this.name = _value;
            this.text = _text;
            this.proName = _value;
            this.isPrimary = false;
        }

        private FieldEnum(String _value, String _text,String _proName) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.isPrimary = false;
        }

        private FieldEnum(String _value, String _text,String _proName,Boolean _isPrimary) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.isPrimary = _isPrimary;
        }

        private List<PrioritySqlTable.FieldEnum> getPrimaryFieldList(){
            List<PrioritySqlTable.FieldEnum> primaryFieldEnumList = new ArrayList<>();
            for (PrioritySqlTable.FieldEnum f : PrioritySqlTable.FieldEnum.values()) {
                if(f.getPrimary()){
                    primaryFieldEnumList.add(f);
                }
            }
            return primaryFieldEnumList;
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

        public Boolean getPrimary() {
            return isPrimary;
        }

        public String getProName() {
            return proName;
        }

        public static String getText(String value) {
            for (PrioritySqlTable.FieldEnum f : PrioritySqlTable.FieldEnum.values()) {
                if (f.getValue().equals(value)) {
                    return f.getText();
                }
            }
            return "";
        }
    }
}