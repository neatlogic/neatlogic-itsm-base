/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.common.constvalue.ParamType;
import neatlogic.framework.notify.core.INotifyParam;
import neatlogic.framework.util.$;

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
