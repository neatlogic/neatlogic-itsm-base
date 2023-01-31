package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNextStepOverOneException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -7909863133400893749L;

    public ProcessTaskNextStepOverOneException(Long processTaskId) {
        super("工单：" + processTaskId + "的当前步骤的下一步骤存在多个");
    }

}
