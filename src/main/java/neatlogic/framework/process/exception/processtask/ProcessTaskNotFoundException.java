package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskNotFoundException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 2861954159600811000L;

    public ProcessTaskNotFoundException(String processTask) {
        super("工单：“{0}”不存在", processTask);
    }

    public ProcessTaskNotFoundException() {
        super("exception.process.processtasknotfoundexception.1");
    }
}
