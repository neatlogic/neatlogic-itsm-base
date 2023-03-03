package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class WorkcenterNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = 1901910086387644808L;

	public WorkcenterNameRepeatException(String name) {
		super("exception.process.workcenternamerepeatexception", name);
	}
}
