package neatlogic.framework.process.approve.core;

import neatlogic.framework.process.constvalue.ProcessTaskFinalStatus;

public interface IApproveHandler {
    String getHandler();

    Long startProcess(String channelUuid, Long id) throws Exception;

    int callback(Long processTaskId, ProcessTaskFinalStatus finalStatus, Long id, String content);
}
