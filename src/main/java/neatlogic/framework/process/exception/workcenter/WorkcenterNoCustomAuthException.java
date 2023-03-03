package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class WorkcenterNoCustomAuthException extends ApiRuntimeException {

	private static final long serialVersionUID = 5226456960465120940L;

	public WorkcenterNoCustomAuthException() {
		super("exception.process.workcenternocustomauthexception");
	}
}
