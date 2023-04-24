/*
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

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
import neatlogic.framework.util.I18n;

/**
 * @author linbq
 * @since 2021/10/16 13:30
 **/
public enum ProcessTaskStepTaskNotifyParam implements INotifyParam {

    TASKCONFIGNAME("taskConfigName", new I18n("enum.process.processtasksteptasknotifyparam.taskconfigname"), ParamType.STRING),
    TASKWORKER("taskWorker", new I18n("enum.process.processtasksteptasknotifyparam.taskworker"), ParamType.STRING),
    TASKCONTENT("taskContent", new I18n("enum.process.processtasksteptasknotifyparam.taskcontent"), ParamType.STRING),
    TASKUSERCONTENT("taskUserContent", new I18n("enum.process.processtasksteptasknotifyparam.taskusercontent"), ParamType.STRING),
    ;

    private final String value;
    private final I18n text;
    private final ParamType paramType;

    ProcessTaskStepTaskNotifyParam(String value, I18n text, ParamType paramType) {
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
        return text.toString();
    }

    @Override
    public ParamType getParamType() {
        return paramType;
    }
}
