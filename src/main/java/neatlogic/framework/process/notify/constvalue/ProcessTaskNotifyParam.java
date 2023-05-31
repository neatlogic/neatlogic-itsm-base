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
import neatlogic.framework.util.I18nUtils;

/**
 * @author linbq
 * @since 2021/10/15 17:12
 **/
public enum ProcessTaskNotifyParam implements INotifyParam {
    ID("id", "common.workorderid", ParamType.NUMBER),
    SERIALNUMBER("serialNumber", "common.tasknumber", ParamType.STRING),
    TITLE("title", "common.title", ParamType.STRING),
    PRIORITYNAME("priorityName", "common.priority", ParamType.STRING),
    CHANNELNAME("channelName", "enum.process.processtasknotifyparam.channelname", ParamType.STRING),
    CHANNELPATH("channelPath", "enum.process.processtasknotifyparam.channelpath", ParamType.STRING),
    CHANNELTYPENAME("channelTypeName", "common.channeltype", ParamType.STRING),
    CONTENT("content", "common.contend", ParamType.STRING),
    STARTTIME("startTime", "common.starttime", ParamType.DATE),
    STARTDATE("startDate", "enum.process.processtasknotifyparam.startdate", ParamType.DATE),
    ENDTIME("endTime", "common.endtime", ParamType.DATE),
    OWNERNAME("ownerName", "common.owner", ParamType.STRING),
    DEPARTMENTANDOWNERNAME("departmentAndOwnerName", "enum.process.processtasknotifyparam.departmentandownername", ParamType.STRING),
    REPORTERNAME("reporterName", "common.reporter", ParamType.STRING),
    OWNERCOMPANYLIST("ownerCompanyList", "enum.process.processtasknotifyparam.ownercompanylist", ParamType.ARRAY),
    STATUSTEXT("statusText", "common.itsm.processtaskstatus", ParamType.STRING),
    PROCESS_TASK_SCORE("processTaskScore", "enum.process.processtasknotifyparam.process_task_score.a", ParamType.ARRAY, "<#if DATA.processTaskScore?? && (DATA.processTaskScore?size > 0)>\n" +
            "\t<#list DATA.processTaskScore as item>\n" +
            "\t\t${item_index}-${item.dimensionName}-${item.score}\n" +
            "\t\t<#if item_has_next>,</#if>\n" +
            "\t</#list>\n" +
            "</#if>"),
    PROCESS_TASK_SCORE_CONTENT("processTaskScoreContent", "enum.process.processtasknotifyparam.process_task_score_content", ParamType.STRING),
    PROCESS_TASK_ABORT_CONTENT("processTaskAbortContent", "enum.process.processtasknotifyparam.process_task_abort_content", ParamType.STRING),
    PROCESS_TASK_RECOVER_CONTENT("processTaskRecoverContent", "enum.process.processtasknotifyparam.process_task_recover_content", ParamType.STRING),
    PROCESS_TASK_REDO_CONTENT("processTaskRedoContent", "enum.process.processtasknotifyparam.process_task_redo_content", ParamType.STRING),
    PROCESS_TASK_URGE_USER("processTaskUrgeUser", "enum.process.processtasknotifyparam.process_task_urge_user", ParamType.STRING),
    PROCESS_TASK_URGE_COUNT("processTaskUrgeCount", "enum.process.processtasknotifyparam.process_task_urge_count", ParamType.NUMBER),
    APPROVALCOMMENTLIST("approvalCommentList", "enum.process.processtasknotifyparam.approvalcommentlist", ParamType.ARRAY),
    FORM_TABLE("formTable", "common.form", ParamType.STRING, "<#if DATA.formTable??>\n" +
            "\t${DATA.formTable()}\n" +
            "</#if>"),
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
        return I18nUtils.getMessage(text);
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
