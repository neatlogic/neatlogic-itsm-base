/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.processtask.task;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author lvzk
 * @since 2021/8/31 14:24
 **/
public class ProcessTaskStepTaskUserCountIllegalException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 7140754332656371637L;

    public ProcessTaskStepTaskUserCountIllegalException(String name,Integer num) {
        super("当前 '"+name+"' 需选择"+num+"位处理人");
    }
}
