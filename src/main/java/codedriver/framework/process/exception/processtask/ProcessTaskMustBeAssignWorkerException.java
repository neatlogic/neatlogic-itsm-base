/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.processtask;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class ProcessTaskMustBeAssignWorkerException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = 1458252264390320599L;

    public ProcessTaskMustBeAssignWorkerException(String name) {
        super("指派：" + name + "步骤处理人是必填");
    }
}
