package codedriver.framework.process.exception.processtask;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProcessTaskCurrentStepOverOneException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -7832294900666990198L;

    public ProcessTaskCurrentStepOverOneException(List<Long> processTaskIdList) {
        super("工单：'" + processTaskIdList.stream().map(Objects::toString).collect(Collectors.joining()) + "'的当前步骤超过一个");
    }
}
