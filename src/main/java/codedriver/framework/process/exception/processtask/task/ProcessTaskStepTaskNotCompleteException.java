/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.exception.processtask.task;

import codedriver.framework.process.dto.ProcessTaskStepTaskVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author lvzk
 * @since 2021/8/31 14:24
 **/
public class ProcessTaskStepTaskNotCompleteException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -7336000187226502999L;

    public ProcessTaskStepTaskNotCompleteException(ProcessTaskStepTaskVo stepTaskVo) {
        super("存在流程步骤 '" + stepTaskVo.getTaskConfigName() + ")' 不满足流转策略: '" + stepTaskVo.getTaskConfigPolicyName() + "'，请确认后重试");
    }
}
