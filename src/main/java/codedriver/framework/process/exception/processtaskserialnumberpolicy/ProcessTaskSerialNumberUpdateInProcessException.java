package codedriver.framework.process.exception.processtaskserialnumberpolicy;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ProcessTaskSerialNumberUpdateInProcessException extends ApiRuntimeException {

    private static final long serialVersionUID = 1592439340762977706L;

    public ProcessTaskSerialNumberUpdateInProcessException() {
        super("正在批量更新工单号，请稍后...");
    }
}
