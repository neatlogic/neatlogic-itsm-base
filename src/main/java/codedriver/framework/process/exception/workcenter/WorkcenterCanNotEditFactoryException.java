package codedriver.framework.process.exception.workcenter;

import codedriver.framework.exception.core.ApiRuntimeException;

public class WorkcenterCanNotEditFactoryException extends ApiRuntimeException {

	private static final long serialVersionUID = -7680398403672039709L;

	public WorkcenterCanNotEditFactoryException() {
		super("出厂工单类型，无法删除或修改");
	}
}
