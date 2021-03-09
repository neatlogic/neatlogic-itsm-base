package codedriver.framework.process.notify.constvalue;

import codedriver.framework.notify.core.INotifyPolicyHandlerGroup;

/**
 * @Title: NotifyPolicyHandlerGroup
 * @Package codedriver.framework.notify.constvalue
 * @Description: 工单通知策略分组
 * @Author: linbq
 * @Date: 2021/3/8 16:01
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public enum ProcessNotifyPolicyHandlerGroup implements INotifyPolicyHandlerGroup {
    TASKSTEP("TaskStep", "工单步骤");
    private String value;
    private String text;

    private ProcessNotifyPolicyHandlerGroup(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }
}
