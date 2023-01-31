/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.constvalue;

/**
 * @author linbq
 * @since 2021/7/15 11:15
 **/
public enum ReplaceableText {
//    SUBTASK("replaceable_subtask", "子任务")
    REPORTCONTENT("replaceableReportcontent", "上报内容"),
    STEPLIST("replaceableSteplist", "步骤日志"),
    AUDITLIST("replaceableAuditlist", "活动日志"),
    RELATIONLIST("replaceableRelationlist", "关联工单")
    ;
    private final String value;
    private final String text;

    ReplaceableText(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
