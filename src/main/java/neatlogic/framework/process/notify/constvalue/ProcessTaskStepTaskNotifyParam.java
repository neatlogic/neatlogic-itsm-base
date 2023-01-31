/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.common.constvalue.ParamType;
import neatlogic.framework.notify.core.INotifyParam;

/**
 * @author linbq
 * @since 2021/10/16 13:30
 **/
public enum ProcessTaskStepTaskNotifyParam implements INotifyParam {

    TASKCONFIGNAME("taskConfigName", "任务名", ParamType.STRING),
    TASKWORKER("taskWorker", "任务处理人", ParamType.STRING),
    TASKCONTENT("taskContent", "任务内容", ParamType.STRING),
    TASKUSERCONTENT("taskUserContent", "任务用户内容", ParamType.STRING),
    ;

    private final String value;
    private final String text;
    private final ParamType paramType;

    ProcessTaskStepTaskNotifyParam(String value, String text, ParamType paramType) {
        this.value = value;
        this.text = text;
        this.paramType = paramType;
    }
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public ParamType getParamType() {
        return paramType;
    }
}
