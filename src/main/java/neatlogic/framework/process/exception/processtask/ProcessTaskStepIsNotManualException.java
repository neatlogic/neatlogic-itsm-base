package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepIsNotManualException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 2699169308437728135L;

    public ProcessTaskStepIsNotManualException(Long processTaskId, String processTaskStepName) {
        super("工单：{0}的{1}步骤无需人工处理", processTaskId, processTaskStepName);
    }

}
