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
import neatlogic.framework.util.I18n;

/**
 * @author linbq
 * @since 2021/10/16 13:30
 **/
public enum ProcessTaskStepTaskNotifyParam implements INotifyParam {

    TASKCONFIGNAME("taskConfigName", new I18n("任务名"), ParamType.STRING),
    TASKWORKER("taskWorker", new I18n("任务处理人"), ParamType.STRING),
    TASKCONTENT("taskContent", new I18n("任务内容"), ParamType.STRING),
    TASKUSERCONTENT("taskUserContent", new I18n("任务用户内容"), ParamType.STRING),
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
