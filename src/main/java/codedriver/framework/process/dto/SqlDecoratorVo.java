/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.dto.condition.ConditionConfigVo;
import com.alibaba.fastjson.JSONObject;

public abstract class SqlDecoratorVo extends ConditionConfigVo {
    private static final long serialVersionUID = 7450999412019135892L;

    private String sqlFieldType;

    public SqlDecoratorVo(JSONObject jsonObj) {
        super(jsonObj);
    }

    public SqlDecoratorVo() {
    }

    public String getSqlFieldType() {
        return sqlFieldType;
    }

    public void setSqlFieldType(String sqlFieldType) {
        this.sqlFieldType = sqlFieldType;
    }

    public abstract void init();

}
