package neatlogic.framework.process.exception.process;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessStepUtilHandlerNotFoundException extends ProcessTaskRuntimeException {
	
	private static final long serialVersionUID = -5334268232696017057L;

	public ProcessStepUtilHandlerNotFoundException(String handler) {
		super("找不到类型为：" + handler + "的流程工具组件");
	}
}
