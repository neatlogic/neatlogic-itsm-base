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
public class ProcessTaskSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask";
    }

    @Override
    public String getShortName() {
        return "pt";
    }

    @Override
    public Map<ISqlTable, Map<String, String>> getDependTableColumnMap() {
        return new HashMap<>();
    }

    public enum FieldEnum {
        ID("id", "工单ID"),
        SERIAL_NUMBER("serial_number", "工单号"),
        START_TIME("start_time", "创建时间"),
        END_TIME("end_time", "结束时间"),
        OWNER("owner", "上报人"),
        REPORTER("reporter", "上报人"),
        STATUS("status", "工单状态"),
        PRIORITY_UUID("priority_uuid", "工单状态"),
        CHANNEL_UUID("channel_uuid", "工单状态"),
        IS_SHOW("is_show", "工单是否隐藏"),
        TITLE("title", "标题");
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
