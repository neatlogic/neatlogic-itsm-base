/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class AuthStringIrregularException extends ApiRuntimeException {
    public AuthStringIrregularException(String authString) {
        super("权限数据“" + authString + "”不符合规范");
    }
}
