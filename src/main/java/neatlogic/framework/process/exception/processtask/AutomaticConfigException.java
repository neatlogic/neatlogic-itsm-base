/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author lvzk
 * @since 2021/8/3 14:24
 **/
public class AutomaticConfigException extends ProcessTaskRuntimeException {
    private static final long serialVersionUID = -1795292676955541383L;

    public AutomaticConfigException(String jobName) {
        super("automaticConfig异常为null，jobName: " + jobName);
    }
}
