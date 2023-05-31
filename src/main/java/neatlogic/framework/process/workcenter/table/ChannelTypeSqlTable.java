package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.util.I18n;
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
        UUID("uuid", new I18n("common.channeltypeuuid"),"channelTypeUuid"),
        NAME("name",new I18n("enum.process.channeltypesqltable.fieldenum.name"),"channelTypeName"),
        COLOR("color",new I18n("enum.process.channeltypesqltable.fieldenum.color"),"channelTypeColor")
        ;
        private final String name;
        private final I18n text;
        private final String proName;
        private final Boolean isPrimary;


        private FieldEnum(String _value, I18n _text) {
            this.name = _value;
            this.text = _text;
            this.proName = _value;
            this.isPrimary = false;
        }

        private FieldEnum(String _value, I18n _text,String _proName) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.isPrimary = false;
        }

        private FieldEnum(String _value, I18n _text,String _proName,Boolean _isPrimary) {
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
            return text.toString();
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
