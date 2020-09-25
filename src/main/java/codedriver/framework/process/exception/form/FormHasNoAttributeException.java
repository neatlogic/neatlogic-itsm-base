package codedriver.framework.process.exception.form;

import codedriver.framework.exception.core.ApiRuntimeException;

public class FormHasNoAttributeException extends ApiRuntimeException {

	private static final long serialVersionUID = -5432947213236587326L;

	public FormHasNoAttributeException(String uuid) {
		super("表单：'" + uuid + "'没有属性");
	}
}
