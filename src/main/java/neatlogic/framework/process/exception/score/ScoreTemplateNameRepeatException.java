package neatlogic.framework.process.exception.score;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ScoreTemplateNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = 7647275192524314055L;

	public ScoreTemplateNameRepeatException(String name) {
		super("评分模版：'" + name + "'已存在");
	}
}
