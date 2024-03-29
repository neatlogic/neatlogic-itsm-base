package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class WorkcenterHandlerNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 8358695524151979636L;

	public WorkcenterHandlerNotFoundException(String handler) {
		super("找不到类型为：{0}的工单中心处理器", handler);
	}
}
