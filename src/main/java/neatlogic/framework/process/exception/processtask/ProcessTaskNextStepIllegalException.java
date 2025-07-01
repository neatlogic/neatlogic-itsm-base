package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNextStepIllegalException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -1358428490096490765L;

    public ProcessTaskNextStepIllegalException(String processTaskStepName, String processTaskNextStepName) {
        super("nfpep.processtasknextstepillegalexception.processtasknextstepillegalexception", processTaskStepName, processTaskNextStepName);
    }

    public ProcessTaskNextStepIllegalException(Long processTaskId) {
        super("nfpep.processtasknextstepillegalexception.processtasknextstepillegalexception_a", processTaskId);
    }

}
