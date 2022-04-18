package codedriver.framework.process.workcenter.table;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: ProcessTaskTable
 * @Package: codedriver.module.process.workcenter.core.table
 * @Description: 工单表
 * @Author: 89770
 * @Date: 2021/1/15 16:02
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
@Component
public class PrioritySqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "priority";
    }

    @Override
    public String getShortName() {
        return "pri";
    }

    public enum FieldEnum {
        UUID("uuid", "服务目录UUID","priorityUuid",true),
        NAME("name", "服务目录名","priorityName"),
        COLOR("color", "颜色","priorityColor"),
        SORT("sort", "排序")
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

        private List<FieldEnum> getPrimaryFieldList(){
            List<FieldEnum> primaryFieldEnumList = new ArrayList<>();
            for (FieldEnum f : FieldEnum.values()) {
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
            for (FieldEnum f : FieldEnum.values()) {
                if (f.getValue().equals(value)) {
                    return f.getText();
                }
            }
            return "";
        }
    }
}
