package neatlogic.framework.process.exception.process;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessReferencedCannotBeDeleteException extends ApiRuntimeException {

	private static final long serialVersionUID = -6726808807183027552L;

	public ProcessReferencedCannotBeDeleteException(String uuid) {
		super("exception.process.processreferencedcannotbedeleteexception", uuid);
	}
}
