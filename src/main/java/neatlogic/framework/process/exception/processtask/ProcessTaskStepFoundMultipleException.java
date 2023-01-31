package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepFoundMultipleException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 2271551252908108256L;

    public ProcessTaskStepFoundMultipleException(String processTaskStepName) {
        super("存在多个名为：'" + processTaskStepName + "'的流程步骤");
    }

}
