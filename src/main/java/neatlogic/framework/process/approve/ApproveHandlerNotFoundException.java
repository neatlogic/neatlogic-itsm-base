package neatlogic.framework.process.approve;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ApproveHandlerNotFoundException extends ProcessTaskRuntimeException {

    public ApproveHandlerNotFoundException(String handler) {
        super("审批处理器：{0} 不存在", handler);
    }
}
