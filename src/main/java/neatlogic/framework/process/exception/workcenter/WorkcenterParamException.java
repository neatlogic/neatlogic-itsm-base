package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class WorkcenterParamException extends ApiRuntimeException {

	private static final long serialVersionUID = 834889107197646727L;

	public WorkcenterParamException(String name) {
		super("过滤条件参数 “{0}”非法", name);
	}
}
