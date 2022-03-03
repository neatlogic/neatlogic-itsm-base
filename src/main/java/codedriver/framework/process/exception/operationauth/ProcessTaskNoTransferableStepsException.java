/*
 * Copyright(c) 2022 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.operationauth;

/**
 * @author linbq
 * @since 2022/3/1 11:27
 **/
public class ProcessTaskNoTransferableStepsException extends ProcessTaskPermissionDeniedException {
    private static final long serialVersionUID = 9216337410118158646L;

    public ProcessTaskNoTransferableStepsException() {
        super("工单里没有您可以转交的步骤");
    }
}
