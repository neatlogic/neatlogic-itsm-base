package codedriver.framework.process.exception.workcenter;

import codedriver.framework.exception.core.ApiRuntimeException;

public class WorkcenterNoModifyAuthException extends ApiRuntimeException {

	private static final long serialVersionUID = 2735647303163896821L;

	public WorkcenterNoModifyAuthException() {
		super("没有'工单中心管理权限'，请联系管理员");
	}
}
