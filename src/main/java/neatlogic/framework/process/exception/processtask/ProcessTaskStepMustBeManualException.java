package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepMustBeManualException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -1806194129752166066L;

    public ProcessTaskStepMustBeManualException(Long processTaskId, String processTaskStepName) {
        super("工单：{0}的{1}步骤必须在页面处理", processTaskId, processTaskStepName);
    }

}
