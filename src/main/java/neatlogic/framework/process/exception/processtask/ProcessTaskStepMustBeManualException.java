package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepMustBeManualException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -1806194129752166066L;

    public ProcessTaskStepMustBeManualException(Long processTaskId, String processTaskStepName) {
        super("nfpep.processtaskstepmustbemanualexception.processtaskstepmustbemanualexception", processTaskId, processTaskStepName);
    }

}
