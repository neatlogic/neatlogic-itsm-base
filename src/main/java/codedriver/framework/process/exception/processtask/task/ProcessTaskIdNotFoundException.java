package codedriver.framework.process.exception.processtask.task;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @author longrf
 * @date 2022/1/6 6:29 下午
 */
public class ProcessTaskIdNotFoundException extends ApiRuntimeException {
    public ProcessTaskIdNotFoundException() {
        super("工单id找不到");
    }
}
