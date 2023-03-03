/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.framework.process.dto;

import neatlogic.framework.dto.condition.ConditionConfigVo;
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
