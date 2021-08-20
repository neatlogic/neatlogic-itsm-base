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
            "<#list DATA.form as attributeItem>\t\t\t\t   \n" +
            "<#if attributeItem.type=='forminput'>\n" +
            "${attributeItem.label}：\t${attributeItem.dataObj}\n" +
            "<#elseif attributeItem.type=='formtextarea'>\n" +
            "${attributeItem.label}：${attributeItem.dataObj}\n" +
            "<#elseif attributeItem.type=='formeditor'>\n" +
            "${attributeItem.label}：${attributeItem.dataObj}\n" +
            "<#elseif attributeItem.type=='formtime'>\n" +
            "${attributeItem.label}：${attributeItem.dataObj}\n" +
            "<#elseif attributeItem.type=='formdate'>\n" +
            "${attributeItem.label}：${attributeItem.dataObj}\n" +
            "<#elseif attributeItem.type=='formlink'>\n" +
            "${attributeItem.label}：${attributeItem.dataObj}\n" +
            "<#elseif attributeItem.type=='formradio'>\n" +
            "${attributeItem.label}：${attributeItem.dataObj}\n" +
            "<#elseif attributeItem.type=='formcheckbox'>\n" +
            "${attributeItem.label}：\n" +
            "\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t${dataItem}\n" +
            "\t<#if dataItem_has_next>、</#if>\n" +
            "\t</#list>\n" +
            "<#elseif attributeItem.type=='formcascadelist'>\n" +
            "${attributeItem.label}：\n" +
            "\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t${dataItem}\n" +
            "\t<#if dataItem_has_next>、</#if>\n" +
            "\t</#list>\n" +
            "<#elseif attributeItem.type=='formselect'>\n" +
            "${attributeItem.label}：\n" +
            "\t<#if attributeItem.isMultiple == 0>\n" +
            "\t${attributeItem.dataObj}\n" +
            "\t<#elseif attributeItem.isMultiple == 1>\n" +
            "\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t${dataItem}\n" +
            "\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t</#list>\n" +
            "\t</#if>\n" +
            "<#elseif attributeItem.type=='formuserselect'>\n" +
            "${attributeItem.label}：\n" +
            "\t<#if attributeItem.isMultiple == 0>\n" +
            "\t${attributeItem.dataObj}\n" +
            "\t<#elseif attributeItem.isMultiple == 1>\n" +
            "\t\t<#list attributeItem.dataObj as dataItem>\n" +
            "\t\t${dataItem}\n" +
            "\t\t<#if dataItem_has_next>、</#if>\n" +
            "\t\t</#list>\n" +
            "\t</#if>\n" +
            "<#elseif attributeItem.type=='formstaticlist'>\n" +
            "${attributeItem.label}：暂无\n" +
            "<#elseif attributeItem.type=='formdynamiclist'>\n" +
            "${attributeItem.label}：暂无\n" +
            "<#elseif attributeItem.type=='cientityselect'>\n" +
            "${attributeItem.label}：暂无\n" +
            "</#if>\n" +
            "<#if attributeItem_has_next><br></#if>\n" +
            "</#list>\n" +
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
