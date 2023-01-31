/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.constvalue;

public enum ProcessTaskStepDataType implements IProcessTaskStepDataType {
    STEPDRAFTSAVE("stepdraftsave", "步骤草稿暂存"),
    AUTOMATIC("automatic", "auto节点数据");
    private final String value;
    private final String text;

    ProcessTaskStepDataType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text;
    }

}
