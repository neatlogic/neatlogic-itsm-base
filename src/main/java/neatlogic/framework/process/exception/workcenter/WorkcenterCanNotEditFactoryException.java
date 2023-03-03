package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class WorkcenterCanNotEditFactoryException extends ApiRuntimeException {

	private static final long serialVersionUID = -7680398403672039709L;

	public WorkcenterCanNotEditFactoryException() {
		super("exception.process.workcentercannoteditfactoryexception");
	}
}
