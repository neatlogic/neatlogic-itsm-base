package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNextStepOverOneException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -7909863133400893749L;

    public ProcessTaskNextStepOverOneException(Long processTaskId) {
        super("exception.process.processtasknextstepoveroneexception", processTaskId);
    }

}
