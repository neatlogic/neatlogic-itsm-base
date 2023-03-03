package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepIsNotManualException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 2699169308437728135L;

    public ProcessTaskStepIsNotManualException(Long processTaskId, String processTaskStepName) {
        super("exception.process.processtaskstepisnotmanualexception", processTaskId, processTaskStepName);
    }

}
