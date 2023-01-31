package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNotFoundException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 2861954159600811000L;

    public ProcessTaskNotFoundException(String processTask) {
        super("工单：'" + processTask + "'不存在");
    }

    public ProcessTaskNotFoundException() {
        super("工单不存在");
    }
}
