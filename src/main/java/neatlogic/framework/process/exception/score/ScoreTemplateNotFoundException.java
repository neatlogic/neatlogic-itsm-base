package neatlogic.framework.process.exception.score;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ScoreTemplateNotFoundException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -2067466892281101344L;

	public ScoreTemplateNotFoundException(Long id) {
		super("评分模版 :" + id + "不存在");
	}
}
