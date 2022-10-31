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
public class ProcessTaskSlaTimeSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask_sla_time";
    }

    @Override
    public String getShortName() {
        return "ptslt";
    }

    public enum FieldEnum {
        SLA_ID("sla_id", "sla ID"),
        EXPIRE_TIME("expire_time", "超时时间点"),
        REALEXPIRE_TIME("realexpire_time", "自然超时时间点"),
        TIME_LEFT("time_left", "剩余时间"),
        STATUS("status", "状态"),
        REALTIME_LEFT("realtime_left", "自然剩余时间"),
        CALCULATION_TIME("calculation_time", "上次耗时计算时间点")
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
