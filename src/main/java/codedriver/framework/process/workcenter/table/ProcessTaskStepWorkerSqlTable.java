package codedriver.framework.process.workcenter.table;

import codedriver.framework.process.workcenter.table.constvalue.FieldTypeEnum;
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
public class ProcessTaskStepWorkerSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask_step_worker";
    }

    @Override
    public String getShortName() {
        return "ptsw";
    }

    public enum FieldEnum {
        PROCESSTASK_ID("processtask_id", "工单ID"),
        PROCESSTASK_STEP_ID("processtask_step_id", "工单步骤ID"),
        TYPE("type","用户/组/角色 类型"),
        UUID("uuid","用户/组/角色 uuid"),
        USER_TYPE("user_type","处理人类型")
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
