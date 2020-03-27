package codedriver.framework.process.exception.form;

import codedriver.framework.exception.core.ApiRuntimeException;

public class FormAttributeNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = -2778517020600259453L;

	public FormAttributeNotFoundException(String attributeUuid) {
		super("表单属性：'" + attributeUuid + "'不存在");
	}
}
