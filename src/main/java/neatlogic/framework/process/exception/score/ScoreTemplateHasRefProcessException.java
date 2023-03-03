package neatlogic.framework.process.exception.score;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ScoreTemplateHasRefProcessException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -6956721890886390194L;

	public ScoreTemplateHasRefProcessException(String name) {
		super("exception.process.scoretemplatehasrefprocessexception", name);
	}
}
