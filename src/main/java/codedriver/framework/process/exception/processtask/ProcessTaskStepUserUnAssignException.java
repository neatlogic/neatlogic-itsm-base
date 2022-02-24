package codedriver.framework.process.exception.processtask;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepUserUnAssignException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 7869255738264215901L;

    public ProcessTaskStepUserUnAssignException() {
        super("流程步骤缺少处理人，请指定处理人");
    }

}
