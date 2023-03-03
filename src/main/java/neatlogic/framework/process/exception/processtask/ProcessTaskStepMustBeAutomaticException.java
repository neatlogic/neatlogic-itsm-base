package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepMustBeAutomaticException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -1161294616338600219L;

    public ProcessTaskStepMustBeAutomaticException() {
		super("exception.process.processtaskstepmustbeautomaticexception");
	}
}
