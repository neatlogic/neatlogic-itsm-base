package codedriver.framework.process.operationauth.core;

import java.util.List;

import codedriver.framework.process.dto.ProcessTaskVo;

public abstract class OperationAuthHandlerBase implements IOperationAuthHandler {

    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, Long processTaskStepId, String userType,
        String userUuid, List<String> teamUuidList, List<String> roleUuidList) {
        return false;
    }

    protected boolean checkIsWorker(ProcessTaskVo processTaskVo, String userType, String userUuid,
        List<String> teamUuidList, List<String> roleUuidList) {
        return checkIsWorker(processTaskVo, null, userType, userUuid, teamUuidList, roleUuidList);
    }

    protected boolean checkIsProcessTaskStepUser(ProcessTaskVo processTaskVo, Long processTaskStepId, String userType,
        String userUuid) {
        return false;
    }

    protected boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, Long processTaskStepId,
        String userUuid, List<String> teamUuidList, List<String> roleUuidList) {
        return false;
    }

    protected boolean checkOperationAuthIsConfigured(ProcessTaskVo processTaskVo, String userUuid,
        List<String> teamUuidList, List<String> roleUuidList) {
        return false;
    }
}
