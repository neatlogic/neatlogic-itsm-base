package codedriver.framework.process.exception.workcenter;

import codedriver.framework.exception.core.ApiRuntimeException;

public class WorkcenterNoCustomAuthException extends ApiRuntimeException {

	private static final long serialVersionUID = 5226456960465120940L;

	public WorkcenterNoCustomAuthException() {
		super("不是自定义工单分类的创建人，操作失败");
	}
}
