package codedriver.framework.process.exception.score;

import codedriver.framework.exception.core.ApiFieldValidRuntimeException;

public class ScoreTemplateNameRepeatException extends ApiFieldValidRuntimeException {

	private static final long serialVersionUID = 7647275192524314055L;

	public ScoreTemplateNameRepeatException(String name) {
		super("评分模版 :" + name + "已存在");
	}
}
