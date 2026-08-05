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
import neatlogic.framework.util.$;

/**
 * @author linbq
 * @since 2021/10/15 17:12
 **/
public enum ProcessTaskNotifyParam implements INotifyParam {
    ID("id", "nfpnc.processtasknotifyparam.text.id", ParamType.NUMBER),
    SERIALNUMBER("serialNumber", "nfpnc.processtasknotifyparam.text.serialnumber", ParamType.STRING),
    TITLE("title", "nfpnc.processtasknotifyparam.text.title", ParamType.STRING),
    PRIORITYNAME("priorityName", "nfpnc.processtasknotifyparam.text.priorityname", ParamType.STRING),
    CHANNELNAME("channelName", "nfpnc.processtasknotifyparam.text.channelname", ParamType.STRING),
    CHANNELPATH("channelPath", "nfpnc.processtasknotifyparam.text.channelpath", ParamType.STRING),
    CHANNELTYPENAME("channelTypeName", "nfpnc.processtasknotifyparam.text.channeltypename", ParamType.STRING),
    CONTENT("content", "nfpnc.processtasknotifyparam.text.content", ParamType.STRING),
    STARTTIME("startTime", "nfpnc.processtasknotifyparam.text.starttime", ParamType.DATE),
    STARTDATE("startDate", "nfpnc.processtasknotifyparam.text.startdate", ParamType.DATE),
    ENDTIME("endTime", "nfpnc.processtasknotifyparam.text.endtime", ParamType.DATE),
    OWNERNAME("ownerName", "nfpnc.processtasknotifyparam.text.ownername", ParamType.STRING),
    DEPARTMENTANDOWNERNAME("departmentAndOwnerName", "nfpnc.processtasknotifyparam.text.departmentandownername", ParamType.STRING),
    REPORTERNAME("reporterName", "nfpnc.processtasknotifyparam.text.reportername", ParamType.STRING),
    OWNERCOMPANYLIST("ownerCompanyList", "nfpnc.processtasknotifyparam.text.ownercompanylist", ParamType.ARRAY),
    STATUSTEXT("statusText", "nfpnc.processtasknotifyparam.text.statustext", ParamType.STRING),
    PROCESS_TASK_SCORE("processTaskScore", "nfpnc.processtasknotifyparam.text.process_task_score", ParamType.ARRAY, "<#if DATA.processTaskScore?? && (DATA.processTaskScore?size > 0)>\n" +
            "\t<#list DATA.processTaskScore as item>\n" +
            "\t\t${item_index}-${item.dimensionName}-${item.score}\n" +
            "\t\t<#if item_has_next>,</#if>\n" +
            "\t</#list>\n" +
            "</#if>"),
    PROCESS_TASK_SCORE_CONTENT("processTaskScoreContent", "nfpnc.processtasknotifyparam.text.process_task_score_content", ParamType.STRING),
    PROCESS_TASK_ABORT_CONTENT("processTaskAbortContent", "nfpnc.processtasknotifyparam.text.process_task_abort_content", ParamType.STRING),
    PROCESS_TASK_RECOVER_CONTENT("processTaskRecoverContent", "nfpnc.processtasknotifyparam.text.process_task_recover_content", ParamType.STRING),
    PROCESS_TASK_REDO_CONTENT("processTaskRedoContent", "nfpnc.processtasknotifyparam.text.process_task_redo_content", ParamType.STRING),
    PROCESS_TASK_URGE_USER("processTaskUrgeUser", "nfpnc.processtasknotifyparam.text.process_task_urge_user", ParamType.STRING),
    PROCESS_TASK_URGE_COUNT("processTaskUrgeCount", "nfpnc.processtasknotifyparam.text.process_task_urge_count", ParamType.NUMBER),
    APPROVALCOMMENTLIST("approvalCommentList", "nfpnc.processtasknotifyparam.text.approvalcommentlist", ParamType.ARRAY),
    FORM_TABLE("formTable", "nfpnc.processtasknotifyparam.text.form_table", ParamType.STRING, "<#if DATA.formTable??>\n" +
            "\t${DATA.formTable()}\n" +
            "</#if>"),
    FORM_JSON("formJson", "nfpnc.processtasknotifyparam.text.form_json", ParamType.ARRAY, "<#if DATA.formJson?? && (DATA.formJson?size > 0)>\n" +
            "<#list DATA.formJson as item>\n" +
            "nfpnc.processtasknotifyparam.description.form_json" +
            "<#if item_has_next><br></#if>\n" +
            "</#list>\n" +
            "</#if>\n"),
    ;
    private final String value;
    private final String text;
    private final ParamType paramType;
    private String freemarkerTemplate;

    ProcessTaskNotifyParam(String value, String text, ParamType paramType) {
        this(value, text, paramType, null);
    }

    ProcessTaskNotifyParam(String value, String text, ParamType paramType, String freemarkerTemplate) {
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
        return $.t(text);
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
