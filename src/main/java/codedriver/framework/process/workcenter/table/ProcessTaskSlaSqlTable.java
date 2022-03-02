package codedriver.framework.process.workcenter.table;

import codedriver.framework.process.workcenter.table.constvalue.ProcessSqlTypeEnum;
import org.springframework.stereotype.Component;

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
public class ProcessTaskSlaSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask_sla";
    }

    @Override
    public String getShortName() {
        return "ptsl";
    }

    public enum FieldEnum {
        PROCESSTASK_ID("processtask_id", "工单ID"),
        ID("id", "sla ID"),
        NAME("name", "sla 名"),
        CONFIG("config", "sla 配置")
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
            for (ProcessSqlTypeEnum f : ProcessSqlTypeEnum.values()) {
                if (f.getValue().equals(value)) {
                    return f.getText();
                }
            }
            return "";
        }
    }
}
