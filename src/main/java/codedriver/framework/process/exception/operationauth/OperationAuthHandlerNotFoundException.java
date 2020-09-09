package codedriver.framework.process.exception.operationauth;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class OperationAuthHandlerNotFoundException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -8305325540549621149L;

    public OperationAuthHandlerNotFoundException(String handler) {
		super("找不到类型为：" + handler + "的操作权限组件");
	}
}