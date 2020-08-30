package codedriver.framework.process.exception.score;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class ScoreTemplateNameRepeatException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = 7647275192524314055L;

	public ScoreTemplateNameRepeatException(String name) {
		super("评分模版名 :" + name + "已存在");
	}
}
