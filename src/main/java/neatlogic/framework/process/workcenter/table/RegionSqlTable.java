package neatlogic.framework.process.workcenter.table;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import neatlogic.framework.util.$;
@Component
public class RegionSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "region";
    }

    @Override
    public String getShortName() {
        return "r";
    }

    public enum FieldEnum {
        ID("id", "id", "regionId"),
        UPWARD_NAME_PATH("upward_name_path", "nfpwt.regionsqltable.text.upward_name_path", "regionUpwardNamePath"),
        NAME("name", "nfpwt.regionsqltable.text.name", "regionName");
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

        private FieldEnum(String _value, String _text, String _proName) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.isPrimary = false;
        }

        private FieldEnum(String _value, String _text, String _proName, Boolean _isPrimary) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.isPrimary = _isPrimary;
        }

        private List<PrioritySqlTable.FieldEnum> getPrimaryFieldList() {
            List<PrioritySqlTable.FieldEnum> primaryFieldEnumList = new ArrayList<>();
            for (PrioritySqlTable.FieldEnum f : PrioritySqlTable.FieldEnum.values()) {
                if (f.getPrimary()) {
                    primaryFieldEnumList.add(f);
                }
            }
            return primaryFieldEnumList;
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
