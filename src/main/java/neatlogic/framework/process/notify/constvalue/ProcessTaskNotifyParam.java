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
    ID("id", "工单ID", ParamType.NUMBER),
    SERIALNUMBER("serialNumber", "工单号", ParamType.STRING),
    TITLE("title", "标题", ParamType.STRING),
    PRIORITYNAME("priorityName", "优先级", ParamType.STRING),
    CHANNELNAME("channelName", "服务名称", ParamType.STRING),
    CHANNELPATH("channelPath", "服务路径", ParamType.STRING),
    CHANNELTYPENAME("channelTypeName", "服务类型", ParamType.STRING),
    CONTENT("content", "上报内容", ParamType.STRING),
    STARTTIME("startTime", "开始时间", ParamType.DATE),
    STARTDATE("startDate", "开始日期", ParamType.DATE),
    ENDTIME("endTime", "结束时间", ParamType.DATE),
    OWNERNAME("ownerName", "上报人", ParamType.STRING),
    DEPARTMENTANDOWNERNAME("departmentAndOwnerName", "部门/上报人", ParamType.STRING),
    REPORTERNAME("reporterName", "代报人", ParamType.STRING),
    OWNERCOMPANYLIST("ownerCompanyList", "上报人公司列表", ParamType.ARRAY),
    STATUSTEXT("statusText", "工单状态", ParamType.STRING),
    PROCESS_TASK_SCORE("processTaskScore", "工单评分", ParamType.ARRAY, "<#if DATA.processTaskScore?? && (DATA.processTaskScore?size > 0)>\n" +
            "\t<#list DATA.processTaskScore as item>\n" +
            "\t\t${item_index}-${item.dimensionName}-${item.score}\n" +
            "\t\t<#if item_has_next>,</#if>\n" +
            "\t</#list>\n" +
            "</#if>"),
    PROCESS_TASK_SCORE_CONTENT("processTaskScoreContent", "工单评分内容", ParamType.STRING),
    PROCESS_TASK_ABORT_CONTENT("processTaskAbortContent", "工单取消原因", ParamType.STRING),
    PROCESS_TASK_RECOVER_CONTENT("processTaskRecoverContent", "工单恢复原因", ParamType.STRING),
    PROCESS_TASK_REDO_CONTENT("processTaskRedoContent", "工单重新打开原因", ParamType.STRING),
    PROCESS_TASK_URGE_USER("processTaskUrgeUser", "工单催办用户", ParamType.STRING),
    PROCESS_TASK_URGE_COUNT("processTaskUrgeCount", "工单催办次数", ParamType.NUMBER),
    APPROVALCOMMENTLIST("approvalCommentList", "审批意见", ParamType.ARRAY),
    FORM_TABLE("formTable", "表单", ParamType.STRING, "<#if DATA.formTable??>\n" +
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
