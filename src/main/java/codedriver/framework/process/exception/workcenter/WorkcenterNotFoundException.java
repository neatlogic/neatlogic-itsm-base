package codedriver.framework.process.exception.workcenter;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class WorkcenterNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -6039692333884409352L;

	public WorkcenterNotFoundException(String handler) {
		super("找不到：'" + handler + "' 工单中心分类");
	}
}
