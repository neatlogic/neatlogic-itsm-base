package codedriver.framework.process.constvalue;

import codedriver.framework.common.constvalue.ParamType;

public enum ProcessTaskParams {
    ID("id", "工单ID", ParamType.NUMBER),
    SERIALNUMBER("serialNumber", "工单号", ParamType.STRING),
    TITLE("title", "标题", ParamType.STRING),
    PRIORITYNAME("priorityName", "优先级", ParamType.STRING),
    CHANNELPATH("channelPath", "服务路径", ParamType.STRING),
    CHANNELTYPENAME("channelTypeName", "服务类型", ParamType.STRING),
    CONTENT("content", "上报内容", ParamType.STRING),
    STARTTIME("startTime", "开始时间", ParamType.DATE),
    ENDTIME("endTime", "结束时间", ParamType.DATE),
    //    EXPIREDTIME("expiretime", "剩余时间",ParamType.STRING),
    OWNERNAME("ownerName", "上报人", ParamType.STRING),
    REPORTERNAME("reporterName", "代报人", ParamType.STRING),
    OWNERCOMPANYLIST("ownerCompanyList", "上报人公司列表", ParamType.ARRAY),
    OWNERDEPARTMENTLIST("ownerDepartmentList", "上报人公司列表", ParamType.ARRAY),
    STATUSTEXT("statusText", "工单状态", ParamType.STRING),
    STEPID("stepId", "步骤id", ParamType.NUMBER),
    STEPNAME("stepName", "步骤名", ParamType.STRING),
    CHANGESTEPNAME("changeStepName", "变更步骤步骤名", ParamType.STRING),
    STEPWORKER("stepWorker", "步骤处理人", ParamType.STRING),
    CHANGESTEPWORKER("changeStepWorker", "变更步骤处理人", ParamType.STRING),
//    SUBTASKWORKER("subtaskWorker", "子任务处理人", ParamType.STRING),
//    SUBTASKCONTENT("subtaskContent", "子任务内容", ParamType.STRING),
//    SUBTASKDEADLINE("subtaskDeadline", "子任务期望完成时间", ParamType.DATE),
    TASKCONFIGNAME("taskConfigName", "任务名", ParamType.STRING),
    TASKWORKER("taskWorker", "任务处理人", ParamType.STRING),
    TASKCONTENT("taskContent", "任务内容", ParamType.STRING),
    TASKUSERCONTENT("taskUserContent", "任务用户内容", ParamType.STRING),
    OPERATOR("operator", "操作人", ParamType.STRING),
    REASON("reason", "原因", ParamType.STRING),
    FORM("form", "表单", ParamType.ARRAY, "<#if DATA.form??>\n" +
            "\t<#list DATA.form as attributeItem>\t\t\t\t   \n" +
            "\t\t<#if attributeItem.type=='forminput'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj!''}\n" +
            "\t\t<#elseif attributeItem.type=='formtextarea'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj!''}\n" +
            "\t\t<#elseif attributeItem.type=='formeditor'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj!''}\n" +
            "\t\t<#elseif attributeItem.type=='formtime'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj!''}\n" +
            "\t\t<#elseif attributeItem.type=='formdate'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj!''}\n" +
            "\t\t<#elseif attributeItem.type=='formlink'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj!''}\n" +
            "\t\t<#elseif attributeItem.type=='formtreeselect'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj!''}\n" +
            "\t\t<#elseif attributeItem.type=='formradio'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj!''}\n" +
            "\t\t<#elseif attributeItem.type=='formcheckbox'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t<#if attributeItem.dataObj?? && attributeItem.dataObj?size gt 0>\n" +
            "\t\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t\t${dataItem}\n" +
            "\t\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t\t</#list>\n" +
            "\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='formcmdbcientity'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t<#if attributeItem.dataObj?? && attributeItem.dataObj?size gt 0>\n" +
            "\t\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t\t${dataItem}\n" +
            "\t\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t\t</#list>\n" +
            "\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='formcascadelist'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.dataObj?? && attributeItem.dataObj?size gt 0>\n" +
            "\t\t\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t\t\t${dataItem}\n" +
            "\t\t\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t\t\t</#list>\n" +
            "\t\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='formselect'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.isMultiple == 0>\n" +
            "\t\t\t\t${attributeItem.dataObj!''}\n" +
            "\t\t\t<#elseif attributeItem.isMultiple == 1>\n" +
            "\t\t\t\t<#if attributeItem.dataObj?? && attributeItem.dataObj?size gt 0>\n" +
            "\t\t\t\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t\t\t\t${dataItem}\n" +
            "\t\t\t\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t\t\t\t</#list>\n" +
            "\t\t\t\t</#if>\n" +
            "\t\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='formuserselect'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.isMultiple == 0>\n" +
            "\t\t\t\t${attributeItem.dataObj!''}\n" +
            "\t\t\t<#elseif attributeItem.isMultiple == 1>\n" +
            "\t\t\t\t<#if attributeItem.dataObj?? && attributeItem.dataObj?size gt 0>\n" +
            "\t\t\t\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t\t\t\t${dataItem}\n" +
            "\t\t\t\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t\t\t\t</#list>\n" +
            "\t\t\t\t</#if>\n" +
            "\t\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='formstaticlist'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.dataObj??>\n" +
            "\t\t\t\t<table border=\"1\" style=\"border-collapse:collapse\">\n" +
            "\t\t\t\t<#assign theadList = attributeItem.dataObj.theadList/>\n" +
            "\t\t\t\t<#if theadList??>\n" +
            "\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t\t<th>${thead.title}</th>\n" +
            "\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t</tr>\n" +
            "\t\t\t\t</#if>\n" +
            "\t\t\t\t<#assign tbodyList = attributeItem.dataObj.tbodyList/>\n" +
            "\t\t\t\t<#if tbodyList??>\n" +
            "\t\t\t\t<#list tbodyList as tbody>\n" +
            "\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t<#assign colKey = thead.key/>\n" +
            "\t\t\t\t\t\t<td>\n" +
            "\t\t\t\t\t\t\t<#assign cell = tbody[colKey]/>\n" +
            "\t\t\t\t\t\t\t<#if cell.type =='selects' || cell.type =='checkbox'>\n" +
            "\t\t\t\t\t\t\t\t<#list cell.text as text>\n" +
            "\t\t\t\t\t\t\t\t\t${text}\n" +
            "\t\t\t\t\t\t\t\t\t<#if text_has_next>、</#if>\n" +
            "\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t<#elseif cell.type == 'table'>\n" +
            "\t\t\t\t\t\t\t\t<table border=\"1\" style=\"border-collapse:collapse\">\n" +
            "\t\t\t\t\t\t\t\t\t<#assign cellTheadList = cell.text.theadList/>\n" +
            "\t\t\t\t\t\t\t\t\t<#if cellTheadList??>\n" +
            "\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<#list cellTheadList as cellThead>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<th>${cellThead.title}</th>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t<#assign cellTbodyList = cell.text.tbodyList/>\n" +
            "\t\t\t\t\t\t\t\t\t<#if cellTbodyList??>\n" +
            "\t\t\t\t\t\t\t\t\t\t<#list cellTbodyList as cellTbody>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<#list cellTheadList as cellThead>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<#assign cellColKey = cellThead.key/>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#assign cellCell = cellTbody[cellColKey]/>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#if cellCell.type =='selects' || cellCell.type =='checkbox'>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#list cellCell.text as text>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t${text}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#if text_has_next>、</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#else>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t${cellCell.text}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t<#else>\n" +
            "\t\t\t\t\t\t\t\t${cell.text}\n" +
            "\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t</tr>\n" +
            "\t\t\t\t</#list>\n" +
            "\t\t\t\t</#if>\n" +
            "\t\t\t\t</table>\n" +
            "\t\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='formdynamiclist'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.dataObj?? && attributeItem.dataObj?size gt 0>\n" +
            "\t\t\t\t<table border=\"1\" style=\"border-collapse:collapse\">\n" +
            "\t\t\t\t\t<#assign theadList = attributeItem.dataObj.theadList/>\n" +
            "\t\t\t\t\t<#if theadList?? && theadList?size gt 0>\n" +
            "\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t\t<th>${thead.title}</th>\n" +
            "\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t<#assign tbodyList = attributeItem.dataObj.tbodyList/>\n" +
            "\t\t\t\t\t<#if tbodyList?? && tbodyList?size gt 0>\n" +
            "\t\t\t\t\t\t<#list tbodyList as tbody>\n" +
            "\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t\t\t<#assign colKey = thead.key/>\n" +
            "\t\t\t\t\t\t\t\t<td>\n" +
            "\t\t\t\t\t\t\t\t\t<#assign cell = tbody[colKey]/>\n" +
            "\t\t\t\t\t\t\t\t\t<#if cell?? && cell?size gt 0>\n" +
            "\t\t\t\t\t\t\t\t\t\t<#assign type = cell.type/>\n" +
            "\t\t\t\t\t\t\t\t\t\t<#if type == 'selects' || type == 'checkbox'>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<#assign textList = cell.text/>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<#if textList?? && textList?size gt 0>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<#list textList as text>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t${text}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<#if text_has_next>、</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t<#else>\n" +
            "\t\t\t\t\t\t\t\t\t\t${cell.text}\n" +
            "\t\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t</#if>\n" +
            "\t\t\t\t</table>\n" +
            "\t\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='formaccounts'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.dataObj?? && attributeItem.dataObj?size gt 0>\n" +
            "\t\t\t\t<table border=\"1\" style=\"border-collapse:collapse\">\n" +
            "\t\t\t\t\t<#assign theadList = attributeItem.dataObj.theadList/>\n" +
            "\t\t\t\t\t<#if theadList?? && theadList?size gt 0>\n" +
            "\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t\t<th>${thead.title}</th>\n" +
            "\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t<#assign tbodyList = attributeItem.dataObj.tbodyList/>\n" +
            "\t\t\t\t\t<#if tbodyList?? && tbodyList?size gt 0>\n" +
            "\t\t\t\t\t\t<#list tbodyList as tbody>\n" +
            "\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t\t\t<#assign colKey = thead.key/>\n" +
            "\t\t\t\t\t\t\t\t<td>\n" +
            "\t\t\t\t\t\t\t\t\t${tbody[colKey]!''}\n" +
            "\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t</#if>\n" +
            "\t\t\t\t</table>\n" +
            "\t\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='cientityselect'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.dataObj?? && attributeItem.dataObj?size gt 0>\n" +
            "\t\t\t\t<#list attributeItem.dataObj as tableObj>\n" +
            "\t\t\t\t\t<#if tableObj?? && tableObj?size gt 0>\n" +
            "\t\t\t\t\t\t${tableObj.ciLabel!''}<br>\n" +
            "\t\t\t\t\t\t<table border=\"1\">\n" +
            "\t\t\t\t\t\t\t<#assign theadList = tableObj.theadList/>\n" +
            "\t\t\t\t\t\t\t<#if theadList?? && theadList?size gt 0>\n" +
            "\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t\t\t\t<th>${thead.title}</th>\n" +
            "\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t<#assign tbodyList = tableObj.tbodyList/>\n" +
            "\t\t\t\t\t\t\t<#if tbodyList?? && tbodyList?size gt 0>\n" +
            "\t\t\t\t\t\t\t\t<#list tbodyList as tbody>\n" +
            "\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t\t\t\t\t<#assign colKey = thead.key/>\n" +
            "\t\t\t\t\t\t\t\t\t\t<td>\t\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<#if tbody[colKey]??>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<#assign cell = tbody[colKey]/>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<#if cell?? && cell?size gt 0>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<#if cell.actualValueList??>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#assign isMultiple = (cell.config.isMultiple)!0/>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#assign actualValueList = cell.actualValueList/>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#if actualValueList?? && actualValueList?size gt 0>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#list actualValueList as actualValue>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#if isMultiple == 1>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#if actualValue?? && actualValue?size gt 0>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t${actualValue.text!''}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#else>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t${actualValue}\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<#if actualValue_has_next>、</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</#if> \n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t\t</#if>\n" +
            "\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t</#if>\n" +
            "\t\t\t\t</#list>\n" +
            "\t\t\t</#if>\n" +
            "\t\t</#if>\n" +
            "\t\t<#if attributeItem_has_next>\n" +
            "\t\t\t<br>\n" +
            "\t\t</#if>\n" +
            "\t</#list>\n" +
            "</#if>");

    private String value;
    private String text;
    private ParamType paramType;
    private String freemarkerTemplate;

    private ProcessTaskParams(String value, String text, ParamType paramType) {
        this(value, text, paramType, null);
    }

    private ProcessTaskParams(String value, String text, ParamType paramType, String freemarkerTemplate) {
        this.value = value;
        this.text = text;
        this.paramType = paramType;
        this.freemarkerTemplate = freemarkerTemplate;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public ParamType getParamType() {
        return paramType;
    }

    public String getFreemarkerTemplate() {
        if (freemarkerTemplate == null && paramType != null) {
            freemarkerTemplate = paramType.getFreemarkerTemplate(value);
        }
        return freemarkerTemplate;
    }

}
