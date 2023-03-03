package neatlogic.framework.process.exception.operationauth;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class OperationAuthHandlerNotFoundException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -8305325540549621149L;

    public OperationAuthHandlerNotFoundException(String handler) {
		super("exception.process.operationauthhandlernotfoundexception", handler);
	}
}
