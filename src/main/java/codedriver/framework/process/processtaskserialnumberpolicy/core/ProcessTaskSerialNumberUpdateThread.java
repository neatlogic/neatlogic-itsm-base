package codedriver.framework.process.processtaskserialnumberpolicy.core;

import codedriver.framework.asynchronization.thread.CodeDriverThread;

public class ProcessTaskSerialNumberUpdateThread extends CodeDriverThread {

    private IProcessTaskSerialNumberPolicyHandler handler;
    private String channelTypeUuid;
    public ProcessTaskSerialNumberUpdateThread(IProcessTaskSerialNumberPolicyHandler handler, String channelTypeUuid) {
        this.handler = handler;
        this.channelTypeUuid = channelTypeUuid;
    }
    
    @Override
    protected void execute() {
        handler.batchUpdateHistoryProcessTask(channelTypeUuid);
    }

}
