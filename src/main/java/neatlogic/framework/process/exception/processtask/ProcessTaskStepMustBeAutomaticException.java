package neatlogic.framework.process.exception.processtask;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskStepMustBeAutomaticException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -1161294616338600219L;

    public ProcessTaskStepMustBeAutomaticException() {
		super("工单步骤 必须为automatic节点类型");
	}
}
