package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNextStepIllegalException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -1358428490096490765L;

    public ProcessTaskNextStepIllegalException(String processTaskStepName, String processTaskNextStepName) {
        super("exception.process.processtasknextstepillegalexception", processTaskStepName, processTaskNextStepName);
    }

    public ProcessTaskNextStepIllegalException(Long processTaskId) {
        super("exception.process.processtasknextstepillegalexception.1", processTaskId);
    }

}
