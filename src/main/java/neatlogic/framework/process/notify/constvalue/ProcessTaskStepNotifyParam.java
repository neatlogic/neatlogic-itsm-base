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

package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.common.constvalue.ParamType;
import neatlogic.framework.notify.core.INotifyParam;
import neatlogic.framework.util.I18n;

/**
 * @author linbq
 * @since 2021/10/16 13:49
 **/
public enum ProcessTaskStepNotifyParam implements INotifyParam {

    STEPID("stepId", new I18n("nfpnc.processtaskstepnotifyparam.text.stepid"), ParamType.NUMBER),
    STEPNAME("stepName", new I18n("nfpnc.processtaskstepnotifyparam.text.stepname"), ParamType.STRING),
    PROCESS_TASK_STEP_ACTIVE_TIME("processTaskStepActiveTime", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_active_time"), ParamType.STRING),
    PROCESS_TASK_STEP_START_TIME("processTaskStepStartTime", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_start_time"), ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_TIME("processTaskStepTransferTime", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_transfer_time"), ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_CONTENT("processTaskStepTransferContent", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_transfer_content"), ParamType.STRING),
    PROCESS_TASK_STEP_TRANSFER_WORKER("processTaskStepTransferWorker", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_transfer_worker"), ParamType.STRING),
    PROCESS_TASK_CURRENT_STEP_COMPLETE_CONTENT("processTaskCurrentStepCompleteContent", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_current_step_complete_content"), ParamType.STRING),
    PROCESS_TASK_CURRENT_STEP_BACK_CONTENT("processTaskCurrentStepBackContent", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_current_step_back_content"), ParamType.STRING),
    PROCESS_TASK_STEP_SLA("processTaskStepSla", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_sla"), ParamType.ARRAY, "<#if DATA.processTaskStepSla?? && (DATA.processTaskStepSla?size > 0)>\n" +
            "\t\t\t\t<#list DATA.processTaskStepSla as item>\n" +
            "\t\t\t\t\t${item_index}-${item.id}-${item.name}-${item.status}-${item.timeLeft}-${item.timeLeftFormat}-${item.timeCostFormat}-${item.expireTimeFormat}\n" +
            "\t\t\t\t\t<#if item_has_next>,</#if>\n" +
            "\t\t\t\t</#list>\n" +
            "\t\t\t</#if>"),
    STEPWORKER("stepWorker", new I18n("nfpnc.processtaskstepnotifyparam.text.stepworker"), ParamType.STRING),
    STEPSTAYTIME("stepStayTime", new I18n("nfpnc.processtaskstepnotifyparam.text.stepstaytime"), ParamType.STRING),
    REASON("reason", new I18n("nfpnc.processtaskstepnotifyparam.text.reason"), ParamType.STRING),
    PROCESS_TASK_STEP_PAUSE_CONTENT("processTaskStepPauseContent", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_pause_content"), ParamType.STRING),
    PROCESS_TASK_STEP_RECOVER_CONTENT("processTaskStepRecoverContent", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_recover_content"), ParamType.STRING),
    PROCESS_TASK_STEP_RETREAT_CONTENT("processTaskStepRetreatContent", new I18n("nfpnc.processtaskstepnotifyparam.text.process_task_step_retreat_content"), ParamType.STRING),
    STEP_COMMENT("stepComment", new I18n("nfpnc.processtaskstepnotifyparam.text.step_comment"), ParamType.STRING),
    STEP_COMMENT_USER("stepCommentUser", new I18n("nfpnc.processtaskstepnotifyparam.text.step_comment_user"), ParamType.STRING),
    STEP_COMMENT_LIST("stepCommentList", new I18n("nfpnc.processtaskstepnotifyparam.text.step_comment_list"), ParamType.ARRAY),
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
