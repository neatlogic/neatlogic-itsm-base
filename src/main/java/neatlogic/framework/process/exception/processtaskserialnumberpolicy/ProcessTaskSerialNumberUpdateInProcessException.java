package neatlogic.framework.process.exception.processtaskserialnumberpolicy;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessTaskSerialNumberUpdateInProcessException extends ApiRuntimeException {

    private static final long serialVersionUID = 1592439340762977706L;

    public ProcessTaskSerialNumberUpdateInProcessException() {
        super("exception.process.processtaskserialnumberupdateinprocessexception");
    }
}
