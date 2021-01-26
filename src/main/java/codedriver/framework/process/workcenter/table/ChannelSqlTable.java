package codedriver.framework.process.workcenter.table;

import codedriver.framework.process.workcenter.table.constvalue.FieldTypeEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
public class ChannelSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "channel";
    }

    @Override
    public String getShortName() {
        return "c";
    }

    @Override
    public Map<ISqlTable, Map<String, String>> getDependTableColumnMap() {
        return new HashMap<>();
    }

    public enum FieldEnum {
        UUID("uuid", "服务类型UUID"),
        CHANNEL_TYPE_UUID("channel_type_uuid", "服务类型UUID"),
        PARENT_UUID("parent_uuid", "服务目录UUID"),
        NAME("name","服务名")
        ;
        private final String name;
        private final String text;

        private FieldEnum(String _value, String _text) {
            this.name = _value;
            this.text = _text;
        }

        public String getValue() {
            return name;
        }

        public String getText() {
            return text;
        }

        public static String getText(String value) {
            for (FieldTypeEnum f : FieldTypeEnum.values()) {
                if (f.getValue().equals(value)) {
                    return f.getText();
                }
            }
            return "";
        }
    }
}
