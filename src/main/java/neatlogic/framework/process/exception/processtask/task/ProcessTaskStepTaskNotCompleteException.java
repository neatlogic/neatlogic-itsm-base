/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
