package codedriver.framework.process.exception.processtask;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNextStepIllegalException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -1358428490096490765L;

    public ProcessTaskNextStepIllegalException(String processTaskStepName, String processTaskNextStepName) {
        super(processTaskNextStepName + "不是步骤：" + processTaskStepName + "的下一步骤");
    }

}
