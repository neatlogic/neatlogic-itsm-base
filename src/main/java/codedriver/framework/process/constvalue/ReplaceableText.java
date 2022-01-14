/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.constvalue;

/**
 * @author linbq
 * @since 2021/7/15 11:15
 **/
public enum ReplaceableText {
//    SUBTASK("replaceable_subtask", "子任务")
    REPORTCONTENT("replaceable_reportcontent", "上报内容"),
    STEPLIST("replaceable_steplist", "步骤日志"),
    AUDITLIST("replaceable_auditlist", "活动日志"),
    RELATIONLIST("replaceable_relationlist", "关联工单")
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
