/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.operationauth;

public class ProcessTaskStepHandledByOthersException extends ProcessTaskPermissionDeniedException {
    private static final long serialVersionUID = 9216337410118158665L;
    public ProcessTaskStepHandledByOthersException(String userId, String userName) {
        super("【" + userName + "|" + userId + "】用户已经处理了此步骤");
    }
}
