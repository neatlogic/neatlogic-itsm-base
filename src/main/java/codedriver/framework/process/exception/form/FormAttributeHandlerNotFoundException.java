package codedriver.framework.process.exception.form;

import codedriver.framework.exception.core.ApiRuntimeException;

public class FormAttributeHandlerNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = 5096527275914516196L;

	public FormAttributeHandlerNotFoundException(String handler) {
		super("表单属性处理器：'" + handler + "'不存在");
	}
}
