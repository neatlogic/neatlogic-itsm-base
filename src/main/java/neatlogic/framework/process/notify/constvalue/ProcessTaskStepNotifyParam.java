/*
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

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

package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.common.constvalue.ParamType;
import neatlogic.framework.notify.core.INotifyParam;

/**
 * @author linbq
 * @since 2021/10/16 13:49
 **/
public enum ProcessTaskStepNotifyParam implements INotifyParam {

    STEPID("stepId", "步骤id", ParamType.NUMBER),
    STEPNAME("stepName", "步骤名", ParamType.STRING),
    PROCESS_TASK_STEP_ACTIVE_TIME("processTaskStepActiveTime", "步骤激活时间", ParamType.STRING),
    PROCESS_TASK_STEP_START_TIME("processTaskStepStartTime", "步骤开始时间", ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_TIME("processTaskStepTransferTime", "步骤转交时间", ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_CONTENT("processTaskStepTransferContent", "步骤转交原因", ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_WORKER("processTaskStepTransferWorker", "步骤转交对象", ParamType.STRING),
    PROCESS_TASK_STEP_COMPLETE_CONTENT("processTaskStepCompleteContent", "步骤处理内容", ParamType.STRING),
    PROCESS_TASK_STEP_SLA("processTaskStepSla", "步骤时效", ParamType.ARRAY),
    STEPWORKER("stepWorker", "步骤处理人", ParamType.STRING),
    STEPSTAYTIME("stepStayTime", "步骤停留时间", ParamType.ARRAY),
    REASON("reason", "原因", ParamType.STRING),
    ;

    private final String value;
    private final String text;
    private final ParamType paramType;

    ProcessTaskStepNotifyParam(String value, String text, ParamType paramType) {
        this.value = value;
        this.text = text;
        this.paramType = paramType;
    }
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public ParamType getParamType() {
        return paramType;
    }
}
