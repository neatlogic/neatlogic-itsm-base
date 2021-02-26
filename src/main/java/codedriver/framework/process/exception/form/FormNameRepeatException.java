package codedriver.framework.process.exception.form;

import codedriver.framework.exception.core.ApiFieldValidRuntimeException;

public class FormNameRepeatException extends ApiFieldValidRuntimeException {

	private static final long serialVersionUID = 1901910086387644808L;

	public FormNameRepeatException(String name) {
		super("表单：'" + name + "'已存在");
	}
}
