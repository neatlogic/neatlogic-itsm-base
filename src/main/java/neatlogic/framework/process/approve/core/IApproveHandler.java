package neatlogic.framework.process.approve.core;

import neatlogic.framework.process.approve.constvalue.ApproveReply;

public interface IApproveHandler {
    String getHandler();

    Long startProcess(String channelUuid, Long id) throws Exception;

    int callback(Long processTaskId, ApproveReply approveReply, Long id, String content);
}
