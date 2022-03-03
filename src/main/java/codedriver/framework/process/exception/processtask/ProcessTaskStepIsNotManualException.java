package codedriver.framework.process.exception.processtask;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepIsNotManualException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 2699169308437728135L;

    public ProcessTaskStepIsNotManualException(Long processTaskId, String processTaskStepName) {
        super("工单：" + processTaskId + "的" + processTaskStepName + "步骤无需人工处理");
    }

}
