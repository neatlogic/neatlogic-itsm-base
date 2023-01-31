package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNextStepNameOrIdUnAssignException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -6064258442539939871L;

    public ProcessTaskNextStepNameOrIdUnAssignException() {
        super("缺少下一步骤的名称或id");
    }

}
