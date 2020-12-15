package codedriver.framework.process.processtaskserialnumberpolicy.core;

import codedriver.framework.asynchronization.thread.CodeDriverThread;
import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

public class ProcessTaskSerialNumberUpdateThread extends CodeDriverThread {

    private IProcessTaskSerialNumberPolicyHandler handler;
    private ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo;

    public ProcessTaskSerialNumberUpdateThread(IProcessTaskSerialNumberPolicyHandler handler, ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo) {
        this.handler = handler;
        this.processTaskSerialNumberPolicyVo = processTaskSerialNumberPolicyVo;
    }

    @Override
    protected void execute() {
        handler.batchUpdateHistoryProcessTask(processTaskSerialNumberPolicyVo);
    }

}
