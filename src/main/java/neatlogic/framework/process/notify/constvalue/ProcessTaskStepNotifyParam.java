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

package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.common.constvalue.ParamType;
import neatlogic.framework.notify.core.INotifyParam;
import neatlogic.framework.util.I18n;

/**
 * @author linbq
 * @since 2021/10/16 13:49
 **/
public enum ProcessTaskStepNotifyParam implements INotifyParam {

    STEPID("stepId", new I18n("步骤id"), ParamType.NUMBER),
    STEPNAME("stepName", new I18n("步骤名"), ParamType.STRING),
    PROCESS_TASK_STEP_ACTIVE_TIME("processTaskStepActiveTime", new I18n("步骤激活时间"), ParamType.STRING),
    PROCESS_TASK_STEP_START_TIME("processTaskStepStartTime", new I18n("步骤开始时间"), ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_TIME("processTaskStepTransferTime", new I18n("步骤转交时间"), ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_CONTENT("processTaskStepTransferContent", new I18n("步骤转交原因"), ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_WORKER("processTaskStepTransferWorker", new I18n("步骤转交对象"), ParamType.STRING),
    PROCESS_TASK_CURRENT_STEP_COMPLETE_CONTENT("processTaskCurrentStepCompleteContent", new I18n("当前步骤处理内容"), ParamType.STRING),
    PROCESS_TASK_CURRENT_STEP_BACK_CONTENT("processTaskCurrentStepBackContent", new I18n("步骤回退原因"), ParamType.STRING),
    PROCESS_TASK_STEP_SLA("processTaskStepSla", new I18n("步骤时效"), ParamType.ARRAY, "<#if DATA.processTaskStepSla?? && (DATA.processTaskStepSla?size > 0)>\n" +
            "\t\t\t\t<#list DATA.processTaskStepSla as item>\n" +
            "\t\t\t\t\t${item_index}-${item.id}-${item.name}-${item.status}-${item.timeLeft}-${item.timeLeftFormat}-${item.timeCostFormat}-${item.expireTimeFormat}\n" +
            "\t\t\t\t\t<#if item_has_next>,</#if>\n" +
            "\t\t\t\t</#list>\n" +
            "\t\t\t</#if>"),
    STEPWORKER("stepWorker", new I18n("步骤处理人"), ParamType.STRING),
    STEPSTAYTIME("stepStayTime", new I18n("步骤停留时间"), ParamType.ARRAY),
    REASON("reason", new I18n("原因"), ParamType.STRING),
    PROCESS_TASK_STEP_PAUSE_CONTENT("processTaskStepPauseContent", new I18n("步骤暂停原因"), ParamType.STRING),
    PROCESS_TASK_STEP_RECOVER_CONTENT("processTaskStepRecoverContent", new I18n("步骤恢复原因"), ParamType.STRING),
    PROCESS_TASK_STEP_RETREAT_CONTENT("processTaskStepRetreatContent", new I18n("步骤撤回原因"), ParamType.STRING),
    STEP_COMMENT("stepComment", new I18n("步骤回复"), ParamType.STRING),
    STEP_COMMENT_USER("stepCommentUser", new I18n("步骤回复用户"), ParamType.STRING),
    STEP_COMMENT_LIST("stepCommentList", new I18n("步骤回复列表"), ParamType.ARRAY),
    ;

    private final String value;
    private final I18n text;
    private final ParamType paramType;
    private String freemarkerTemplate;

    ProcessTaskStepNotifyParam(String value, I18n text, ParamType paramType) {
        this(value, text, paramType, null);
    }

    ProcessTaskStepNotifyParam(String value, I18n text, ParamType paramType, String freemarkerTemplate) {
        this.value = value;
        this.text = text;
        this.paramType = paramType;
        this.freemarkerTemplate = freemarkerTemplate;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text.toString();
    }

    @Override
    public ParamType getParamType() {
        return paramType;
    }

    @Override
    public String getFreemarkerTemplate() {
        if (freemarkerTemplate == null && paramType != null) {
            freemarkerTemplate = paramType.getFreemarkerTemplate(value);
        }
        return freemarkerTemplate;
    }
}
