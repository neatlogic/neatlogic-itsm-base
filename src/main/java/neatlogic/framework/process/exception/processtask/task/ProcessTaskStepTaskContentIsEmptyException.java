/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.exception.processtask.task;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author laiwt
 * @since 2022/6/22 14:24
 **/
public class ProcessTaskStepTaskContentIsEmptyException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 1355052766826671175L;

    public ProcessTaskStepTaskContentIsEmptyException() {
        super("回复意见不能为空");
    }

}
