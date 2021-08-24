package codedriver.framework.process.constvalue;

import codedriver.framework.common.constvalue.ParamType;

public enum ProcessTaskParams {
    ID("id", "工单ID", ParamType.NUMBER),
    SERIALNUMBER("serialNumber", "工单号", ParamType.STRING),
    TITLE("title", "标题", ParamType.STRING),
    PRIORITYNAME("priorityname", "优先级", ParamType.STRING),
    CHANNELPATH("channelpath", "服务路径", ParamType.STRING),
    CHANNELTYPENAME("channeltypename", "服务类型", ParamType.STRING),
    CONTENT("content", "上报内容", ParamType.STRING),
    STARTTIME("starttime", "开始时间", ParamType.DATE),
    ENDTIME("endtime", "结束时间", ParamType.DATE),
    //    EXPIREDTIME("expiretime", "剩余时间",ParamType.STRING),
    OWNERNAME("ownername", "上报人", ParamType.STRING),
    REPORTERNAME("reportername", "代报人", ParamType.STRING),
    OWNERCOMPANYLIST("ownercompanylist", "上报人公司列表", ParamType.ARRAY),
    STATUSTEXT("statustext", "工单状态", ParamType.STRING),
    STEPID("stepid", "步骤id", ParamType.NUMBER),
    STEPNAME("stepName", "步骤名", ParamType.STRING),
    CHANGESTEPNAME("changestepname", "变更步骤步骤名", ParamType.STRING),
    STEPWORKER("stepworker", "步骤处理人", ParamType.STRING),
    CHANGESTEPWORKER("changestepworker", "变更步骤处理人", ParamType.STRING),
    SUBTASKWORKER("subtaskworker", "子任务处理人", ParamType.STRING),
    SUBTASKCONTENT("subtaskcontent", "子任务内容", ParamType.STRING),
    SUBTASKDEADLINE("subtaskdeadline", "子任务期望完成时间", ParamType.DATE),
    OPERATOR("operator", "操作人", ParamType.STRING),
    REASON("reason", "原因", ParamType.STRING),
    FORM("form", "表单", ParamType.ARRAY, "<#if DATA.form??>\n" +
            "\t<#list DATA.form as attributeItem>\t\t\t\t   \n" +
            "\t\t<#if attributeItem.type=='forminput'>\n" +
            "\t\t\t${attributeItem.label}：${attributeItem.dataObj}\n" +
            "\t\t<#elseif attributeItem.type=='formtextarea'>\n" +
            "\t\t\t${attributeItem.label}：${attributeItem.dataObj}\n" +
            "\t\t<#elseif attributeItem.type=='formeditor'>\n" +
            "\t\t\t${attributeItem.label}：${attributeItem.dataObj}\n" +
            "\t\t<#elseif attributeItem.type=='formtime'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj}\n" +
            "\t\t<#elseif attributeItem.type=='formdate'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj}\n" +
            "\t\t<#elseif attributeItem.type=='formlink'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj}\n" +
            "\t\t<#elseif attributeItem.type=='formradio'>\n" +
            "\t\t${attributeItem.label}：${attributeItem.dataObj}\n" +
            "\t\t<#elseif attributeItem.type=='formcheckbox'>\n" +
            "\t\t${attributeItem.label}：\n" +
            "\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t${dataItem}\n" +
            "\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t</#list>\n" +
            "\t\t<#elseif attributeItem.type=='formcascadelist'>\n" +
            "\t\t\t${attributeItem.label}：\n" +
            "\t\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t\t${dataItem}\n" +
            "\t\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t\t</#list>\n" +
            "\t\t<#elseif attributeItem.type=='formselect'>\n" +
            "\t\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.isMultiple == 0>\n" +
            "\t\t\t\t${attributeItem.dataObj}\n" +
            "\t\t\t<#elseif attributeItem.isMultiple == 1>\n" +
            "\t\t\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t\t\t${dataItem}\n" +
            "\t\t\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t\t\t</#list>\n" +
            "\t\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='formuserselect'>\n" +
            "\t\t\t${attributeItem.label}：\n" +
            "\t\t\t<#if attributeItem.isMultiple == 0>\n" +
            "\t\t\t\t${attributeItem.dataObj}\n" +
            "\t\t\t<#elseif attributeItem.isMultiple == 1>\n" +
            "\t\t\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t\t\t\t${dataItem}\n" +
            "\t\t\t\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t\t\t</#list>\n" +
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
            "\t\t<#if attributeItem.dataObj??>\n" +
            "\t\t\t<table border=\"1\" style=\"border-collapse:collapse\">\n" +
            "\t\t\t\t<#assign theadList = attributeItem.dataObj.theadList/>\n" +
            "\t\t\t\t<#if theadList??>\n" +
            "\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t<th>${thead.title}</th>\n" +
            "\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t</tr>\n" +
            "\t\t\t\t</#if>\n" +
            "\t\t\t\t<#assign tbodyList = attributeItem.dataObj.tbodyList/>\n" +
            "\t\t\t\t<#if tbodyList??>\n" +
            "\t\t\t\t\t<#list tbodyList as tbody>\n" +
            "\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t<#list theadList as thead>\n" +
            "\t\t\t\t\t\t\t<#assign colKey = thead.key/>\n" +
            "\t\t\t\t\t\t\t<td>\n" +
            "\t\t\t\t\t\t\t\t<#assign cell = tbody[colKey]/>\n" +
            "\t\t\t\t\t\t\t\t${cell.text}\n" +
            "\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t</#list>\n" +
            "\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t</#list>\n" +
            "\t\t\t\t</#if>\n" +
            "\t\t\t</table>\n" +
            "\t\t</#if>\n" +
            "\t\t<#elseif attributeItem.type=='cientityselect'>\n" +
            "\t\t\t${attributeItem.label}：暂不支持显示内容\n" +
            "\t\t</#if>\n" +
            "\t\t<#if attributeItem_has_next>\n" +
            "\t\t\t<br>\n" +
            "\t\t</#if>\n" +
            "\t</#list>\n" +
            "</#if>")
    ;

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
