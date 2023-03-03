package neatlogic.framework.process.exception.process;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessDraftNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = -7136586762433623298L;
	
	public ProcessDraftNotFoundException(String uuid) {
		super("exception.process.processdraftnotfoundexception", uuid);
	}
}
