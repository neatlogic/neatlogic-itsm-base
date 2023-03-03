package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepUserUnAssignException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 7869255738264215901L;

    public ProcessTaskStepUserUnAssignException() {
        super("exception.process.processtaskstepuserunassignexception");
    }

}
