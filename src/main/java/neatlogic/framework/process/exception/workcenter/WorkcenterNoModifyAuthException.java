package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class WorkcenterNoModifyAuthException extends ApiRuntimeException {

	private static final long serialVersionUID = 2735647303163896821L;

	public WorkcenterNoModifyAuthException() {
		super("exception.process.workcenternomodifyauthexception");
	}
}
