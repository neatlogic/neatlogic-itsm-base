package neatlogic.framework.process.exception.score;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ScoreTemplateHasRefProcessException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -6956721890886390194L;

	public ScoreTemplateHasRefProcessException(String name) {
		super("评分模版 :" + name + "已关联流程，不可删除或禁用");
	}
}
