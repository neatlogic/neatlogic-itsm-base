/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.exception.processtask.task;

import neatlogic.framework.process.dto.ProcessTaskStepTaskVo;
import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author lvzk
 * @since 2021/8/31 14:24
 **/
public class ProcessTaskStepTaskNotCompleteException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -7336000187226502999L;

    public ProcessTaskStepTaskNotCompleteException(ProcessTaskStepTaskVo stepTaskVo) {
        super("“{0}” 不满足流转策略: “{1}”", stepTaskVo.getTaskConfigName(), stepTaskVo.getTaskConfigPolicyName());
    }
}
