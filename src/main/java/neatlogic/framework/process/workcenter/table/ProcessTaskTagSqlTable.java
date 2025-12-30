package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.util.I18n;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessTaskTagSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask_tag";
    }

    @Override
    public String getShortName() {
        return "pttag";
    }

    public enum FieldEnum {
        PROCESSTASK_ID("processtask_id", new I18n("term.itsm.processtaskid"), "processTaskId"),
        TAG_ID("tag_id", new I18n("common.tagid")),
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

        private FieldEnum(String _value, I18n _text, String _proName) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.isPrimary = false;
        }

        private FieldEnum(String _value, I18n _text, String _proName, Boolean _isPrimary) {
            this.name = _value;
            this.text = _text;
            this.proName = _proName;
            this.isPrimary = _isPrimary;
        }

        private List<ProcessTaskTagSqlTable.FieldEnum> getPrimaryFieldList() {
            List<ProcessTaskTagSqlTable.FieldEnum> primaryFieldEnumList = new ArrayList<>();
            for (ProcessTaskTagSqlTable.FieldEnum f : ProcessTaskTagSqlTable.FieldEnum.values()) {
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
            for (ProcessTaskTagSqlTable.FieldEnum f : ProcessTaskTagSqlTable.FieldEnum.values()) {
                if (f.getValue().equals(value)) {
                    return f.getText();
                }
            }
            return "";
        }
    }
}
